package fzi.mottem.runtime.dataexchanger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fzi.mottem.runtime.dataexchanger.Signal.SignalType;
import fzi.mottem.runtime.dataexchanger.interfaces.ITargetDataConsumer;

public class DataExchanger {

	private static final HashMap<Signal, List<ITargetDataConsumer>> signalIdHash = new HashMap<Signal, List<ITargetDataConsumer>>();

	private static final HashMap<String, ExchangeLink> exchangeLinkHash = new HashMap<String, ExchangeLink>();
	
	private static long baseTimeStamp = 0;

	private DataExchanger() {

	}

	/**
	 * Removes a DataConsumer object from the Signals HashMap. There will be no
	 * Signals associated with this consumer after this operation.
	 * 
	 * @param consumer
	 *            The Consumer that will be removed
	 */
	public static void removeConsumer(ITargetDataConsumer consumer) {
		signalIdHash.forEach((signal, consumerList) -> consumerList.remove(consumer));
	}

	/**
	 * Registers a DataConsumer object for a given Signal. If the Signal is unknown to the
	 * DataExchanger then the consumer will not be registered.
	 * 
	 * @param signal
	 *            The Signal which will be associated with the DataConsumer.
	 *            All existing associations will be removed.
	 * @param c
	 *            the DataConsumer
	 * @return <b> true </b> if the consumer is successfully registered,
	 *         <b> false </b> otherwise
	 */
	public static boolean replaceSignal(Signal signal, ITargetDataConsumer c) {
		removeConsumer(c);
		
		return addSignalToConsumer(signal, c);
	}

	/**
	 * Registers a DataConsumer object for a given Signal UID. If there is no such Signal
	 * then the consumer will not be registered.
	 * 
	 * @param uid
	 *            the Signal UID which will be associated with the DataConsumer
	 *            All existing associations will be removed.
	 * @param c
	 *            the DataConsumer
	 * @return <b> true </b> if the consumer is successfully registered,
	 *         <b> false </b> otherwise
	 */
	public static boolean replaceSignal(String uid, ITargetDataConsumer c) {
		boolean registered = false;
		Signal signal = getSignal(uid);
		if (signal != null) {
			registered = replaceSignal(signal, c);
		} else {
			System.out.println("DataExchanger - Tried to register consumer " + c.getClass().getSimpleName()
					+ " but could not find signal with uid " + uid);
		}

		return registered;
	}

	/**
	 * Registers a DataConsumer object for a given Signal. If the Signal is unknown to the
	 * DataExchanger then the consumer will not be registered.
	 * 
	 * @param signal
	 *            the Signal which will be associated with the DataConsumer.
	 *            Existing association will be kept (multiple signals for consumer possible)
	 * @param c
	 *            the DataConsumer
	 * @return <b> true </b> if the consumer is successfully registered,
	 *         <b> false </b> otherwise
	 */
	public static boolean addSignalToConsumer(Signal signal, ITargetDataConsumer c) {
		boolean registered = false;

		System.out.println("DataExchanger - Registering consumer for signal " + signal.getId() + ": " + c.toString());

		if(signalIdHash.get(signal) != null) {
			registered = signalIdHash.get(signal).add(c);
		} else {
			registered = false;
		}

		return registered;
	}

	/**
	 * Registers a DataConsumer object for a given Signal UID. If there is no such Signal
	 * then the consumer will not be registered.
	 * 
	 * @param uid
	 *            the Signal UID which will be associated with the DataConsumer
	 *            Existing association will be kept (multiple signals for consumer possible)
	 * @param c
	 *            the DataConsumer
	 * @return <b> true </b> if the consumer is successfully registered,
	 *         <b> false </b> otherwise
	 */
	public static boolean addSignalToConsumer(String uid, ITargetDataConsumer c) {
		boolean registered = false;
		Signal signal = getSignal(uid);
		if (signal != null) {
			registered = addSignalToConsumer(signal, c);
		} else {
			System.out.println("DataExchanger - Tried to register consumer " + c.getClass().getSimpleName()
					+ " but could not find signal with uid " + uid);
		}

		return registered;
	}
	

	/**
	 * Gathers all Signals known to the DataExchanger
	 * 
	 * @return a List<Signal> containing references of all known Signals.
	 */
	public static List<Signal> getSignals() {
		return new ArrayList<Signal>(signalIdHash.keySet());
	}

	/**
	 * Set up a new Signal object for a given UID and a simple name. The Signal 
	 * can later be retrieved and used through its UID. If the UID is already 
	 * taken then a new Signal will not be added.
	 * 
	 * @param uid
	 *            the unique Signal id
	 * @param simpleName
	 * 			  the simple name
	 * @param type
	 * 			  the signal type
	 * @return the newly created Signal or the old Signal with this uid
	 */
	public static Signal setUpSignal(String uid, String simpleName, SignalType type) {
		if (getSignal(uid) == null) {
			return addSignal(uid, simpleName, type);
		} else {
			return getSignal(uid);
		}
	}

