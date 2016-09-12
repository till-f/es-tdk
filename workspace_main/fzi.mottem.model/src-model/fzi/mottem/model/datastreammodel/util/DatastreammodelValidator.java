/**
 */
package fzi.mottem.model.datastreammodel.util;

import java.util.Map;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;

import fzi.mottem.model.baseelements.util.BaseelementsValidator;
import fzi.mottem.model.datastreammodel.CANMessage;
import fzi.mottem.model.datastreammodel.Conversion;
import fzi.mottem.model.datastreammodel.DataStreamInstance;
import fzi.mottem.model.datastreammodel.DatastreammodelPackage;
import fzi.mottem.model.datastreammodel.EBaseType;
import fzi.mottem.model.datastreammodel.EByteOrder;
import fzi.mottem.model.datastreammodel.EDirection;
import fzi.mottem.model.datastreammodel.MessageSignal;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see fzi.mottem.model.datastreammodel.DatastreammodelPackage
 * @generated
 */
public class DatastreammodelValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final DatastreammodelValidator INSTANCE = new DatastreammodelValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "fzi.mottem.model.datastreammodel";

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
	public DatastreammodelValidator() {
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
	  return DatastreammodelPackage.eINSTANCE;
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
			case DatastreammodelPackage.DATA_STREAM_INSTANCE:
				return validateDataStreamInstance((DataStreamInstance)value, diagnostics, context);
			case DatastreammodelPackage.MESSAGE_SIGNAL:
				return validateMessageSignal((MessageSignal)value, diagnostics, context);
			case DatastreammodelPackage.CONVERSION:
				return validateConversion((Conversion)value, diagnostics, context);
			case DatastreammodelPackage.CAN_MESSAGE:
				return validateCANMessage((CANMessage)value, diagnostics, context);
			case DatastreammodelPackage.EBYTE_ORDER:
				return validateEByteOrder((EByteOrder)value, diagnostics, context);
			case DatastreammodelPackage.EDIRECTION:
				return validateEDirection((EDirection)value, diagnostics, context);
			case DatastreammodelPackage.EBASE_TYPE:
				return validateEBaseType((EBaseType)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDataStreamInstance(DataStreamInstance dataStreamInstance, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(dataStreamInstance, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(dataStreamInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(dataStreamInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(dataStreamInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(dataStreamInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(dataStreamInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(dataStreamInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(dataStreamInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(dataStreamInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validateDataStreamInstance_nameValid(dataStreamInstance, diagnostics, context);
		return result;
	}

	/**
	 * Validates the nameValid constraint of '<em>Data Stream Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateDataStreamInstance_nameValid(DataStreamInstance dataStreamInstance, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return baseelementsValidator.validateINamed_nameValid(dataStreamInstance, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMessageSignal(MessageSignal messageSignal, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(messageSignal, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(messageSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(messageSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(messageSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(messageSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(messageSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(messageSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(messageSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(messageSignal, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(messageSignal, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateConversion(Conversion conversion, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(conversion, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCANMessage(CANMessage canMessage, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(canMessage, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(canMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(canMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(canMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(canMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(canMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(canMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(canMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(canMessage, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(canMessage, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEByteOrder(EByteOrder eByteOrder, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEDirection(EDirection eDirection, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEBaseType(EBaseType eBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
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

} //DatastreammodelValidator
