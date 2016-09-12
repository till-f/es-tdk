/**
 */
package fzi.mottem.model.baseelements.util;

import fzi.mottem.model.baseelements.*;

import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see fzi.mottem.model.baseelements.BaseelementsPackage
 * @generated
 */
public class BaseelementsValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final BaseelementsValidator INSTANCE = new BaseelementsValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "fzi.mottem.model.baseelements";

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
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseelementsValidator() {
		super();
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
	  return BaseelementsPackage.eINSTANCE;
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
			case BaseelementsPackage.INAMED:
				return validateINamed((INamed)value, diagnostics, context);
			case BaseelementsPackage.IINDEXED:
				return validateIIndexed((IIndexed)value, diagnostics, context);
			case BaseelementsPackage.ICOMMENTABLE:
				return validateICommentable((ICommentable)value, diagnostics, context);
			case BaseelementsPackage.IEXECUTOR:
				return validateIExecutor((IExecutor)value, diagnostics, context);
			case BaseelementsPackage.IINSPECTABLE:
				return validateIInspectable((IInspectable)value, diagnostics, context);
			case BaseelementsPackage.ITEST_REFERENCEABLE:
				return validateITestReferenceable((ITestReferenceable)value, diagnostics, context);
			case BaseelementsPackage.ITEST_WRITEABLE:
				return validateITestWriteable((ITestWriteable)value, diagnostics, context);
			case BaseelementsPackage.ITEST_READABLE:
				return validateITestReadable((ITestReadable)value, diagnostics, context);
			case BaseelementsPackage.ITEST_CALLABLE:
				return validateITestCallable((ITestCallable)value, diagnostics, context);
			case BaseelementsPackage.IREFERENCEABLE_CONTAINER:
				return validateIReferenceableContainer((IReferenceableContainer)value, diagnostics, context);
			case BaseelementsPackage.IINSPECTOR:
				return validateIInspector((IInspector)value, diagnostics, context);
			case BaseelementsPackage.IINSPECTOR_CONNECTOR:
				return validateIInspectorConnector((IInspectorConnector)value, diagnostics, context);
			case BaseelementsPackage.INETWORK:
				return validateINetwork((INetwork)value, diagnostics, context);
			case BaseelementsPackage.INETWORK_CONNECTOR:
				return validateINetworkConnector((INetworkConnector)value, diagnostics, context);
			case BaseelementsPackage.INETWORK_PORT:
				return validateINetworkPort((INetworkPort)value, diagnostics, context);
			case BaseelementsPackage.ISYMBOL_CONTAINER:
				return validateISymbolContainer((ISymbolContainer)value, diagnostics, context);
			case BaseelementsPackage.IWRAPPED_REFERENCEABLE:
				return validateIWrappedReferenceable((IWrappedReferenceable)value, diagnostics, context);
			case BaseelementsPackage.TR_WRAPPER_RW:
				return validateTRWrapperRW((TRWrapperRW)value, diagnostics, context);
			case BaseelementsPackage.TR_WRAPPER_CALLABLE:
				return validateTRWrapperCallable((TRWrapperCallable)value, diagnostics, context);
			case BaseelementsPackage.ISIGNAL:
				return validateISignal((ISignal)value, diagnostics, context);
			case BaseelementsPackage.IMESSAGE:
				return validateIMessage((IMessage)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateINamed(INamed iNamed, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iNamed, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iNamed, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iNamed, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iNamed, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iNamed, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iNamed, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iNamed, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iNamed, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iNamed, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iNamed, diagnostics, context);
		return result;
	}

	/**
	 * Validates the nameValid constraint of '<em>INamed</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateINamed_nameValid(INamed iNamed, DiagnosticChain diagnostics, Map<Object, Object> context) 
	{
		if (iNamed == null)
		{
			return false;
		}
		
		if (iNamed.getName() == null || iNamed.getName().isEmpty() || !Pattern.matches("([a-zA-Z0-9_])+", iNamed.getName()))
		{
			if (diagnostics != null)
			{
				diagnostics.add(createDiagnostic(Diagnostic.ERROR,
						 DIAGNOSTIC_SOURCE,
						 0,
						 "_UI_GenericConstraint_diagnostic",
						 new Object[] { "Name is not valid", getObjectLabel(iNamed, context) },
						 new Object[] { iNamed },
						 context));
			}
			return false;
		}
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIIndexed(IIndexed iIndexed, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(iIndexed, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateICommentable(ICommentable iCommentable, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(iCommentable, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIExecutor(IExecutor iExecutor, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iExecutor, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iExecutor, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iExecutor, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIInspectable(IInspectable iInspectable, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(iInspectable, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateITestReferenceable(ITestReferenceable iTestReferenceable, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iTestReferenceable, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iTestReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iTestReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iTestReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iTestReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iTestReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iTestReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iTestReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iTestReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iTestReferenceable, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateITestWriteable(ITestWriteable iTestWriteable, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iTestWriteable, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iTestWriteable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iTestWriteable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iTestWriteable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iTestWriteable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iTestWriteable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iTestWriteable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iTestWriteable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iTestWriteable, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iTestWriteable, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateITestReadable(ITestReadable iTestReadable, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iTestReadable, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iTestReadable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iTestReadable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iTestReadable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iTestReadable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iTestReadable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iTestReadable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iTestReadable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iTestReadable, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iTestReadable, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateITestCallable(ITestCallable iTestCallable, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iTestCallable, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iTestCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iTestCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iTestCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iTestCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iTestCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iTestCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iTestCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iTestCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iTestCallable, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIReferenceableContainer(IReferenceableContainer iReferenceableContainer, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iReferenceableContainer, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iReferenceableContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iReferenceableContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iReferenceableContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iReferenceableContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iReferenceableContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iReferenceableContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iReferenceableContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iReferenceableContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iReferenceableContainer, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIInspector(IInspector iInspector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iInspector, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iInspector, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iInspector, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIInspectorConnector(IInspectorConnector iInspectorConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(iInspectorConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateINetwork(INetwork iNetwork, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iNetwork, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iNetwork, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iNetwork, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iNetwork, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iNetwork, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iNetwork, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iNetwork, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iNetwork, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iNetwork, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iNetwork, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateINetworkConnector(INetworkConnector iNetworkConnector, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(iNetworkConnector, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateINetworkPort(INetworkPort iNetworkPort, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iNetworkPort, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iNetworkPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iNetworkPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iNetworkPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iNetworkPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iNetworkPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iNetworkPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iNetworkPort, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iNetworkPort, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iNetworkPort, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateISymbolContainer(ISymbolContainer iSymbolContainer, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iSymbolContainer, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iSymbolContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iSymbolContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iSymbolContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iSymbolContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iSymbolContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iSymbolContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iSymbolContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iSymbolContainer, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iSymbolContainer, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIWrappedReferenceable(IWrappedReferenceable iWrappedReferenceable, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iWrappedReferenceable, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iWrappedReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iWrappedReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iWrappedReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iWrappedReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iWrappedReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iWrappedReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iWrappedReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iWrappedReferenceable, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iWrappedReferenceable, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTRWrapperRW(TRWrapperRW trWrapperRW, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(trWrapperRW, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(trWrapperRW, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(trWrapperRW, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(trWrapperRW, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(trWrapperRW, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(trWrapperRW, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(trWrapperRW, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(trWrapperRW, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(trWrapperRW, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(trWrapperRW, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTRWrapperCallable(TRWrapperCallable trWrapperCallable, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(trWrapperCallable, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(trWrapperCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(trWrapperCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(trWrapperCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(trWrapperCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(trWrapperCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(trWrapperCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(trWrapperCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(trWrapperCallable, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(trWrapperCallable, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateISignal(ISignal iSignal, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iSignal, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iSignal, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iSignal, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIMessage(IMessage iMessage, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(iMessage, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(iMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(iMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(iMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(iMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(iMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(iMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(iMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(iMessage, diagnostics, context);
		if (result || diagnostics != null) result &= validateINamed_nameValid(iMessage, diagnostics, context);
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

} //BaseelementsValidator
