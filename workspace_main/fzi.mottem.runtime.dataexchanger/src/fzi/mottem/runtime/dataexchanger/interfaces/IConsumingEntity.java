package fzi.mottem.runtime.dataexchanger.interfaces;

import java.util.List;

import fzi.mottem.runtime.dataexchanger.DEMessage;

public interface IConsumingEntity {
	
	/**
	 * This method consumes a signal message with a given signal String ID, a
	 * value and a time stamp.
	 * 
	 * @param message
	 *            The message object that will be sent.
	 */
	public void consume(DEMessage message);
	
	
	/**
	 * This method consumes a burst of signal messages, each with a given signal
	 * String ID, a value and a time stamp.
	 * 
	 * @param messagess
	 *            The messages list that will be sent.
	 */
	public void consumeBurst(List<DEMessage> messages);
	
}
