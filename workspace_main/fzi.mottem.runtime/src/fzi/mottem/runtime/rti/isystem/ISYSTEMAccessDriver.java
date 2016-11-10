package fzi.mottem.runtime.rti.isystem;

import java.util.ArrayList;

import si.isystem.connect.CAddressController;
import si.isystem.connect.CDebugFacade;
import si.isystem.connect.CExecutionController;
import si.isystem.connect.CMemAddress;
import si.isystem.connect.CValueType;
import si.isystem.connect.ConnectionMgr;
import si.isystem.connect.IConnectDebug;
import si.isystem.connect.StrVector;
import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IInspectable;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.ITestWriteable;
import fzi.mottem.model.codemodel.CodeInstance;
import fzi.mottem.model.codemodel.DTFloatingPoint;
import fzi.mottem.model.codemodel.DTInteger;
import fzi.mottem.model.codemodel.DTVoid;
import fzi.mottem.model.codemodel.DataType;
import fzi.mottem.model.codemodel.Function;
import fzi.mottem.model.codemodel.Variable;
import fzi.mottem.model.testrigmodel.Processor;
import fzi.mottem.model.testrigmodel.ProcessorCore;
import fzi.mottem.model.util.ModelUtils;
import fzi.mottem.runtime.EItemProperty;
import fzi.mottem.runtime.interfaces.IRuntime;
import fzi.mottem.runtime.rti.AbstractAccessDriver;
import fzi.mottem.runtime.util.RangeValidator;
import fzi.util.ecore.EcoreUtils;

public class ISYSTEMAccessDriver extends AbstractAccessDriver
{
	private static final String ARCHITECTURE_KEY = "sun.arch.data.model";
	private static final String ARCHITECTURE_32 = "32";
	private static final String ARCHITECTURE_64 = "64";

	private static final String LIB_NAME_64_MAIN = "lib/IConnectJNIx64";
	private static final String LIB_NAME_32_MAIN = "lib/IConnectJNI";
	
	static
	{
		String architecture = System.getProperty(ARCHITECTURE_KEY);

		if (ARCHITECTURE_64.equals(architecture))
		{
			System.loadLibrary(LIB_NAME_64_MAIN);
		}
		else if (ARCHITECTURE_32.equals(architecture))
		{
			System.loadLibrary(LIB_NAME_32_MAIN);
		}
		else
		{
		    throw new IllegalStateException("Unknown architecture:" + architecture);
		}
	}
	
	private final ArrayList<ISYSTEMCtrlSet> _coreCtrlSets = new ArrayList<ISYSTEMCtrlSet>();
	
	private final Processor _processor;
	
	public ISYSTEMAccessDriver(IRuntime runtime, IInspector inspector)
	{
		super(runtime, inspector);

		IInspectable inspectable = getInspector().getInspectorConnector().getInspectable();
		if (inspectable instanceof Processor)
		{
			_processor = (Processor) inspectable;
			for(ProcessorCore core : _processor.getProcessorCores())
			{
				ISYSTEMCtrlSet ctrlSet = new ISYSTEMCtrlSet(core, new ConnectionMgr());
				_coreCtrlSets.add(ctrlSet);
			}
		}
		else
		{
			throw new RuntimeException("ISYSTEMAccessDriver cannot be used with Inspectable of type " + inspectable.getClass().getSimpleName());
		}
	}
	
	@Override
	public void init()
	{
		cleanup();
		
		// connect to WinIDEA and launch all cores
		for(int coreIdx=0; coreIdx < _coreCtrlSets.size(); coreIdx++)
		{
			ISYSTEMCtrlSet ctrlSet = _coreCtrlSets.get(coreIdx);
			
			String workspacePath;
			if (coreIdx == 0)
			{
				workspacePath = ISYSTEMUtil.getAbsoluteWorkspacePath(getInspector()).toOSString();
			}
			else
			{
				workspacePath = _coreCtrlSets.get(0).getConnectionManager().launchCore(coreIdx, true);
			}

			ctrlSet.init(workspacePath);
			
			ctrlSet.getLoaderController().downloadWithoutCode();
			ctrlSet.getDebugFacade().deleteAll();
			
			if (coreIdx == 0)
				ctrlSet.getExecutionController().reset();
			
			// !TODO: use API to trigger "Load Symbols" in WinIDEA (or configure winidea project to do it automatically)
		}
	}

