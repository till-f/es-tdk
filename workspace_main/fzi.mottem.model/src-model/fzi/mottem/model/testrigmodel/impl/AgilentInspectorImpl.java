/**
 */
package fzi.mottem.model.testrigmodel.impl;

import fzi.mottem.model.baseelements.BaseelementsPackage;
import fzi.mottem.model.baseelements.IInspectorConnector;
import fzi.mottem.model.baseelements.ISymbolContainer;

import fzi.mottem.model.testrigmodel.AgilentInspector;
import fzi.mottem.model.testrigmodel.AgilentInspectorContainer;
import fzi.mottem.model.testrigmodel.TestrigmodelPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Agilent Inspector</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.AgilentInspectorImpl#getName <em>Name</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.AgilentInspectorImpl#getRuntimeInspectorClass <em>Runtime Inspector Class</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.AgilentInspectorImpl#getTraceControllerClass <em>Trace Controller Class</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.AgilentInspectorImpl#getInspectorConnector <em>Inspector Connector</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.AgilentInspectorImpl#getSymbolContainer <em>Symbol Container</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.AgilentInspectorImpl#getAgilentContainer <em>Agilent Container</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AgilentInspectorImpl extends MinimalEObjectImpl.Container implements AgilentInspector {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getRuntimeInspectorClass() <em>Runtime Inspector Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRuntimeInspectorClass()
	 * @generated
	 * @ordered
	 */
	protected static final String RUNTIME_INSPECTOR_CLASS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRuntimeInspectorClass() <em>Runtime Inspector Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRuntimeInspectorClass()
	 * @generated
	 * @ordered
	 */
	protected String runtimeInspectorClass = RUNTIME_INSPECTOR_CLASS_EDEFAULT;

	/**
	 * The default value of the '{@link #getTraceControllerClass() <em>Trace Controller Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraceControllerClass()
	 * @generated
	 * @ordered
	 */
	protected static final String TRACE_CONTROLLER_CLASS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTraceControllerClass() <em>Trace Controller Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraceControllerClass()
	 * @generated
	 * @ordered
	 */
	protected String traceControllerClass = TRACE_CONTROLLER_CLASS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInspectorConnector() <em>Inspector Connector</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInspectorConnector()
	 * @generated
	 * @ordered
	 */
	protected IInspectorConnector inspectorConnector;

	/**
	 * The cached value of the '{@link #getSymbolContainer() <em>Symbol Container</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbolContainer()
	 * @generated
	 * @ordered
	 */
	protected ISymbolContainer symbolContainer;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AgilentInspectorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestrigmodelPackage.Literals.AGILENT_INSPECTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.AGILENT_INSPECTOR__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRuntimeInspectorClass() {
		return runtimeInspectorClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRuntimeInspectorClass(String newRuntimeInspectorClass) {
		String oldRuntimeInspectorClass = runtimeInspectorClass;
		runtimeInspectorClass = newRuntimeInspectorClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.AGILENT_INSPECTOR__RUNTIME_INSPECTOR_CLASS, oldRuntimeInspectorClass, runtimeInspectorClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTraceControllerClass() {
		return traceControllerClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTraceControllerClass(String newTraceControllerClass) {
		String oldTraceControllerClass = traceControllerClass;
		traceControllerClass = newTraceControllerClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.AGILENT_INSPECTOR__TRACE_CONTROLLER_CLASS, oldTraceControllerClass, traceControllerClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IInspectorConnector getInspectorConnector() {
		if (inspectorConnector != null && inspectorConnector.eIsProxy()) {
			InternalEObject oldInspectorConnector = (InternalEObject)inspectorConnector;
			inspectorConnector = (IInspectorConnector)eResolveProxy(oldInspectorConnector);
			if (inspectorConnector != oldInspectorConnector) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TestrigmodelPackage.AGILENT_INSPECTOR__INSPECTOR_CONNECTOR, oldInspectorConnector, inspectorConnector));
			}
		}
		return inspectorConnector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IInspectorConnector basicGetInspectorConnector() {
		return inspectorConnector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInspectorConnector(IInspectorConnector newInspectorConnector, NotificationChain msgs) {
		IInspectorConnector oldInspectorConnector = inspectorConnector;
		inspectorConnector = newInspectorConnector;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.AGILENT_INSPECTOR__INSPECTOR_CONNECTOR, oldInspectorConnector, newInspectorConnector);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInspectorConnector(IInspectorConnector newInspectorConnector) {
		if (newInspectorConnector != inspectorConnector) {
			NotificationChain msgs = null;
			if (inspectorConnector != null)
				msgs = ((InternalEObject)inspectorConnector).eInverseRemove(this, BaseelementsPackage.IINSPECTOR_CONNECTOR__INSPECTOR, IInspectorConnector.class, msgs);
			if (newInspectorConnector != null)
				msgs = ((InternalEObject)newInspectorConnector).eInverseAdd(this, BaseelementsPackage.IINSPECTOR_CONNECTOR__INSPECTOR, IInspectorConnector.class, msgs);
			msgs = basicSetInspectorConnector(newInspectorConnector, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.AGILENT_INSPECTOR__INSPECTOR_CONNECTOR, newInspectorConnector, newInspectorConnector));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ISymbolContainer getSymbolContainer() {
		if (symbolContainer != null && symbolContainer.eIsProxy()) {
			InternalEObject oldSymbolContainer = (InternalEObject)symbolContainer;
			symbolContainer = (ISymbolContainer)eResolveProxy(oldSymbolContainer);
			if (symbolContainer != oldSymbolContainer) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TestrigmodelPackage.AGILENT_INSPECTOR__SYMBOL_CONTAINER, oldSymbolContainer, symbolContainer));
			}
		}
		return symbolContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ISymbolContainer basicGetSymbolContainer() {
		return symbolContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setSymbolContainer(ISymbolContainer newSymbolContainer)
	{
		ISymbolContainer oldSymbolContainer = symbolContainer;
		symbolContainer = newSymbolContainer;
		if (eNotificationRequired())
		{
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.AGILENT_INSPECTOR__SYMBOL_CONTAINER, oldSymbolContainer, symbolContainer));
		}
		
		if (getInspectorConnector() != null && getInspectorConnector().getInspectable() != null)
		{
			eNotify(new ENotificationImpl((EObjectImpl)getInspectorConnector().getInspectable(), Notification.SET, TestrigmodelPackage.IO_PIN__SYMBOL_CONTAINER, oldSymbolContainer, newSymbolContainer));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AgilentInspectorContainer getAgilentContainer() {
		if (eContainerFeatureID() != TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER) return null;
		return (AgilentInspectorContainer)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAgilentContainer(AgilentInspectorContainer newAgilentContainer, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newAgilentContainer, TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAgilentContainer(AgilentInspectorContainer newAgilentContainer) {
		if (newAgilentContainer != eInternalContainer() || (eContainerFeatureID() != TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER && newAgilentContainer != null)) {
			if (EcoreUtil.isAncestor(this, newAgilentContainer))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newAgilentContainer != null)
				msgs = ((InternalEObject)newAgilentContainer).eInverseAdd(this, TestrigmodelPackage.AGILENT_INSPECTOR_CONTAINER__AGILENT_PINS, AgilentInspectorContainer.class, msgs);
			msgs = basicSetAgilentContainer(newAgilentContainer, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER, newAgilentContainer, newAgilentContainer));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestrigmodelPackage.AGILENT_INSPECTOR__INSPECTOR_CONNECTOR:
				if (inspectorConnector != null)
					msgs = ((InternalEObject)inspectorConnector).eInverseRemove(this, BaseelementsPackage.IINSPECTOR_CONNECTOR__INSPECTOR, IInspectorConnector.class, msgs);
				return basicSetInspectorConnector((IInspectorConnector)otherEnd, msgs);
			case TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetAgilentContainer((AgilentInspectorContainer)otherEnd, msgs);
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
			case TestrigmodelPackage.AGILENT_INSPECTOR__INSPECTOR_CONNECTOR:
				return basicSetInspectorConnector(null, msgs);
			case TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER:
				return basicSetAgilentContainer(null, msgs);
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
			case TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER:
				return eInternalContainer().eInverseRemove(this, TestrigmodelPackage.AGILENT_INSPECTOR_CONTAINER__AGILENT_PINS, AgilentInspectorContainer.class, msgs);
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
			case TestrigmodelPackage.AGILENT_INSPECTOR__NAME:
				return getName();
			case TestrigmodelPackage.AGILENT_INSPECTOR__RUNTIME_INSPECTOR_CLASS:
				return getRuntimeInspectorClass();
			case TestrigmodelPackage.AGILENT_INSPECTOR__TRACE_CONTROLLER_CLASS:
				return getTraceControllerClass();
			case TestrigmodelPackage.AGILENT_INSPECTOR__INSPECTOR_CONNECTOR:
				if (resolve) return getInspectorConnector();
				return basicGetInspectorConnector();
			case TestrigmodelPackage.AGILENT_INSPECTOR__SYMBOL_CONTAINER:
				if (resolve) return getSymbolContainer();
				return basicGetSymbolContainer();
			case TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER:
				return getAgilentContainer();
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
			case TestrigmodelPackage.AGILENT_INSPECTOR__NAME:
				setName((String)newValue);
				return;
			case TestrigmodelPackage.AGILENT_INSPECTOR__RUNTIME_INSPECTOR_CLASS:
				setRuntimeInspectorClass((String)newValue);
				return;
			case TestrigmodelPackage.AGILENT_INSPECTOR__TRACE_CONTROLLER_CLASS:
				setTraceControllerClass((String)newValue);
				return;
			case TestrigmodelPackage.AGILENT_INSPECTOR__INSPECTOR_CONNECTOR:
				setInspectorConnector((IInspectorConnector)newValue);
				return;
			case TestrigmodelPackage.AGILENT_INSPECTOR__SYMBOL_CONTAINER:
				setSymbolContainer((ISymbolContainer)newValue);
				return;
			case TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER:
				setAgilentContainer((AgilentInspectorContainer)newValue);
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
			case TestrigmodelPackage.AGILENT_INSPECTOR__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TestrigmodelPackage.AGILENT_INSPECTOR__RUNTIME_INSPECTOR_CLASS:
				setRuntimeInspectorClass(RUNTIME_INSPECTOR_CLASS_EDEFAULT);
				return;
			case TestrigmodelPackage.AGILENT_INSPECTOR__TRACE_CONTROLLER_CLASS:
				setTraceControllerClass(TRACE_CONTROLLER_CLASS_EDEFAULT);
				return;
			case TestrigmodelPackage.AGILENT_INSPECTOR__INSPECTOR_CONNECTOR:
				setInspectorConnector((IInspectorConnector)null);
				return;
			case TestrigmodelPackage.AGILENT_INSPECTOR__SYMBOL_CONTAINER:
				setSymbolContainer((ISymbolContainer)null);
				return;
			case TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER:
				setAgilentContainer((AgilentInspectorContainer)null);
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
			case TestrigmodelPackage.AGILENT_INSPECTOR__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TestrigmodelPackage.AGILENT_INSPECTOR__RUNTIME_INSPECTOR_CLASS:
				return RUNTIME_INSPECTOR_CLASS_EDEFAULT == null ? runtimeInspectorClass != null : !RUNTIME_INSPECTOR_CLASS_EDEFAULT.equals(runtimeInspectorClass);
			case TestrigmodelPackage.AGILENT_INSPECTOR__TRACE_CONTROLLER_CLASS:
				return TRACE_CONTROLLER_CLASS_EDEFAULT == null ? traceControllerClass != null : !TRACE_CONTROLLER_CLASS_EDEFAULT.equals(traceControllerClass);
			case TestrigmodelPackage.AGILENT_INSPECTOR__INSPECTOR_CONNECTOR:
				return inspectorConnector != null;
			case TestrigmodelPackage.AGILENT_INSPECTOR__SYMBOL_CONTAINER:
				return symbolContainer != null;
			case TestrigmodelPackage.AGILENT_INSPECTOR__AGILENT_CONTAINER:
				return getAgilentContainer() != null;
		}
		return super.eIsSet(featureID);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", runtimeInspectorClass: ");
		result.append(runtimeInspectorClass);
		result.append(", traceControllerClass: ");
		result.append(traceControllerClass);
		result.append(')');
		return result.toString();
	}

} //AgilentInspectorImpl
