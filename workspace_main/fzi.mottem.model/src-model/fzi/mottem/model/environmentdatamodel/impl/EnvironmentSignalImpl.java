/**
 */
package fzi.mottem.model.environmentdatamodel.impl;

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
import fzi.mottem.model.environmentdatamodel.EnvironmentDataInstance;
import fzi.mottem.model.environmentdatamodel.EnvironmentSignal;
import fzi.mottem.model.environmentdatamodel.EnvironmentdatamodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Environment Signal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link fzi.mottem.model.environmentdatamodel.impl.EnvironmentSignalImpl#getName <em>Name</em>}</li>
 *   <li>{@link fzi.mottem.model.environmentdatamodel.impl.EnvironmentSignalImpl#getPhysicalUnit <em>Physical Unit</em>}</li>
 *   <li>{@link fzi.mottem.model.environmentdatamodel.impl.EnvironmentSignalImpl#getEdmInstance <em>Edm Instance</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnvironmentSignalImpl extends MinimalEObjectImpl.Container implements EnvironmentSignal {
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
	protected EnvironmentSignalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EnvironmentdatamodelPackage.Literals.ENVIRONMENT_SIGNAL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__PHYSICAL_UNIT, oldPhysicalUnit, physicalUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentDataInstance getEdmInstance() {
		if (eContainerFeatureID() != EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE) return null;
		return (EnvironmentDataInstance)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEdmInstance(EnvironmentDataInstance newEdmInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newEdmInstance, EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEdmInstance(EnvironmentDataInstance newEdmInstance) {
		if (newEdmInstance != eInternalContainer() || (eContainerFeatureID() != EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE && newEdmInstance != null)) {
			if (EcoreUtil.isAncestor(this, newEdmInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newEdmInstance != null)
				msgs = ((InternalEObject)newEdmInstance).eInverseAdd(this, EnvironmentdatamodelPackage.ENVIRONMENT_DATA_INSTANCE__SIGNALS, EnvironmentDataInstance.class, msgs);
			msgs = basicSetEdmInstance(newEdmInstance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE, newEdmInstance, newEdmInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getDisplayName() {
		EnvironmentDataInstance edi = getEdmInstance();
		String containerPrefix;
		if (edi == null || edi.getName() == null || edi.getName().isEmpty())
		{
			containerPrefix = "<Environment>.";
		}
		else
		{
			containerPrefix =  edi.getName() + ".";
		}
		
		return containerPrefix + getName();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetEdmInstance((EnvironmentDataInstance)otherEnd, msgs);
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
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE:
				return basicSetEdmInstance(null, msgs);
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
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE:
				return eInternalContainer().eInverseRemove(this, EnvironmentdatamodelPackage.ENVIRONMENT_DATA_INSTANCE__SIGNALS, EnvironmentDataInstance.class, msgs);
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
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__NAME:
				return getName();
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__PHYSICAL_UNIT:
				return getPhysicalUnit();
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE:
				return getEdmInstance();
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
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__NAME:
				setName((String)newValue);
				return;
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__PHYSICAL_UNIT:
				setPhysicalUnit((String)newValue);
				return;
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE:
				setEdmInstance((EnvironmentDataInstance)newValue);
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
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__PHYSICAL_UNIT:
				setPhysicalUnit(PHYSICAL_UNIT_EDEFAULT);
				return;
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE:
				setEdmInstance((EnvironmentDataInstance)null);
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
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__PHYSICAL_UNIT:
				return PHYSICAL_UNIT_EDEFAULT == null ? physicalUnit != null : !PHYSICAL_UNIT_EDEFAULT.equals(physicalUnit);
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL__EDM_INSTANCE:
				return getEdmInstance() != null;
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
				case BaseelementsPackage.IDISPLAYABLE___GET_DISPLAY_NAME: return EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL___GET_DISPLAY_NAME;
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
			case EnvironmentdatamodelPackage.ENVIRONMENT_SIGNAL___GET_DISPLAY_NAME:
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

} //EnvironmentSignalImpl
