/**
 */
package fzi.mottem.model.environmentdatamodel.util;

import java.util.Map;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;

import fzi.mottem.model.baseelements.util.BaseelementsValidator;
import fzi.mottem.model.environmentdatamodel.EnvironmentDataInstance;
import fzi.mottem.model.environmentdatamodel.EnvironmentSignal;
import fzi.mottem.model.environmentdatamodel.EnvironmentdatamodelPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see fzi.mottem.model.environmentdatamodel.EnvironmentdatamodelPackage
 * @generated
 */
public class EnvironmentdatamodelValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final EnvironmentdatamodelValidator INSTANCE = new EnvironmentdatamodelValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "fzi.mottem.model.environmentdatamodel";

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
	public EnvironmentdatamodelValidator() {
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
	  return EnvironmentdatamodelPackage.eINSTANCE;
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
			case EnvironmentdatamodelPackage.ENVIRONMENT_DATA_INSTANCE:
				return validateEnvironmentDataInstance((EnvironmentDataInstance)value, diagnostics, context);
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL:
				return validateEnvironmentSignal((EnvironmentSignal)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEnvironmentDataInstance(EnvironmentDataInstance environmentDataInstance, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(environmentDataInstance, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(environmentDataInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(environmentDataInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(environmentDataInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(environmentDataInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(environmentDataInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(environmentDataInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(environmentDataInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(environmentDataInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validateEnvironmentDataInstance_nameValid(environmentDataInstance, diagnostics, context);
		return result;
	}

	/**
	 * Validates the nameValid constraint of '<em>Environment Data Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateEnvironmentDataInstance_nameValid(EnvironmentDataInstance environmentDataInstance, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return baseelementsValidator.validateINamed_nameValid(environmentDataInstance, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEnvironmentSignal(EnvironmentSignal environmentSignal, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(environmentSignal, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(environmentSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(environmentSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(environmentSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(environmentSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(environmentSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(environmentSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(environmentSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(environmentSignal, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(environmentSignal, diagnostics, context);
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

} //EnvironmentdatamodelValidator
