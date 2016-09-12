/**
 */
package fzi.mottem.model.codemodel.util;

import java.util.Map;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;

import fzi.mottem.model.baseelements.util.BaseelementsValidator;
import fzi.mottem.model.codemodel.BinaryLocation;
import fzi.mottem.model.codemodel.CClass;
import fzi.mottem.model.codemodel.CodeInstance;
import fzi.mottem.model.codemodel.CodemodelPackage;
import fzi.mottem.model.codemodel.DTClass;
import fzi.mottem.model.codemodel.DTEnum;
import fzi.mottem.model.codemodel.DTFloatingPoint;
import fzi.mottem.model.codemodel.DTInteger;
import fzi.mottem.model.codemodel.DTReference;
import fzi.mottem.model.codemodel.DTVoid;
import fzi.mottem.model.codemodel.DataType;
import fzi.mottem.model.codemodel.EnumValue;
import fzi.mottem.model.codemodel.Function;
import fzi.mottem.model.codemodel.SourceCodeLocation;
import fzi.mottem.model.codemodel.SourceFile;
import fzi.mottem.model.codemodel.Symbol;
import fzi.mottem.model.codemodel.Variable;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see fzi.mottem.model.codemodel.CodemodelPackage
 * @generated
 */
public class CodemodelValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final CodemodelValidator INSTANCE = new CodemodelValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "fzi.mottem.model.codemodel";

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
	public CodemodelValidator() {
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
	  return CodemodelPackage.eINSTANCE;
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
			case CodemodelPackage.CODE_INSTANCE:
				return validateCodeInstance((CodeInstance)value, diagnostics, context);
			case CodemodelPackage.FUNCTION:
				return validateFunction((Function)value, diagnostics, context);
			case CodemodelPackage.VARIABLE:
				return validateVariable((Variable)value, diagnostics, context);
			case CodemodelPackage.SYMBOL:
				return validateSymbol((Symbol)value, diagnostics, context);
			case CodemodelPackage.SOURCE_CODE_LOCATION:
				return validateSourceCodeLocation((SourceCodeLocation)value, diagnostics, context);
			case CodemodelPackage.BINARY_LOCATION:
				return validateBinaryLocation((BinaryLocation)value, diagnostics, context);
			case CodemodelPackage.SOURCE_FILE:
				return validateSourceFile((SourceFile)value, diagnostics, context);
			case CodemodelPackage.DATA_TYPE:
				return validateDataType((DataType)value, diagnostics, context);
			case CodemodelPackage.DT_INTEGER:
				return validateDTInteger((DTInteger)value, diagnostics, context);
			case CodemodelPackage.DT_FLOATING_POINT:
				return validateDTFloatingPoint((DTFloatingPoint)value, diagnostics, context);
			case CodemodelPackage.DT_REFERENCE:
				return validateDTReference((DTReference)value, diagnostics, context);
			case CodemodelPackage.DT_ENUM:
				return validateDTEnum((DTEnum)value, diagnostics, context);
			case CodemodelPackage.ENUM_VALUE:
				return validateEnumValue((EnumValue)value, diagnostics, context);
			case CodemodelPackage.DT_VOID:
				return validateDTVoid((DTVoid)value, diagnostics, context);
			case CodemodelPackage.DT_CLASS:
				return validateDTClass((DTClass)value, diagnostics, context);
			case CodemodelPackage.CCLASS:
				return validateCClass((CClass)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCodeInstance(CodeInstance codeInstance, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(codeInstance, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(codeInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(codeInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(codeInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(codeInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(codeInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(codeInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(codeInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(codeInstance, diagnostics, context);
		if (result || diagnostics != null) result &= validateCodeInstance_nameValid(codeInstance, diagnostics, context);
		return result;
	}

	/**
	 * Validates the nameValid constraint of '<em>Code Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateCodeInstance_nameValid(CodeInstance codeInstance, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return baseelementsValidator.validateINamed_nameValid(codeInstance, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFunction(Function function, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(function, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(function, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(function, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(function, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(function, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(function, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(function, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(function, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(function, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(function, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateVariable(Variable variable, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(variable, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(variable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(variable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(variable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(variable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(variable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(variable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(variable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(variable, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(variable, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSymbol(Symbol symbol, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(symbol, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(symbol, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(symbol, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(symbol, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(symbol, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(symbol, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(symbol, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(symbol, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(symbol, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(symbol, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSourceCodeLocation(SourceCodeLocation sourceCodeLocation, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(sourceCodeLocation, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBinaryLocation(BinaryLocation binaryLocation, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(binaryLocation, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSourceFile(SourceFile sourceFile, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(sourceFile, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDataType(DataType dataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(dataType, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(dataType, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(dataType, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(dataType, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(dataType, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(dataType, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(dataType, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(dataType, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(dataType, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(dataType, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDTInteger(DTInteger dtInteger, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(dtInteger, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(dtInteger, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(dtInteger, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(dtInteger, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(dtInteger, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(dtInteger, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(dtInteger, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(dtInteger, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(dtInteger, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(dtInteger, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDTFloatingPoint(DTFloatingPoint dtFloatingPoint, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(dtFloatingPoint, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(dtFloatingPoint, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(dtFloatingPoint, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(dtFloatingPoint, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(dtFloatingPoint, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(dtFloatingPoint, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(dtFloatingPoint, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(dtFloatingPoint, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(dtFloatingPoint, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(dtFloatingPoint, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDTReference(DTReference dtReference, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(dtReference, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(dtReference, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(dtReference, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(dtReference, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(dtReference, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(dtReference, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(dtReference, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(dtReference, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(dtReference, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(dtReference, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDTEnum(DTEnum dtEnum, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(dtEnum, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(dtEnum, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(dtEnum, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(dtEnum, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(dtEnum, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(dtEnum, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(dtEnum, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(dtEnum, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(dtEnum, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(dtEnum, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEnumValue(EnumValue enumValue, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(enumValue, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(enumValue, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(enumValue, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(enumValue, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(enumValue, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(enumValue, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(enumValue, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(enumValue, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(enumValue, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(enumValue, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDTVoid(DTVoid dtVoid, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(dtVoid, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(dtVoid, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(dtVoid, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(dtVoid, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(dtVoid, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(dtVoid, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(dtVoid, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(dtVoid, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(dtVoid, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(dtVoid, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDTClass(DTClass dtClass, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(dtClass, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(dtClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(dtClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(dtClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(dtClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(dtClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(dtClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(dtClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(dtClass, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(dtClass, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCClass(CClass cClass, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(cClass, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(cClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(cClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(cClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(cClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(cClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(cClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(cClass, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(cClass, diagnostics, context);
		if (result || diagnostics != null) result &= baseelementsValidator.validateINamed_nameValid(cClass, diagnostics, context);
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

} //CodemodelValidator
