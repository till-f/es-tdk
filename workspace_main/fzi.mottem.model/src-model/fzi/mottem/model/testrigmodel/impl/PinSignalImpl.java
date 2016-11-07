/**
 */
package fzi.mottem.model.testrigmodel.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import fzi.mottem.model.baseelements.BaseelementsPackage;
import fzi.mottem.model.baseelements.IDisplayable;
import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.model.testrigmodel.IOPin;
import fzi.mottem.model.testrigmodel.PinSignal;
import fzi.mottem.model.testrigmodel.TestrigmodelPackage;
import fzi.mottem.model.testrigmodel.UUT;
import fzi.util.ecore.EcoreUtils;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Pin Signal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.PinSignalImpl#getName <em>Name</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.PinSignalImpl#getPhysicalUnit <em>Physical Unit</em>}</li>
 *   <li>{@link fzi.mottem.model.testrigmodel.impl.PinSignalImpl#getIopin <em>Iopin</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PinSignalImpl extends MinimalEObjectImpl.Container implements PinSignal {
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
	 * The default value of the '{@link #getPhysicalUnit() <em>Physical Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhysicalUnit()
	 * @generated
	 * @ordered
	 */
	protected static final String PHYSICAL_UNIT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPhysicalUnit() <em>Physical Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhysicalUnit()
	 * @generated
	 * @ordered
	 */
	protected String physicalUnit = PHYSICAL_UNIT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PinSignalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestrigmodelPackage.Literals.PIN_SIGNAL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.PIN_SIGNAL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPhysicalUnit() {
		return physicalUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPhysicalUnit(String newPhysicalUnit) {
		String oldPhysicalUnit = physicalUnit;
		physicalUnit = newPhysicalUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.PIN_SIGNAL__PHYSICAL_UNIT, oldPhysicalUnit, physicalUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IOPin getIopin() {
		if (eContainerFeatureID() != TestrigmodelPackage.PIN_SIGNAL__IOPIN) return null;
		return (IOPin)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetIopin(IOPin newIopin, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newIopin, TestrigmodelPackage.PIN_SIGNAL__IOPIN, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIopin(IOPin newIopin) {
		if (newIopin != eInternalContainer() || (eContainerFeatureID() != TestrigmodelPackage.PIN_SIGNAL__IOPIN && newIopin != null)) {
			if (EcoreUtil.isAncestor(this, newIopin))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newIopin != null)
				msgs = ((InternalEObject)newIopin).eInverseAdd(this, TestrigmodelPackage.IO_PIN__PIN_SIGNALS, IOPin.class, msgs);
			msgs = basicSetIopin(newIopin, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestrigmodelPackage.PIN_SIGNAL__IOPIN, newIopin, newIopin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getDisplayName() {
		UUT board = EcoreUtils.getContainerInstanceOf(this, UUT.class);
		String containerPrefix;
		if (board == null || board.getName() == null || board.getName().isEmpty())
		{
			containerPrefix = "<Board>.";
		}
		else
		{
			containerPrefix =  board.getName() + ".";
		}

		IOPin iop = getIopin();
		if (iop == null || iop.getName() == null || iop.getName().isEmpty())
		{
			return containerPrefix + "<Pin>." + getName();
		}
		else
		{
			return containerPrefix + iop.getName() + "." + getName();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestrigmodelPackage.PIN_SIGNAL__IOPIN:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetIopin((IOPin)otherEnd, msgs);
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
			case TestrigmodelPackage.PIN_SIGNAL__IOPIN:
				return basicSetIopin(null, msgs);
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
			case TestrigmodelPackage.PIN_SIGNAL__IOPIN:
				return eInternalContainer().eInverseRemove(this, TestrigmodelPackage.IO_PIN__PIN_SIGNALS, IOPin.class, msgs);
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
			case TestrigmodelPackage.PIN_SIGNAL__NAME:
				return getName();
			case TestrigmodelPackage.PIN_SIGNAL__PHYSICAL_UNIT:
				return getPhysicalUnit();
			case TestrigmodelPackage.PIN_SIGNAL__IOPIN:
				return getIopin();
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
			case TestrigmodelPackage.PIN_SIGNAL__NAME:
				setName((String)newValue);
				return;
			case TestrigmodelPackage.PIN_SIGNAL__PHYSICAL_UNIT:
				setPhysicalUnit((String)newValue);
				return;
			case TestrigmodelPackage.PIN_SIGNAL__IOPIN:
				setIopin((IOPin)newValue);
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
			case TestrigmodelPackage.PIN_SIGNAL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TestrigmodelPackage.PIN_SIGNAL__PHYSICAL_UNIT:
				setPhysicalUnit(PHYSICAL_UNIT_EDEFAULT);
				return;
			case TestrigmodelPackage.PIN_SIGNAL__IOPIN:
				setIopin((IOPin)null);
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
			case TestrigmodelPackage.PIN_SIGNAL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TestrigmodelPackage.PIN_SIGNAL__PHYSICAL_UNIT:
				return PHYSICAL_UNIT_EDEFAULT == null ? physicalUnit != null : !PHYSICAL_UNIT_EDEFAULT.equals(physicalUnit);
			case TestrigmodelPackage.PIN_SIGNAL__IOPIN:
				return getIopin() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == ITestReadable.class) {
			switch (baseOperationID) {
				default: return -1;
			}
		}
		if (baseClass == IDisplayable.class) {
			switch (baseOperationID) {
				case BaseelementsPackage.IDISPLAYABLE___GET_DISPLAY_NAME: return TestrigmodelPackage.PIN_SIGNAL___GET_DISPLAY_NAME;
				default: return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case TestrigmodelPackage.PIN_SIGNAL___GET_DISPLAY_NAME:
				return getDisplayName();
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
		result.append(" (name: ");
		result.append(name);
		result.append(", physicalUnit: ");
		result.append(physicalUnit);
		result.append(')');
		return result.toString();
	}

} //PinSignalImpl
