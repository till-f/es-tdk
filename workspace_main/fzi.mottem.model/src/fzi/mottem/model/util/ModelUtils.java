package fzi.mottem.model.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.LinkedList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IInspectable;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.INamed;
import fzi.mottem.model.baseelements.INetwork;
import fzi.mottem.model.baseelements.INetworkConnector;
import fzi.mottem.model.baseelements.INetworkPort;
import fzi.mottem.model.baseelements.ISymbolContainer;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.codemodel.BinaryLocation;
import fzi.mottem.model.codemodel.CodeInstance;
import fzi.mottem.model.codemodel.CodemodelFactory;
import fzi.mottem.model.codemodel.DTFloatingPoint;
import fzi.mottem.model.codemodel.DTInteger;
import fzi.mottem.model.codemodel.DTVoid;
import fzi.mottem.model.codemodel.DataType;
import fzi.mottem.model.codemodel.SourceCodeLocation;
import fzi.mottem.model.codemodel.SourceFile;
import fzi.mottem.model.codemodel.Symbol;
import fzi.mottem.model.datastreammodel.Conversion;
import fzi.mottem.model.datastreammodel.EBaseType;
import fzi.mottem.model.datastreammodel.EByteOrder;
import fzi.mottem.model.datastreammodel.MessageSignal;
import fzi.mottem.model.testrigmodel.IOPort;
import fzi.mottem.model.testrigmodel.Processor;
import fzi.mottem.model.testrigmodel.ProcessorCore;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.Thread;
import fzi.util.BitMagic;
import fzi.util.ecore.EcoreUtils;

public class ModelUtils
{
	
	public static final String PTS_SOURCE_FILES_ROOT = "pts";
	public static final String PTS_MODEL_FILES_ROOT = "model";

	public static final String FILE_EXTENSION_TESTRIG_MODEL = "etm-testrig";
	public static final String FILE_EXTENSION_CODE_MODEL = "etm-code";
	public static final String FILE_EXTENSION_ND_MODEL = "etm-dstream";
	public static final String FILE_EXTENSION_ED_MODEL = "etm-env";
	
	public static IInspector getInspector(IExecutor exec)
	{
		if (exec instanceof IInspectable)
		{
			return getInspector((IInspectable) exec);
		}
		else if (exec instanceof INetworkPort)
		{
			return getInspector((INetworkPort) exec);
		}
		else if (exec instanceof ProcessorCore)
		{
			return getInspector((ProcessorCore) exec);
		}
		else if (exec instanceof Thread)
		{
			throw new RuntimeException("Cannot get Inspector: 'Threads' are not yet supported");
		}
		else
		{
			throw new RuntimeException("Cannot get Inspector: Unexpected IExecutor type");
		}
	}

	public static IInspector getInspector(IInspectable inspectable)
	{
		if (inspectable.getInspectorConnector() != null)
		{
			return inspectable.getInspectorConnector().getInspector();
		}
		
		return null;
	}

	public static IInspector getInspector(INetworkPort netPort)
	{
		if (netPort.getNetworkConnector() != null)
		{
			INetwork network = netPort.getNetworkConnector().getNetwork();
			
			if (!(network instanceof IInspectable))
				throw new RuntimeException("Cannot get Inspector: unexpected INetwork type");
			
			return getInspector((IInspectable)network);
		}
		
		return null;
	}

	public static IInspector getInspector(ProcessorCore core)
	{
		Processor processor = core.getProcessor();
		
		if (!(processor instanceof IInspectable))
			throw new RuntimeException("Cannot get Inspector: unexpected INetwork type");
		
		return getInspector((IInspectable)processor);
	}

	public static IInspectable getInspectable(IInspector inspector)
	{
		if (inspector.getInspectorConnector() == null)
			return null;
		
		return inspector.getInspectorConnector().getInspectable();
	}
	
	public static int getProcessorCoreIndex(ProcessorCore processorCore)
	{
		return processorCore.getProcessor().getProcessorCores().indexOf(processorCore);
	}

