/**
 */
package fzi.mottem.model.testrigmodel.util;

import java.util.Map;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;

import fzi.mottem.model.baseelements.util.BaseelementsValidator;
import fzi.mottem.model.testrigmodel.AgilentInspector;
import fzi.mottem.model.testrigmodel.AgilentInspectorContainer;
import fzi.mottem.model.testrigmodel.CANBus;
import fzi.mottem.model.testrigmodel.CANInspectorConnector;
import fzi.mottem.model.testrigmodel.CANInspectorPort;
import fzi.mottem.model.testrigmodel.CANPort;
import fzi.mottem.model.testrigmodel.CANPortConnector;
import fzi.mottem.model.testrigmodel.CDIInspectorPort;
import fzi.mottem.model.testrigmodel.CodeConnector;
import fzi.mottem.model.testrigmodel.CodeInspectorContainer;
import fzi.mottem.model.testrigmodel.Environment;
import fzi.mottem.model.testrigmodel.Ethernet;
import fzi.mottem.model.testrigmodel.EthernetInspectorConnector;
import fzi.mottem.model.testrigmodel.EthernetPort;
import fzi.mottem.model.testrigmodel.EthernetPortConnector;
import fzi.mottem.model.testrigmodel.HostInspectorContainer;
import fzi.mottem.model.testrigmodel.IC5000;
import fzi.mottem.model.testrigmodel.IC5000Port;
import fzi.mottem.model.testrigmodel.IOPin;
import fzi.mottem.model.testrigmodel.IOPort;
import fzi.mottem.model.testrigmodel.IOne;
import fzi.mottem.model.testrigmodel.IOnePort;
import fzi.mottem.model.testrigmodel.ISystemInspectorContainer;
import fzi.mottem.model.testrigmodel.InspectorContainer;
import fzi.mottem.model.testrigmodel.JTAGInspectorConnector;
import fzi.mottem.model.testrigmodel.PinConnector;
import fzi.mottem.model.testrigmodel.PinInspectorContainer;
import fzi.mottem.model.testrigmodel.PinSignal;
import fzi.mottem.model.testrigmodel.Processor;
import fzi.mottem.model.testrigmodel.ProcessorCore;
import fzi.mottem.model.testrigmodel.SoftwareExecutor;
import fzi.mottem.model.testrigmodel.StreamConnector;
import fzi.mottem.model.testrigmodel.StreamInspectorContainer;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelPackage;
import fzi.mottem.model.testrigmodel.TraceInspectorConnector;
import fzi.mottem.model.testrigmodel.UUT;
import fzi.mottem.model.testrigmodel.VN7600;
import fzi.mottem.model.testrigmodel.VectorInspectorContainer;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see fzi.mottem.model.testrigmodel.TestrigmodelPackage
 * @generated
 */