	@Override
	public void cleanup()
	{
		for(ISYSTEMCtrlSet ctrlSet : _coreCtrlSets)
		{
			ctrlSet.cleanup();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(ITestReadable element)
	{
		CDebugFacade debugFacade = getPrimaryCoreCtrlSet().getDebugFacade();
		
		Number val;
		if (element instanceof Variable)
		{
			Variable variable = ((Variable) element);
			String varName = getISYSTEMName(variable);
			
			CValueType value = debugFacade.evaluate(IConnectDebug.EAccessFlags.fRealTime, varName);

			DataType dataType = ((Variable) element).getDataType();
			if (dataType instanceof DTInteger)
			{
				DTInteger intType = (DTInteger)dataType;
				
				if (intType.getBitSize() <= 8)
				{
					Integer int_value = new Integer(value.getInt());
					val = int_value.byteValue();
				}
				else if (intType.getBitSize() <= 16)
				{
					val = (short)value.getInt();
				}
				else if (intType.getBitSize() <= 32)
				{
					val = value.getInt();
				}
				else if (intType.getBitSize() <= 64)
				{
					val = value.getLong();
				}
				else
				{
					throw new RuntimeException("ISYSTEMAccessDriver does not support this integer data type (unexpected bit size)");
				}
				return (T)(val);
			}
			else if (dataType instanceof DTFloatingPoint)
			{
				DTFloatingPoint floatType = (DTFloatingPoint)dataType;

				if (floatType.getExponentBitSize() == 8 &&
					floatType.getSignificandBitSize() == 23)
				{
					val = value.getFloat();
				}
				else if (floatType.getExponentBitSize() == 11 &&
					     floatType.getSignificandBitSize() == 52)
				{
					val = value.getDouble();
			
				}
				else
				{
					throw new RuntimeException("ISYSTEMAccessDriver does not support this floating point data type (unexpected format)");
				}
			}
			else
			{
				throw new RuntimeException("ISYSTEMAccessDriver does not yet support DataType type " + dataType.getClass().getSimpleName());
			}
		}
		else
		{
			throw new RuntimeException("ISYSTEMAccessDriver does not support ITestReadable type " + element.getClass().getSimpleName());
		}

		return (T)(val);
	}

	@Override
	public <T> void setValue(ITestWriteable element, T value)
	{
		CDebugFacade debugFacade = getPrimaryCoreCtrlSet().getDebugFacade();

		if (element instanceof Variable)
		{
			Variable variable = (Variable)element;
			String varName = getISYSTEMName(variable);
			DataType dataType = variable.getDataType();
			
			if (dataType instanceof DTInteger)
			{
				DTInteger intType = (DTInteger)dataType;
				int bitSize = intType.getBitSize();

				if (bitSize == 8 && RangeValidator.isInByteRange((Number) value))
				{
					
					byte value_byte = ((Number)value).byteValue();
					CValueType varValue = new CValueType(bitSize, value_byte);
					debugFacade.modify(IConnectDebug.EAccessFlags.fRealTime, varName, varValue);
				}
				else if (bitSize == 16 && RangeValidator.isInShortRange((Number)value))
				{
					Short value_short = ((Number)value).shortValue();
					CValueType varValue = new CValueType(bitSize, value_short);
					debugFacade.modify(IConnectDebug.EAccessFlags.fRealTime, varName, varValue);
				}
				else if (bitSize == 32 && RangeValidator.isInIntegerRange((Number)value))
				{
					Integer value_int = ((Number)value).intValue();
					CValueType varValue = new CValueType(bitSize, value_int);
					debugFacade.modify(IConnectDebug.EAccessFlags.fRealTime, varName, varValue);
				}
				else if (bitSize == 64 && RangeValidator.isInLongRange((Number)value))
				{
					Long value_Long = ((Number)value).longValue();
					CValueType varValue = new CValueType(bitSize, value_Long);
					debugFacade.modify(IConnectDebug.EAccessFlags.fRealTime, varName, varValue);
				}
				else
				{
					Long castedValue = ((Number)value).longValue();
					throw new RuntimeException(castedValue + " is out of range, not written.");
				}
			}
			
			else if (dataType instanceof DTFloatingPoint)
			{				
				DTFloatingPoint floatType = (DTFloatingPoint)dataType;
				
				if ((floatType.getExponentBitSize() == 8 &&
						floatType.getSignificandBitSize() == 23))
				{
					Float value_float = ((Number)value).floatValue();
					CValueType wrapped_value = new CValueType(value_float);
					debugFacade.modify(IConnectDebug.EAccessFlags.fRealTime, varName, wrapped_value);
				}
				else if ((floatType.getExponentBitSize() == 11 &&
					     floatType.getSignificandBitSize() == 52))
				{
					Double value_double = ((Number)value).doubleValue();
					CValueType wrapped_value = new CValueType(value_double);
					debugFacade.modify(IConnectDebug.EAccessFlags.fRealTime, varName, wrapped_value);
				}
				else
				{
					Double castedValue = ((Number)value).doubleValue();
					throw new RuntimeException(castedValue + " is out of range, not written.");
				}
			}
			else
			{
				throw new RuntimeException("ISYSTEMAccessDriver does not yet support DataType type " + dataType.getClass().getSimpleName());
			}
		}
		else
		{
			throw new RuntimeException("ISYSTEMAccessDriver does not support ITestWriteable type " + element.getClass().getSimpleName());
		}		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getItemPropertyValue(ITestReferenceable element, EItemProperty property)
	{
		CDebugFacade debugFacade;
		
		switch (property) 
		{
			case InstructionPointer:
				debugFacade = getCoreCtrlSet((IExecutor)element).getDebugFacade();
				CValueType wrappedResult = debugFacade.readRegister(IConnectDebug.EAccessFlags.fRealTime, "PC");
				Long result = wrappedResult.getLong();
				return (T)result;
				
			case Address:
				debugFacade = getPrimaryCoreCtrlSet().getDebugFacade();
				CAddressController adrController = debugFacade.getAddressController();
				
				String elementName = getISYSTEMName(element);

				if (element instanceof Variable)
				{
					element = (Variable)element;
					CMemAddress wrappedVarAdr = adrController.getVariableAddress(elementName);
					Long varAdr = wrappedVarAdr.getAddress();
					return (T)varAdr;
				}
				else if (element instanceof Function)
				{
					CMemAddress wrappedFuncAdr = adrController.getFunctionAddress(elementName);
					Long funcAdr = wrappedFuncAdr.getAddress();
					return (T)funcAdr;	
				}
				
			default:
				RuntimeException error = new RuntimeException("The register " + property +
															  " is unknown to ISYSTEMAccessDriver.");
				throw error;
		}
	}

	@Override
	public <T> void setItemPropertyValue(ITestReferenceable element, EItemProperty property, T value)
	{
		CDebugFacade debugFacade;
		
		switch (property) 
		{
			case InstructionPointer:
				debugFacade = getCoreCtrlSet((IExecutor)element).getDebugFacade();
				int regBitSize = 32;
				Long newIPValue = (Long)value;
				CValueType wrappedIPValue = new CValueType(regBitSize, newIPValue);
				debugFacade.writeRegister(IConnectDebug.EAccessFlags.fRealTime, "PC", wrappedIPValue);
				break;
								
			default:
				RuntimeException error = new RuntimeException("The register " + property +
															  " is unknown to ISYSTEMAccessDriver.");
				throw error;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute(IExecutor executor, ITestCallable executableElement, Object... arguments)
	{
		CExecutionController executionController = getCoreCtrlSet(executor).getExecutionController();

		Number val;
		
		if (executableElement instanceof Function)
		{
			Function function = (Function) executableElement;
			String funcName = getISYSTEMName(function);

			StrVector params = new StrVector();
			for (Object arg : arguments)
			{
				params.add(String.valueOf(arg));
			}

			String funcReturn = executionController.call(funcName, params);
			DataType returnType = function.getDataType();

			if (returnType instanceof DTVoid)
			{				
				val = null;
			}
			else if (returnType instanceof DTInteger)
			{
				DTInteger intResult = (DTInteger)returnType;
				int bitSize = intResult.getBitSize();
				
				if (bitSize <= 8)
				{
					int beginIndex = funcReturn.indexOf('(') + 1;
					int endIndex = funcReturn.indexOf(')');
					funcReturn = funcReturn.substring(beginIndex, endIndex);
					Integer byte_value = Integer.decode(funcReturn);
					val = byte_value.byteValue();
				}
				else if (bitSize <= 16)
				{
					Integer short_value = Integer.decode(funcReturn);
					val = short_value.shortValue();
				}
				else if (bitSize <= 32)
				{
					Integer int_value = Integer.decode(funcReturn);
					val = int_value;
				}
				else if (bitSize <= 64)
				{
					Long long_value = Long.decode(funcReturn);
					val = long_value;
				}
				else
				{
					throw new RuntimeException("ISYSTEMAccessDriver does not support this integer data type (unexpected bit size)");
				}
			}
			else if (returnType instanceof DTFloatingPoint)
			{
				DTFloatingPoint floatType = (DTFloatingPoint)returnType;

				if (floatType.getExponentBitSize() == 8 &&
					floatType.getSignificandBitSize() == 23)
				{
					Float float_value = Float.valueOf(funcReturn);
					val = float_value;
				}
				else if (floatType.getExponentBitSize() == 11 &&
					     floatType.getSignificandBitSize() == 52)
				{
					Double double_value = Double.valueOf(funcReturn);
					val = double_value;
			
				}
				else
				{
					throw new RuntimeException("ISYSTEMAccessDriver does not support this floating point data type (unexpected format)");
				}
			}
			else
			{
				throw new RuntimeException("ISYSTEMAccessDriver does not yet support DataType type " + returnType.getClass().getSimpleName());
			}			
		}else
		{
			throw new RuntimeException("ISYSTEMAccessDriver does not support ITestCallable type " + executableElement.getClass().getSimpleName());
		}

		return (T)val;
	}
	
	@Override
	public void run(IExecutor executor)
	{
		CExecutionController executionController = getCoreCtrlSet(executor).getExecutionController();
		executionController.run();
		executionController.waitUntilStopped();
	}

	@Override
	public void runUntil(IExecutor executor, ITestCallable executableElement)
	{
		CExecutionController executionController = getCoreCtrlSet(executor).getExecutionController();
		executionController.runUntilFunction(getISYSTEMName(executableElement));
		executionController.waitUntilStopped();
	}
	
	@Override
	public void stop(IExecutor executor)
	{
		CExecutionController executionController = getCoreCtrlSet(executor).getExecutionController();
		executionController.stop();
	}

	@Override
	public void breakAt(IExecutor executor, ITestCallable executableElement)
	{
		CDebugFacade debugFacade = getCoreCtrlSet(executor).getDebugFacade();
		String executableElementName = getISYSTEMName(executableElement);
		
		debugFacade.setBP(executableElementName);
//		debugFacade.setHWBP(
//				IConnectDebug.EBreakpointFlags.bHW_accAny.swigValue(),
//				IConnectDebug.EBreakpointFlags.bHW_SizeDefault.swigValue(),
//				IConnectDebug.EBreakpointFlags.bHW_DataLSB0.swigValue(),
//				false,
//				executableElementName, 
//				0
//			);
	}
	
	public void prepareExecutionStart(IExecutor executor, ITestCallable executableElement, Object... arguments)
	{
		CExecutionController executionController = getCoreCtrlSet(executor).getExecutionController();
		String isysName = getISYSTEMName(executableElement);
		executionController.gotoFunction(isysName);
	}
	
	public void prepareExecutionStop(IExecutor executor, ITestCallable executableElement)
	{
		CDebugFacade debugFacade = getCoreCtrlSet(executor).getDebugFacade();
		debugFacade.setBP(getISYSTEMName(executableElement));
	}
	
	public String getISYSTEMName(ITestReferenceable element)
	{
		String elementName = element.getName();
		CodeInstance ci = EcoreUtils.getContainerInstanceOf(element, CodeInstance.class);
		
		String binaryName = ISYSTEMUtil.getWinIDEABinaryName(ci.getBinaryFile());
		if (binaryName != null && !binaryName.isEmpty())
			return elementName + ",," + binaryName;
		
		return elementName;
	}
	
	public Processor getProcessor()
	{
		return _processor;
	}

	public ConnectionMgr getPrimaryConnectionManager()
	{
		return getPrimaryCoreCtrlSet().getConnectionManager();
	}
	
	public ISYSTEMCtrlSet getPrimaryCoreCtrlSet()
	{
		return _coreCtrlSets.get(0);
	}
	
	public ISYSTEMCtrlSet getCoreCtrlSet(IExecutor executor)
	{
		if (executor instanceof ProcessorCore)
		{
			int coreIndex = ModelUtils.getProcessorCoreIndex((ProcessorCore)executor);
			
			if (coreIndex < 0)
				throw new RuntimeException("Could not get ISYSTEMCtrlSet for ProcessorCore " + executor.getName());
			
			return _coreCtrlSets.get(coreIndex);
		}
		else
		{
			throw new RuntimeException("Could not get ISYSTEMCtrlSet for Executor " + executor.getName() + "; not a ProcessorCore");
		}
	}

}
