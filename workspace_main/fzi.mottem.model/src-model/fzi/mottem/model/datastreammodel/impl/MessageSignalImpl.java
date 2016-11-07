/**
 */
package fzi.mottem.model.datastreammodel.impl;

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
import fzi.mottem.model.datastreammodel.CANMessage;
import fzi.mottem.model.datastreammodel.Conversion;
import fzi.mottem.model.datastreammodel.DataStreamInstance;
import fzi.mottem.model.datastreammodel.DatastreammodelPackage;
import fzi.mottem.model.datastreammodel.EDirection;
import fzi.mottem.model.datastreammodel.MessageSignal;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Message Signal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link fzi.mottem.model.datastreammodel.impl.MessageSignalImpl#getName <em>Name</em>}</li>
 *   <li>{@link fzi.mottem.model.datastreammodel.impl.MessageSignalImpl#getPhysicalUnit <em>Physical Unit</em>}</li>
 *   <li>{@link fzi.mottem.model.datastreammodel.impl.MessageSignalImpl#getDataStreamInstance <em>Data Stream Instance</em>}</li>
 *   <li>{@link fzi.mottem.model.datastreammodel.impl.MessageSignalImpl#getBitOffset <em>Bit Offset</em>}</li>
 *   <li>{@link fzi.mottem.model.datastreammodel.impl.MessageSignalImpl#getBitLength <em>Bit Length</em>}</li>
 *   <li>{@link fzi.mottem.model.datastreammodel.impl.MessageSignalImpl#getConversion <em>Conversion</em>}</li>
 *   <li>{@link fzi.mottem.model.datastreammodel.impl.MessageSignalImpl#getDirection <em>Direction</em>}</li>
 *   <li>{@link fzi.mottem.model.datastreammodel.impl.MessageSignalImpl#getCanMessage <em>Can Message</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MessageSignalImpl extends MinimalEObjectImpl.Container implements MessageSignal {
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
	 * The default value of the '{@link #getBitOffset() <em>Bit Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBitOffset()
	 * @generated
	 * @ordered
	 */
	protected static final int BIT_OFFSET_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBitOffset() <em>Bit Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBitOffset()
	 * @generated
	 * @ordered
	 */
	protected int bitOffset = BIT_OFFSET_EDEFAULT;

	/**
	 * The default value of the '{@link #getBitLength() <em>Bit Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBitLength()
	 * @generated
	 * @ordered
	 */
	protected static final int BIT_LENGTH_EDEFAULT = 32;

	/**
	 * The cached value of the '{@link #getBitLength() <em>Bit Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBitLength()
	 * @generated
	 * @ordered
	 */
	protected int bitLength = BIT_LENGTH_EDEFAULT;

	/**
	 * The cached value of the '{@link #getConversion() <em>Conversion</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConversion()
	 * @generated
	 * @ordered
	 */
	protected Conversion conversion;

	/**
	 * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDirection()
	 * @generated
	 * @ordered
	 */
	protected static final EDirection DIRECTION_EDEFAULT = EDirection.INPUT;

	/**
	 * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDirection()
	 * @generated
	 * @ordered
	 */
	protected EDirection direction = DIRECTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCanMessage() <em>Can Message</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanMessage()
	 * @generated
	 * @ordered
	 */
	protected CANMessage canMessage;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MessageSignalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatastreammodelPackage.Literals.MESSAGE_SIGNAL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DatastreammodelPackage.MESSAGE_SIGNAL__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DatastreammodelPackage.MESSAGE_SIGNAL__PHYSICAL_UNIT, oldPhysicalUnit, physicalUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataStreamInstance getDataStreamInstance() {
		if (eContainerFeatureID() != DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE) return null;
		return (DataStreamInstance)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDataStreamInstance(DataStreamInstance newDataStreamInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newDataStreamInstance, DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDataStreamInstance(DataStreamInstance newDataStreamInstance) {
		if (newDataStreamInstance != eInternalContainer() || (eContainerFeatureID() != DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE && newDataStreamInstance != null)) {
			if (EcoreUtil.isAncestor(this, newDataStreamInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDataStreamInstance != null)
				msgs = ((InternalEObject)newDataStreamInstance).eInverseAdd(this, DatastreammodelPackage.DATA_STREAM_INSTANCE__SIGNALS, DataStreamInstance.class, msgs);
			msgs = basicSetDataStreamInstance(newDataStreamInstance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE, newDataStreamInstance, newDataStreamInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getBitOffset() {
		return bitOffset;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBitOffset(int newBitOffset) {
		int oldBitOffset = bitOffset;
		bitOffset = newBitOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatastreammodelPackage.MESSAGE_SIGNAL__BIT_OFFSET, oldBitOffset, bitOffset));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getBitLength() {
		return bitLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBitLength(int newBitLength) {
		int oldBitLength = bitLength;
		bitLength = newBitLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatastreammodelPackage.MESSAGE_SIGNAL__BIT_LENGTH, oldBitLength, bitLength));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Conversion getConversion() {
		return conversion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConversion(Conversion newConversion, NotificationChain msgs) {
		Conversion oldConversion = conversion;
		conversion = newConversion;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DatastreammodelPackage.MESSAGE_SIGNAL__CONVERSION, oldConversion, newConversion);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConversion(Conversion newConversion) {
		if (newConversion != conversion) {
			NotificationChain msgs = null;
			if (conversion != null)
				msgs = ((InternalEObject)conversion).eInverseRemove(this, DatastreammodelPackage.CONVERSION__SIGNAL, Conversion.class, msgs);
			if (newConversion != null)
				msgs = ((InternalEObject)newConversion).eInverseAdd(this, DatastreammodelPackage.CONVERSION__SIGNAL, Conversion.class, msgs);
			msgs = basicSetConversion(newConversion, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatastreammodelPackage.MESSAGE_SIGNAL__CONVERSION, newConversion, newConversion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDirection getDirection() {
		return direction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDirection(EDirection newDirection) {
		EDirection oldDirection = direction;
		direction = newDirection == null ? DIRECTION_EDEFAULT : newDirection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatastreammodelPackage.MESSAGE_SIGNAL__DIRECTION, oldDirection, direction));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CANMessage getCanMessage() {
		if (canMessage != null && canMessage.eIsProxy()) {
			InternalEObject oldCanMessage = (InternalEObject)canMessage;
			canMessage = (CANMessage)eResolveProxy(oldCanMessage);
			if (canMessage != oldCanMessage) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DatastreammodelPackage.MESSAGE_SIGNAL__CAN_MESSAGE, oldCanMessage, canMessage));
			}
		}
		return canMessage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CANMessage basicGetCanMessage() {
		return canMessage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCanMessage(CANMessage newCanMessage, NotificationChain msgs) {
		CANMessage oldCanMessage = canMessage;
		canMessage = newCanMessage;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DatastreammodelPackage.MESSAGE_SIGNAL__CAN_MESSAGE, oldCanMessage, newCanMessage);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCanMessage(CANMessage newCanMessage) {
		if (newCanMessage != canMessage) {
			NotificationChain msgs = null;
			if (canMessage != null)
				msgs = ((InternalEObject)canMessage).eInverseRemove(this, DatastreammodelPackage.CAN_MESSAGE__SIGNALS, CANMessage.class, msgs);
			if (newCanMessage != null)
				msgs = ((InternalEObject)newCanMessage).eInverseAdd(this, DatastreammodelPackage.CAN_MESSAGE__SIGNALS, CANMessage.class, msgs);
			msgs = basicSetCanMessage(newCanMessage, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatastreammodelPackage.MESSAGE_SIGNAL__CAN_MESSAGE, newCanMessage, newCanMessage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getDisplayName() {
		DataStreamInstance dsi = getDataStreamInstance();
		String containerPrefix;
		if (dsi == null || dsi.getName() == null || dsi.getName().isEmpty())
		{
			containerPrefix = "<Network>.";
		}
		else
		{
			containerPrefix =  dsi.getName() + ".";
		}

		CANMessage cm = getCanMessage();
		if (cm == null || cm.getName() == null || cm.getName().isEmpty())
		{
			return containerPrefix + "<CAN>." + getName();
		}
		else
		{
			return containerPrefix + cm.getName() + "." + getName();
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
			case DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetDataStreamInstance((DataStreamInstance)otherEnd, msgs);
			case DatastreammodelPackage.MESSAGE_SIGNAL__CONVERSION:
				if (conversion != null)
					msgs = ((InternalEObject)conversion).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DatastreammodelPackage.MESSAGE_SIGNAL__CONVERSION, null, msgs);
				return basicSetConversion((Conversion)otherEnd, msgs);
			case DatastreammodelPackage.MESSAGE_SIGNAL__CAN_MESSAGE:
				if (canMessage != null)
					msgs = ((InternalEObject)canMessage).eInverseRemove(this, DatastreammodelPackage.CAN_MESSAGE__SIGNALS, CANMessage.class, msgs);
				return basicSetCanMessage((CANMessage)otherEnd, msgs);
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
			case DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE:
				return basicSetDataStreamInstance(null, msgs);
			case DatastreammodelPackage.MESSAGE_SIGNAL__CONVERSION:
				return basicSetConversion(null, msgs);
			case DatastreammodelPackage.MESSAGE_SIGNAL__CAN_MESSAGE:
				return basicSetCanMessage(null, msgs);
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
			case DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE:
				return eInternalContainer().eInverseRemove(this, DatastreammodelPackage.DATA_STREAM_INSTANCE__SIGNALS, DataStreamInstance.class, msgs);
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
			case DatastreammodelPackage.MESSAGE_SIGNAL__NAME:
				return getName();
			case DatastreammodelPackage.MESSAGE_SIGNAL__PHYSICAL_UNIT:
				return getPhysicalUnit();
			case DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE:
				return getDataStreamInstance();
			case DatastreammodelPackage.MESSAGE_SIGNAL__BIT_OFFSET:
				return getBitOffset();
			case DatastreammodelPackage.MESSAGE_SIGNAL__BIT_LENGTH:
				return getBitLength();
			case DatastreammodelPackage.MESSAGE_SIGNAL__CONVERSION:
				return getConversion();
			case DatastreammodelPackage.MESSAGE_SIGNAL__DIRECTION:
				return getDirection();
			case DatastreammodelPackage.MESSAGE_SIGNAL__CAN_MESSAGE:
				if (resolve) return getCanMessage();
				return basicGetCanMessage();
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
			case DatastreammodelPackage.MESSAGE_SIGNAL__NAME:
				setName((String)newValue);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__PHYSICAL_UNIT:
				setPhysicalUnit((String)newValue);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE:
				setDataStreamInstance((DataStreamInstance)newValue);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__BIT_OFFSET:
				setBitOffset((Integer)newValue);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__BIT_LENGTH:
				setBitLength((Integer)newValue);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__CONVERSION:
				setConversion((Conversion)newValue);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__DIRECTION:
				setDirection((EDirection)newValue);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__CAN_MESSAGE:
				setCanMessage((CANMessage)newValue);
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
			case DatastreammodelPackage.MESSAGE_SIGNAL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__PHYSICAL_UNIT:
				setPhysicalUnit(PHYSICAL_UNIT_EDEFAULT);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE:
				setDataStreamInstance((DataStreamInstance)null);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__BIT_OFFSET:
				setBitOffset(BIT_OFFSET_EDEFAULT);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__BIT_LENGTH:
				setBitLength(BIT_LENGTH_EDEFAULT);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__CONVERSION:
				setConversion((Conversion)null);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__DIRECTION:
				setDirection(DIRECTION_EDEFAULT);
				return;
			case DatastreammodelPackage.MESSAGE_SIGNAL__CAN_MESSAGE:
				setCanMessage((CANMessage)null);
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
			case DatastreammodelPackage.MESSAGE_SIGNAL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DatastreammodelPackage.MESSAGE_SIGNAL__PHYSICAL_UNIT:
				return PHYSICAL_UNIT_EDEFAULT == null ? physicalUnit != null : !PHYSICAL_UNIT_EDEFAULT.equals(physicalUnit);
			case DatastreammodelPackage.MESSAGE_SIGNAL__DATA_STREAM_INSTANCE:
				return getDataStreamInstance() != null;
			case DatastreammodelPackage.MESSAGE_SIGNAL__BIT_OFFSET:
				return bitOffset != BIT_OFFSET_EDEFAULT;
			case DatastreammodelPackage.MESSAGE_SIGNAL__BIT_LENGTH:
				return bitLength != BIT_LENGTH_EDEFAULT;
			case DatastreammodelPackage.MESSAGE_SIGNAL__CONVERSION:
				return conversion != null;
			case DatastreammodelPackage.MESSAGE_SIGNAL__DIRECTION:
				return direction != DIRECTION_EDEFAULT;
			case DatastreammodelPackage.MESSAGE_SIGNAL__CAN_MESSAGE:
				return canMessage != null;
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
				case BaseelementsPackage.IDISPLAYABLE___GET_DISPLAY_NAME: return DatastreammodelPackage.MESSAGE_SIGNAL___GET_DISPLAY_NAME;
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
			case DatastreammodelPackage.MESSAGE_SIGNAL___GET_DISPLAY_NAME:
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
		result.append(", bitOffset: ");
		result.append(bitOffset);
		result.append(", bitLength: ");
		result.append(bitLength);
		result.append(", direction: ");
		result.append(direction);
		result.append(')');
		return result.toString();
	}

} //MessageSignalImpl