	public static String generateCustomUUID(EObject eObject)
	{
		try
		{
			return generateCustomUUIDThrowing(eObject);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static int uid_idx = 0;
	private static String generateCustomUUIDThrowing(EObject eObject) throws IOException
	{
		StringBuilder uidBuilder = new StringBuilder();
        
		int currentDepth = 0;
		int targetDepth = 0;
		
		while(true)
		{
			String idPart = EcoreUtils.getXmiId(eObject);
			
			// calculate new UID part, if ID is not set, otherwise re-use already calculated ID
			if (idPart == null)
			{
		        if (eObject instanceof INamed &&
					((INamed)eObject).getName() != null &&
					!((INamed)eObject).getName().isEmpty() &&
					!EcoreUtils.hasContainerInstanceOf(eObject, TestRigInstance.class))
				{
		        	/*
		        	 * if the eObject is of INamed the name is sufficient if the
		        	 *   a) name is set,
		        	 *   b) name not empty,
		        	 *   c) eObject is not contained in a TestRigInstance
		        	 *      (as names are not ensured to be unique there)
		        	 */
					idPart = "+N" + ((INamed)eObject).getName();
					targetDepth = 2;
				}
				else if (eObject instanceof SourceFile)
				{
					idPart = "+LF" + ((SourceFile) eObject).getFilePath();
					targetDepth = 1;
				}
				else if (eObject instanceof SourceCodeLocation)
				{
					idPart = "+LS";
					targetDepth = 2;
				}
				else if (eObject instanceof BinaryLocation)
				{
					idPart = "+LB";
					targetDepth = 2;
				}
				else
				{
					ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
				    buffer.putLong(new Date().getTime() << 32 | uid_idx++);
					idPart = "+R" + buffer.array();
					targetDepth = 1;
				}
			}
			
			uidBuilder.append(idPart);
			currentDepth++;
			
			if (targetDepth > 0 && currentDepth >= targetDepth)
				break;

			if (eObject.eContainer() == null || eObject == eObject.eContainer())
				break;
			
			eObject = eObject.eContainer();
		}
		
		return uidBuilder.toString();
	}
	
	public static boolean containsElementWithName(EList<Symbol> elements, String name)
	{
		for (INamed element : elements)
		{
			if (element.getName().equals(name))
				return true;
		}
		return false;
	}

	public static double rawByteArrayToDouble(byte[] data, MessageSignal signal)
	{
		Conversion conv = signal.getConversion();
		boolean swapByteOrder = conv.getByteOrder() == EByteOrder.MSF;
		if (swapByteOrder && (signal.getBitOffset() % 8 != 0 || signal.getBitLength() % 8 != 0))
			throw new InvalidParameterException("Signals that are not byte-aligned can only have byte order 'LSF'");
			
		data = BitMagic.getBits(data,
				signal.getBitOffset(),
				signal.getBitLength());
		
		return BitMagic.getValueDouble(data,
				conv.getBaseType() == EBaseType.INTEGER ? BitMagic.EIntegralDataType.Integer : BitMagic.EIntegralDataType.Float,
				!swapByteOrder,		// !TODO: really strange... for raw2double we need inverted byte order?!
				conv.getOffset(),
				conv.getFactor());
	}

	public static byte[] doubleToRawByteArray(double value, MessageSignal signal)
	{
		Conversion conv = signal.getConversion();
		boolean swapByteOrder = conv.getByteOrder() == EByteOrder.MSF;
		if (swapByteOrder && (signal.getBitOffset() % 8 != 0 || signal.getBitLength() % 8 != 0))
			throw new InvalidParameterException("Signals that are not byte-aligned can only have byte order 'LSF'");

		return BitMagic.getValueRAW(value,
				conv.getBaseType() == EBaseType.INTEGER ? BitMagic.EIntegralDataType.Integer : BitMagic.EIntegralDataType.Float,
				swapByteOrder,
				conv.getOffset(),
				conv.getFactor(),
				BitMagic.getByteCount(signal.getBitLength()));
	}

	public static LinkedList<ITestReferenceable> getAvailableTRefs(IInspector inspector)
	{
		if (inspector.getInspectorConnector() == null || inspector.getInspectorConnector().getInspectable() == null)
			return null;
		IInspectable inspectedElement = inspector.getInspectorConnector().getInspectable();
		
		if (inspectedElement == null)
		{
			return null;
		}

		LinkedList<ISymbolContainer> containers = new LinkedList<ISymbolContainer>();
		
		if (inspectedElement instanceof ISymbolContainer)
		{
			containers.add((ISymbolContainer)inspectedElement);
		}
		else if (inspectedElement instanceof IExecutor)
		{
			containers.add(((IExecutor) inspectedElement).getSymbolContainer());
		}
		else if (inspectedElement instanceof Processor)
		{
			for (IExecutor exec : ((Processor) inspectedElement).getProcessorCores())
			{
				containers.add(exec.getSymbolContainer());
			}
		}
		else if (inspectedElement instanceof INetwork)
		{
			EList<INetworkConnector> connectors = ((INetwork) inspectedElement).getNetworkConnector();
			if (connectors == null)
				return null;
			for (INetworkConnector connector : connectors)
			{
				if (connector.getNetworkPort() instanceof IOPort)
				{
					containers.add(((IOPort)connector.getNetworkPort()).getSymbolContainer());
				}
				else
				{
					throw new RuntimeException("Unexpected networkPort feature");
				}
			}
		}
		else
		{
			throw new RuntimeException("Unexpected IInspectable: " + inspectedElement.getClass().getSimpleName());
		}
		
		LinkedList<ITestReferenceable> tRefs = new LinkedList<ITestReferenceable>();
		for(ISymbolContainer container : containers)
		{
			TreeIterator<EObject> contents = container.eAllContents();
			while (contents.hasNext())
			{
				EObject ele = contents.next();
				if (ele instanceof ITestReferenceable)
					tRefs.add((ITestReferenceable)ele);
			}

			
		}
		
		return tRefs;
	}

	public static LinkedList<CodeInstance> getAllCodeModelsInWorkspace()
	{
		LinkedList<CodeInstance> codeInstances = new LinkedList<CodeInstance>();
		
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
		{
			if (project.isOpen())
			{
				IFolder modelFolder = project.getFolder(PTS_MODEL_FILES_ROOT);
				
				if (modelFolder == null || !modelFolder.exists())
					continue;
				
				try 
				{
					for (IResource resource : modelFolder.members())
					{
						if (FILE_EXTENSION_CODE_MODEL.equals(resource.getFileExtension()))
						{
							try
							{
								CodeInstance ci = (CodeInstance)EcoreUtils.loadFullEMFModel(URI.createPlatformResourceURI(resource.getFullPath().toString(), true));
								codeInstances.add(ci);
							}
							catch (IOException e) 
							{
								e.printStackTrace();
								continue;
							}
						}
					}
				}
				catch (CoreException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return codeInstances;
	}

	public static Symbol getGlobalSymbol(CodeInstance ci, String name)
	{
		if (name == null)
			return null;
		
		for (Symbol s : ci.getSymbols())
		{
			if (name.equals(s.getName()))
				return s;
		}
		
		return null;
	}

	public static DataType findDataTypeForName(CodeInstance ci, String name)
	{
		for(DataType dt : ci.getDataTypes())
		{
			if (dt.getName() == null)
			{
				continue;
			}
			
			if (dt.getName().equals(name))
			{
				return dt;
			}	
		}
		return null;
	}

	public static DataType findDataTypeForNameOrCreateDefault(CodeInstance ci, String typeName)
	{
		DataType dt = findDataTypeForName(ci, typeName);
		
		if (dt != null)
		{
			return dt;
		}
		else
		{
			if (typeName.contains("int"))
			{
				DTInteger di = CodemodelFactory.eINSTANCE.createDTInteger();
				di.setName(typeName);
				di.setIsSigned(!typeName.startsWith("u"));
				int startOfN = typeName.lastIndexOf("int") + 3;
				int bitSize = Integer.parseInt(typeName.substring(startOfN));
				di.setBitSize(bitSize);
				dt = di;
			}
			else if (typeName.contains("float"))
			{
				DTFloatingPoint dfp = CodemodelFactory.eINSTANCE.createDTFloatingPoint();
				dfp.setName(typeName);
				dfp.setExponentBitSize(8);
				dfp.setSignificandBitSize(23);
				dt = dfp;
			}
			else if (typeName.contains("double"))
			{
				DTFloatingPoint dfp = CodemodelFactory.eINSTANCE.createDTFloatingPoint();
				dfp.setName(typeName);
				dfp.setExponentBitSize(11);
				dfp.setSignificandBitSize(52);
				dt = dfp;
			}
			else if (typeName.contains("void"))
			{
				DTVoid dv = CodemodelFactory.eINSTANCE.createDTVoid();
				dv.setName("void");
				dt = dv;
			}
			else
			{
				return null;
			}
			
			ci.getDataTypes().add(dt);
			return dt;
		}
	}
}