	/**
	 * Returns the Signal object for a given unique signalID string.
	 * Returns null if there is no Signal
	 * @param signalUID
	 *            the unique Signal id
	 * @return the corresponding Signal
	 */
	public static Signal getSignal(String signalUID) {

		for (Signal signal : signalIdHash.keySet()) {
			if (signal.getId().equals(signalUID))
				return signal;
		}

		return null;
	}
	
	/**
	 * Drops a Signal from the DataExchanger
	 * @param s the signal that will be dropped
	 */
	public static void dropSignal(Signal s) {
		signalIdHash.remove(s);
	}
	
	/**
	 * Drops a Signal with the given UID from the DataExchanger.
	 * Useful if a new Signal with the same UID has to be created.
	 * @param signalUID the UID which identifies the Signal
	 */
	public static void dropSignal(String signalUID) {
		for (Signal signal : signalIdHash.keySet()) {
			if (signal.getId().equals(signalUID))
				signalIdHash.remove(signal);
		}
	}

	/**
	 * Drops all Signals from the DataExchanger.
	 */
	public static void dropAllSignals() {
		signalIdHash.clear();
	}

	/**
	 * This method consumes a signal message with a given signal String ID, a
	 * value and a time stamp. The DataExchanger sends the message to all
	 * Consumers that are registered for its Signal.
	 * 
	 * @param message
	 *            The message object that will be sent.
	 */

	public static void consume(DEMessage message) {
		if(message.getTimestamp() == 0) message.setTimestamp(System.currentTimeMillis() - baseTimeStamp);
		for (ITargetDataConsumer consumer : signalIdHash.get(message.getSignal())) {
			consumer.consume(message);
		}
	}

	/**
	 * This method consumes a burst of signal messages, each with a given signal
	 * String ID, a value and a time stamp. The DataExchanger either sends the
	 * messages in the given order, calling the consumers which are registered
	 * for the specific Signal of each message, or sends them as a collection to
	 * each consumer that has been registered for the Signal at index 0. To
	 * differentiate between the cases the mixedSignals parameter can be set to
	 * true or false.
	 * 
	 * @param messagess
	 *            The messages list that will be sent.
	 * @param mixedSignals
	 *            pass as false if all signals have the same ID and they will be
	 *            sent as a collection; true if every signal should be send
	 *            separately.
	 */

	public static void consumeBurst(List<DEMessage> messages, boolean mixedSignals) {

		if (!mixedSignals) {
			for (ITargetDataConsumer consumer : signalIdHash.get(messages.get(0).getSignal())) {
				consumer.consumeBurst(messages);
			}
		} else {
			for (DEMessage msg : messages) {
				consume(msg);
			}
		}

	}

	private static Signal addSignal(String uid, String simpleName, SignalType type) {
		Signal newSignal = new Signal(uid, simpleName, type);
		signalIdHash.put(newSignal, new ArrayList<ITargetDataConsumer>());
		return newSignal;
	}

	// ------------------------- Addition : TraceExchangeLink methods
	// I also don't want to make the DataExchanger dependable on the
	// RealtimeGraph project

	/**
	 * Will register an exchange link in a dedicated HashTable with its name as
	 * a key IF the link name is not taken.
	 * 
	 * @param link
	 *            the ExchangeLink that will be put in the HashTable
	 */
	public static void registerExchangeLink(ExchangeLink link) {
		exchangeLinkHash.putIfAbsent(link.getName(), link);
	}

	/**
	 * Will register a new exchange link in a dedicated HashTable with the name
	 * as a key IF the link name is not taken. Warning! This method will create
	 * a pure ExchangeLink with no special functionality
	 * 
	 * @param linkName
	 *            the name of the new ExchangeLink that will be put in the
	 *            HashTable
	 */
	public static void registerExchangeLink(String linkName) {
		ExchangeLink link = new ExchangeLink(linkName);
		exchangeLinkHash.putIfAbsent(link.getName(), link);
	}

	/**
	 * Retrieve an ExchangeLink based on its name
	 * 
	 * @param name
	 *            name of the ExchangeLink
	 * @return the corresponding ExchangeLink
	 */
	public static ExchangeLink getExchangeLink(String name) {
		return exchangeLinkHash.get(name); // may be a bad design decision to
											// use thi method
	}

	/**
	 * Make a burst consume call on an ExchangeLink identified by its name. The
	 * burst consume functionality is strictly implementation-dependent. The
	 * method performs no actions if a Link with this name isn't registered.
	 * 
	 * @param name
	 *            The String name of the exchange link that will consume the
	 *            messages
	 * @param messages
	 *            A List of DEMessages that will be consumed
	 */
	public static void exchangeLinkBurst(String name, List<DEMessage> messages) {
		if (exchangeLinkHash.containsKey(name)) {
			exchangeLinkHash.get(name).consumeBurst(messages);
		}
	}

	/**
	 * 
	 * @return A Strings ArrayList containing the names of the ExchangeLinks
	 *         known by the DataExchanger
	 */
	public static ArrayList<String> getExchangeLinkNames() {
		return new ArrayList<String>(exchangeLinkHash.keySet());
	}
	
	/**
	 * Sets the DE's base time stamp to the current system time.
	 */
	public static void resetZeroTimestamp() {
		baseTimeStamp = System.currentTimeMillis();
	}

}