public class TestrigmodelValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final TestrigmodelValidator INSTANCE = new TestrigmodelValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "fzi.mottem.model.testrigmodel";

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * The cached base package validator.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseelementsValidator baseelementsValidator;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestrigmodelValidator() {
		super();
		baseelementsValidator = BaseelementsValidator.INSTANCE;
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
	  return TestrigmodelPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
		switch (classifierID) {
			case TestrigmodelPackage.TEST_RIG_INSTANCE:
				return validateTestRigInstance((TestRigInstance)value, diagnostics, context);
			case TestrigmodelPackage.UUT:
				return validateUUT((UUT)value, diagnostics, context);
			case TestrigmodelPackage.SOFTWARE_EXECUTOR:
				return validateSoftwareExecutor((SoftwareExecutor)value, diagnostics, context);
			case TestrigmodelPackage.THREAD:
				return validateThread((fzi.mottem.model.testrigmodel.Thread)value, diagnostics, context);
			case TestrigmodelPackage.PROCESSOR_CORE:
				return validateProcessorCore((ProcessorCore)value, diagnostics, context);
			case TestrigmodelPackage.IO_PORT:
				return validateIOPort((IOPort)value, diagnostics, context);
			case TestrigmodelPackage.IO_PIN:
				return validateIOPin((IOPin)value, diagnostics, context);
			case TestrigmodelPackage.INSPECTOR_CONTAINER:
				return validateInspectorContainer((InspectorContainer)value, diagnostics, context);
			case TestrigmodelPackage.CODE_INSPECTOR_CONTAINER:
				return validateCodeInspectorContainer((CodeInspectorContainer)value, diagnostics, context);
			case TestrigmodelPackage.STREAM_INSPECTOR_CONTAINER:
				return validateStreamInspectorContainer((StreamInspectorContainer)value, diagnostics, context);
			case TestrigmodelPackage.PIN_INSPECTOR_CONTAINER:
				return validatePinInspectorContainer((PinInspectorContainer)value, diagnostics, context);
			case TestrigmodelPackage.ISYSTEM_INSPECTOR_CONTAINER:
				return validateISystemInspectorContainer((ISystemInspectorContainer)value, diagnostics, context);
			case TestrigmodelPackage.IC5000:
				return validateIC5000((IC5000)value, diagnostics, context);
			case TestrigmodelPackage.IONE:
				return validateIOne((IOne)value, diagnostics, context);
			case TestrigmodelPackage.CAN_INSPECTOR_CONNECTOR:
				return validateCANInspectorConnector((CANInspectorConnector)value, diagnostics, context);
			case TestrigmodelPackage.ETHERNET_INSPECTOR_CONNECTOR:
				return validateEthernetInspectorConnector((EthernetInspectorConnector)value, diagnostics, context);
			case TestrigmodelPackage.JTAG_INSPECTOR_CONNECTOR:
				return validateJTAGInspectorConnector((JTAGInspectorConnector)value, diagnostics, context);
			case TestrigmodelPackage.TRACE_INSPECTOR_CONNECTOR:
				return validateTraceInspectorConnector((TraceInspectorConnector)value, diagnostics, context);
			case TestrigmodelPackage.CODE_CONNECTOR:
				return validateCodeConnector((CodeConnector)value, diagnostics, context);
			case TestrigmodelPackage.STREAM_CONNECTOR:
				return validateStreamConnector((StreamConnector)value, diagnostics, context);
			case TestrigmodelPackage.VECTOR_INSPECTOR_CONTAINER:
				return validateVectorInspectorContainer((VectorInspectorContainer)value, diagnostics, context);
			case TestrigmodelPackage.VN7600:
				return validateVN7600((VN7600)value, diagnostics, context);
			case TestrigmodelPackage.CAN_BUS:
				return validateCANBus((CANBus)value, diagnostics, context);
			case TestrigmodelPackage.ETHERNET:
				return validateEthernet((Ethernet)value, diagnostics, context);
			case TestrigmodelPackage.CAN_PORT:
				return validateCANPort((CANPort)value, diagnostics, context);
			case TestrigmodelPackage.ETHERNET_PORT:
				return validateEthernetPort((EthernetPort)value, diagnostics, context);
			case TestrigmodelPackage.CAN_INSPECTOR_PORT:
				return validateCANInspectorPort((CANInspectorPort)value, diagnostics, context);
			case TestrigmodelPackage.IC5000_PORT:
				return validateIC5000Port((IC5000Port)value, diagnostics, context);
			case TestrigmodelPackage.IONE_PORT:
				return validateIOnePort((IOnePort)value, diagnostics, context);
			case TestrigmodelPackage.CAN_PORT_CONNECTOR:
				return validateCANPortConnector((CANPortConnector)value, diagnostics, context);
			case TestrigmodelPackage.ETHERNET_PORT_CONNECTOR:
				return validateEthernetPortConnector((EthernetPortConnector)value, diagnostics, context);
			case TestrigmodelPackage.HOST_INSPECTOR_CONTAINER:
				return validateHostInspectorContainer((HostInspectorContainer)value, diagnostics, context);
			case TestrigmodelPackage.CDI_INSPECTOR_PORT:
				return validateCDIInspectorPort((CDIInspectorPort)value, diagnostics, context);
			case TestrigmodelPackage.PROCESSOR:
				return validateProcessor((Processor)value, diagnostics, context);
			case TestrigmodelPackage.AGILENT_INSPECTOR_CONTAINER:
				return validateAgilentInspectorContainer((AgilentInspectorContainer)value, diagnostics, context);
			case TestrigmodelPackage.AGILENT_INSPECTOR:
				return validateAgilentInspector((AgilentInspector)value, diagnostics, context);
			case TestrigmodelPackage.PIN_CONNECTOR:
				return validatePinConnector((PinConnector)value, diagnostics, context);
			case TestrigmodelPackage.PIN_SIGNAL:
				return validatePinSignal((PinSignal)value, diagnostics, context);
			case TestrigmodelPackage.ENVIRONMENT:
				return validateEnvironment((Environment)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTestRigInstance(TestRigInstance testRigInstance, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(testRigInstance, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(testRigInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(testRigInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(testRigInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(testRigInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(testRigInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(testRigInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(testRigInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(testRigInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validateTestRigInstance_nameValid(testRigInstance, diagnostics, context);
		return result;
	}

	/**
	 * Validates the nameValid constraint of '<em>Test Rig Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateTestRigInstance_nameValid(TestRigInstance testRigInstance, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return baseelementsValidator.validateINamed_nameValid(testRigInstance, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateUUT(UUT uut, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(uut, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(uut, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(uut, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(uut, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(uut, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(uut, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(uut, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(uut, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(uut, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(uut, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSoftwareExecutor(SoftwareExecutor softwareExecutor, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(softwareExecutor, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(softwareExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(softwareExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(softwareExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(softwareExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(softwareExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(softwareExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(softwareExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(softwareExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(softwareExecutor, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateThread(fzi.mottem.model.testrigmodel.Thread thread, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(thread, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(thread, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(thread, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(thread, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(thread, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(thread, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(thread, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(thread, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(thread, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(thread, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateProcessorCore(ProcessorCore processorCore, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(processorCore, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(processorCore, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(processorCore, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(processorCore, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(processorCore, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(processorCore, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(processorCore, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(processorCore, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(processorCore, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(processorCore, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIOPort(IOPort ioPort, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(ioPort, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(ioPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(ioPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(ioPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(ioPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(ioPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(ioPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(ioPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(ioPort, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(ioPort, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIOPin(IOPin ioPin, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(ioPin, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(ioPin, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(ioPin, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(ioPin, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(ioPin, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(ioPin, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(ioPin, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(ioPin, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(ioPin, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(ioPin, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateInspectorContainer(InspectorContainer inspectorContainer, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(inspectorContainer, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(inspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(inspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(inspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(inspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(inspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(inspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(inspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(inspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(inspectorContainer, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCodeInspectorContainer(CodeInspectorContainer codeInspectorContainer, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(codeInspectorContainer, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(codeInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(codeInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(codeInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(codeInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(codeInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(codeInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(codeInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(codeInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(codeInspectorContainer, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateStreamInspectorContainer(StreamInspectorContainer streamInspectorContainer, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(streamInspectorContainer, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(streamInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(streamInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(streamInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(streamInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(streamInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(streamInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(streamInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(streamInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(streamInspectorContainer, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePinInspectorContainer(PinInspectorContainer pinInspectorContainer, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(pinInspectorContainer, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(pinInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(pinInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(pinInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(pinInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(pinInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(pinInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(pinInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(pinInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(pinInspectorContainer, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateISystemInspectorContainer(ISystemInspectorContainer iSystemInspectorContainer, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iSystemInspectorContainer, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iSystemInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iSystemInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iSystemInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iSystemInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iSystemInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iSystemInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iSystemInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iSystemInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(iSystemInspectorContainer, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIC5000(IC5000 ic5000, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(ic5000, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(ic5000, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(ic5000, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(ic5000, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(ic5000, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(ic5000, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(ic5000, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(ic5000, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(ic5000, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(ic5000, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIOne(IOne iOne, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iOne, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iOne, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iOne, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iOne, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iOne, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iOne, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iOne, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iOne, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iOne, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(iOne, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCANInspectorConnector(CANInspectorConnector canInspectorConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(canInspectorConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEthernetInspectorConnector(EthernetInspectorConnector ethernetInspectorConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(ethernetInspectorConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateJTAGInspectorConnector(JTAGInspectorConnector jtagInspectorConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(jtagInspectorConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTraceInspectorConnector(TraceInspectorConnector traceInspectorConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(traceInspectorConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCodeConnector(CodeConnector codeConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(codeConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateStreamConnector(StreamConnector streamConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(streamConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateVectorInspectorContainer(VectorInspectorContainer vectorInspectorContainer, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(vectorInspectorContainer, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(vectorInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(vectorInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(vectorInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(vectorInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(vectorInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(vectorInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(vectorInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(vectorInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(vectorInspectorContainer, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateVN7600(VN7600 vn7600, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(vn7600, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(vn7600, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(vn7600, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(vn7600, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(vn7600, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(vn7600, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(vn7600, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(vn7600, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(vn7600, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(vn7600, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCANBus(CANBus canBus, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(canBus, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(canBus, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(canBus, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(canBus, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(canBus, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(canBus, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(canBus, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(canBus, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(canBus, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(canBus, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEthernet(Ethernet ethernet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(ethernet, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(ethernet, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(ethernet, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(ethernet, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(ethernet, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(ethernet, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(ethernet, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(ethernet, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(ethernet, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(ethernet, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCANPort(CANPort canPort, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(canPort, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(canPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(canPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(canPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(canPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(canPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(canPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(canPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(canPort, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(canPort, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEthernetPort(EthernetPort ethernetPort, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(ethernetPort, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(ethernetPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(ethernetPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(ethernetPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(ethernetPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(ethernetPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(ethernetPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(ethernetPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(ethernetPort, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(ethernetPort, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCANInspectorPort(CANInspectorPort canInspectorPort, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(canInspectorPort, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(canInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(canInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(canInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(canInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(canInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(canInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(canInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(canInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(canInspectorPort, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIC5000Port(IC5000Port ic5000Port, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(ic5000Port, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(ic5000Port, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(ic5000Port, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(ic5000Port, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(ic5000Port, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(ic5000Port, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(ic5000Port, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(ic5000Port, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(ic5000Port, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(ic5000Port, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIOnePort(IOnePort iOnePort, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iOnePort, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iOnePort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iOnePort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iOnePort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iOnePort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iOnePort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iOnePort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iOnePort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iOnePort, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(iOnePort, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCANPortConnector(CANPortConnector canPortConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(canPortConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEthernetPortConnector(EthernetPortConnector ethernetPortConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(ethernetPortConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateHostInspectorContainer(HostInspectorContainer hostInspectorContainer, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(hostInspectorContainer, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(hostInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(hostInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(hostInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(hostInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(hostInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(hostInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(hostInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(hostInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(hostInspectorContainer, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCDIInspectorPort(CDIInspectorPort cdiInspectorPort, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(cdiInspectorPort, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(cdiInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(cdiInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(cdiInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(cdiInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(cdiInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(cdiInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(cdiInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(cdiInspectorPort, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(cdiInspectorPort, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateProcessor(Processor processor, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(processor, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(processor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(processor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(processor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(processor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(processor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(processor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(processor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(processor, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(processor, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAgilentInspectorContainer(AgilentInspectorContainer agilentInspectorContainer, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(agilentInspectorContainer, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(agilentInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(agilentInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(agilentInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(agilentInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(agilentInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(agilentInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(agilentInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(agilentInspectorContainer, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(agilentInspectorContainer, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAgilentInspector(AgilentInspector agilentInspector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(agilentInspector, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(agilentInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(agilentInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(agilentInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(agilentInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(agilentInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(agilentInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(agilentInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(agilentInspector, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(agilentInspector, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePinConnector(PinConnector pinConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(pinConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePinSignal(PinSignal pinSignal, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(pinSignal, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(pinSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(pinSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(pinSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(pinSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(pinSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(pinSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(pinSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(pinSignal, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(pinSignal, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEnvironment(Environment environment, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(environment, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(environment, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(environment, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(environment, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(environment, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(environment, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(environment, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(environment, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(environment, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(environment, diagnostics, context);
		return result;
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		// TODO
		// Specialize this to return a resource locator for messages specific to this validator.
		// Ensure that you remove @generated or mark it @generated NOT
		return super.getResourceLocator();
	}

} //TestrigmodelValidator
