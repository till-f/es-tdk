/**
 */
package fzi.mottem.model.codemodel.impl;

import fzi.mottem.model.codemodel.CodemodelPackage;
import fzi.mottem.model.codemodel.Function;
import fzi.mottem.model.codemodel.Variable;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link fzi.mottem.model.codemodel.impl.VariableImpl#getFunction <em>Function</em>}</li>
 *   <li>{@link fzi.mottem.model.codemodel.impl.VariableImpl#isFunctionParameter <em>Function Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VariableImpl extends SymbolImpl implements Variable {
	/**
	 * The default value of the '{@link #isFunctionParameter() <em>Function Parameter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFunctionParameter()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FUNCTION_PARAMETER_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFunctionParameter() <em>Function Parameter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFunctionParameter()
	 * @generated
	 * @ordered
	 */
	protected boolean functionParameter = FUNCTION_PARAMETER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VariableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CodemodelPackage.Literals.VARIABLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Function getFunction() {
		if (eContainerFeatureID() != CodemodelPackage.VARIABLE__FUNCTION) return null;
		return (Function)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFunction(Function newFunction, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newFunction, CodemodelPackage.VARIABLE__FUNCTION, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFunction(Function newFunction) {
		if (newFunction != eInternalContainer() || (eContainerFeatureID() != CodemodelPackage.VARIABLE__FUNCTION && newFunction != null)) {
			if (EcoreUtil.isAncestor(this, newFunction))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newFunction != null)
				msgs = ((InternalEObject)newFunction).eInverseAdd(this, CodemodelPackage.FUNCTION__LOCALVARIABLES, Function.class, msgs);
			msgs = basicSetFunction(newFunction, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodemodelPackage.VARIABLE__FUNCTION, newFunction, newFunction));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFunctionParameter() {
		return functionParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFunctionParameter(boolean newFunctionParameter) {
		boolean oldFunctionParameter = functionParameter;
		functionParameter = newFunctionParameter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodemodelPackage.VARIABLE__FUNCTION_PARAMETER, oldFunctionParameter, functionParameter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	  * @generated NOT
	 */
	public boolean getIsGlobal()
	{
		return !(this.eContainer instanceof Function);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CodemodelPackage.VARIABLE__FUNCTION:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetFunction((Function)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CodemodelPackage.VARIABLE__FUNCTION:
				return basicSetFunction(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case CodemodelPackage.VARIABLE__FUNCTION:
				return eInternalContainer().eInverseRemove(this, CodemodelPackage.FUNCTION__LOCALVARIABLES, Function.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CodemodelPackage.VARIABLE__FUNCTION:
				return getFunction();
			case CodemodelPackage.VARIABLE__FUNCTION_PARAMETER:
				return isFunctionParameter();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CodemodelPackage.VARIABLE__FUNCTION:
				setFunction((Function)newValue);
				return;
			case CodemodelPackage.VARIABLE__FUNCTION_PARAMETER:
				setFunctionParameter((Boolean)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CodemodelPackage.VARIABLE__FUNCTION:
				setFunction((Function)null);
				return;
			case CodemodelPackage.VARIABLE__FUNCTION_PARAMETER:
				setFunctionParameter(FUNCTION_PARAMETER_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CodemodelPackage.VARIABLE__FUNCTION:
				return getFunction() != null;
			case CodemodelPackage.VARIABLE__FUNCTION_PARAMETER:
				return functionParameter != FUNCTION_PARAMETER_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case CodemodelPackage.VARIABLE___GET_IS_GLOBAL:
				return getIsGlobal();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (functionParameter: ");
		result.append(functionParameter);
		result.append(')');
		return result.toString();
	}

} //VariableImpl
