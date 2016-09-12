/**
 */
package fzi.mottem.model.testrigmodel.impl;

import fzi.mottem.model.baseelements.BaseelementsPackage;
import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IInspectorConnector;
import fzi.mottem.model.baseelements.INamed;
import fzi.mottem.model.baseelements.IReferenceableContainer;
import fzi.mottem.model.baseelements.ISymbolContainer;
import fzi.mottem.model.baseelements.ITestReferenceable;

import fzi.mottem.model.testrigmodel.IOPin;
import fzi.mottem.model.testrigmodel.PinSignal;
import fzi.mottem.model.testrigmodel.TestrigmodelPackage;
import fzi.mottem.model.testrigmodel.UUT;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IO Pin</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.IOPinImpl#getInspectorConnector <em>Inspector Connector</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.IOPinImpl#getName <em>Name</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.IOPinImpl#getSymbolContainer <em>Symbol Container</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.IOPinImpl#getUut <em>Uut</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.IOPinImpl#getPinSignals <em>Pin Signals</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IOPinImpl extends MinimalEObjectImpl.Container implements IOPin {
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
	 * The cached value of the '{@link #getSymbolContainer() <em>Symbol Container</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbolContainer()
	 * @generated
	 * @ordered
	 */
	protected ISymbolContainer symbolContainer;

	/**
	 * The cached value of the '{@link #getPinSignals() <em>Pin Signals</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPinSignals()
	 * @generated
	 * @ordered
	 */
	protected EList<PinSignal> pinSignals;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IOPinImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestrigmodelPackage.Literals.IO_PIN;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TestrigmodelPackage.IO_PIN__INSPECTOR_CONNECTOR, oldInspectorConnector, inspectorConnector));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.IO_PIN__INSPECTOR_CONNECTOR, oldInspectorConnector, newInspectorConnector);
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
				msgs = ((InternalEObject)inspectorConnector).eInverseRemove(this, BaseelementsPackage.IINSPECTOR_CONNECTOR__INSPECTABLE, IInspectorConnector.class, msgs);
			if (newInspectorConnector != null)
				msgs = ((InternalEObject)newInspectorConnector).eInverseAdd(this, BaseelementsPackage.IINSPECTOR_CONNECTOR__INSPECTABLE, IInspectorConnector.class, msgs);
			msgs = basicSetInspectorConnector(newInspectorConnector, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.IO_PIN__INSPECTOR_CONNECTOR, newInspectorConnector, newInspectorConnector));
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
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.IO_PIN__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ISymbolContainer getSymbolContainer() {
		return this;
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
	public void setSymbolContainer(ISymbolContainer newSymbolContainer) {
		throw new RuntimeException("Cannot set SymbolContainer of IOPin as it IS the SymbolContainer");
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UUT getUut() {
		if (eContainerFeatureID() != TestrigmodelPackage.IO_PIN__UUT) return null;
		return (UUT)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUut(UUT newUut, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newUut, TestrigmodelPackage.IO_PIN__UUT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUut(UUT newUut) {
		if (newUut != eInternalContainer() || (eContainerFeatureID() != TestrigmodelPackage.IO_PIN__UUT && newUut != null)) {
			if (EcoreUtil.isAncestor(this, newUut))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newUut != null)
				msgs = ((InternalEObject)newUut).eInverseAdd(this, TestrigmodelPackage.UUT__IO_PINS, UUT.class, msgs);
			msgs = basicSetUut(newUut, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.IO_PIN__UUT, newUut, newUut));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PinSignal> getPinSignals() {
		if (pinSignals == null) {
			pinSignals = new EObjectContainmentWithInverseEList<PinSignal>(PinSignal.class, this, TestrigmodelPackage.IO_PIN__PIN_SIGNALS, TestrigmodelPackage.PIN_SIGNAL__IOPIN);
		}
		return pinSignals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestrigmodelPackage.IO_PIN__INSPECTOR_CONNECTOR:
				if (inspectorConnector != null)
					msgs = ((InternalEObject)inspectorConnector).eInverseRemove(this, BaseelementsPackage.IINSPECTOR_CONNECTOR__INSPECTABLE, IInspectorConnector.class, msgs);
				return basicSetInspectorConnector((IInspectorConnector)otherEnd, msgs);
			case TestrigmodelPackage.IO_PIN__UUT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetUut((UUT)otherEnd, msgs);
			case TestrigmodelPackage.IO_PIN__PIN_SIGNALS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getPinSignals()).basicAdd(otherEnd, msgs);
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
			case TestrigmodelPackage.IO_PIN__INSPECTOR_CONNECTOR:
				return basicSetInspectorConnector(null, msgs);
			case TestrigmodelPackage.IO_PIN__UUT:
				return basicSetUut(null, msgs);
			case TestrigmodelPackage.IO_PIN__PIN_SIGNALS:
				return ((InternalEList<?>)getPinSignals()).basicRemove(otherEnd, msgs);
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
			case TestrigmodelPackage.IO_PIN__UUT:
				return eInternalContainer().eInverseRemove(this, TestrigmodelPackage.UUT__IO_PINS, UUT.class, msgs);
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
			case TestrigmodelPackage.IO_PIN__INSPECTOR_CONNECTOR:
				if (resolve) return getInspectorConnector();
				return basicGetInspectorConnector();
			case TestrigmodelPackage.IO_PIN__NAME:
				return getName();
			case TestrigmodelPackage.IO_PIN__SYMBOL_CONTAINER:
				if (resolve) return getSymbolContainer();
				return basicGetSymbolContainer();
			case TestrigmodelPackage.IO_PIN__UUT:
				return getUut();
			case TestrigmodelPackage.IO_PIN__PIN_SIGNALS:
				return getPinSignals();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TestrigmodelPackage.IO_PIN__INSPECTOR_CONNECTOR:
				setInspectorConnector((IInspectorConnector)newValue);
				return;
			case TestrigmodelPackage.IO_PIN__NAME:
				setName((String)newValue);
				return;
			case TestrigmodelPackage.IO_PIN__SYMBOL_CONTAINER:
				setSymbolContainer((ISymbolContainer)newValue);
				return;
			case TestrigmodelPackage.IO_PIN__UUT:
				setUut((UUT)newValue);
				return;
			case TestrigmodelPackage.IO_PIN__PIN_SIGNALS:
				getPinSignals().clear();
				getPinSignals().addAll((Collection<? extends PinSignal>)newValue);
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
			case TestrigmodelPackage.IO_PIN__INSPECTOR_CONNECTOR:
				setInspectorConnector((IInspectorConnector)null);
				return;
			case TestrigmodelPackage.IO_PIN__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TestrigmodelPackage.IO_PIN__SYMBOL_CONTAINER:
				setSymbolContainer((ISymbolContainer)null);
				return;
			case TestrigmodelPackage.IO_PIN__UUT:
				setUut((UUT)null);
				return;
			case TestrigmodelPackage.IO_PIN__PIN_SIGNALS:
				getPinSignals().clear();
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
			case TestrigmodelPackage.IO_PIN__INSPECTOR_CONNECTOR:
				return inspectorConnector != null;
			case TestrigmodelPackage.IO_PIN__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TestrigmodelPackage.IO_PIN__SYMBOL_CONTAINER:
				return symbolContainer != null;
			case TestrigmodelPackage.IO_PIN__UUT:
				return getUut() != null;
			case TestrigmodelPackage.IO_PIN__PIN_SIGNALS:
				return pinSignals != null && !pinSignals.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == INamed.class) {
			switch (derivedFeatureID) {
				case TestrigmodelPackage.IO_PIN__NAME: return BaseelementsPackage.INAMED__NAME;
				default: return -1;
			}
		}
		if (baseClass == ITestReferenceable.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == IExecutor.class) {
			switch (derivedFeatureID) {
				case TestrigmodelPackage.IO_PIN__SYMBOL_CONTAINER: return BaseelementsPackage.IEXECUTOR__SYMBOL_CONTAINER;
				default: return -1;
			}
		}
		if (baseClass == IReferenceableContainer.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == ISymbolContainer.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == INamed.class) {
			switch (baseFeatureID) {
				case BaseelementsPackage.INAMED__NAME: return TestrigmodelPackage.IO_PIN__NAME;
				default: return -1;
			}
		}
		if (baseClass == ITestReferenceable.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == IExecutor.class) {
			switch (baseFeatureID) {
				case BaseelementsPackage.IEXECUTOR__SYMBOL_CONTAINER: return TestrigmodelPackage.IO_PIN__SYMBOL_CONTAINER;
				default: return -1;
			}
		}
		if (baseClass == IReferenceableContainer.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == ISymbolContainer.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(')');
		return result.toString();
	}

} //IOPinImpl
