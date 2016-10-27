package fzi.mottem.runtime.dataexchanger;

import java.util.List;

import fzi.mottem.runtime.dataexchanger.interfaces.ITargetDataConsumer;

/**
 * This abstract class represents and implementation of the IDataConsumer
 * for the DataExchanger.
 * 
 * The DataExchanger sends (asynchronous) data to the ExchangeLink which saves
 * the data; this data can later be retrieved or rewritten by the DataExchanger
 * 
 * @author Kalin Katev
 *
 */

public class ExchangeLink implements ITargetDataConsumer {

	protected double value;
	protected long timestamp = 0;
	public boolean updated = false;
	
	protected String name;

	public ExchangeLink() {
	
	}
	
	public ExchangeLink(String name) {
		this.name = name;
	}
	
	/**
	 * Consumes a DataExchanger message. The message values are stored in local variables.
	 */
	public void consume(DEMessage msg) {
		this.value = msg.getValue();
		this.timestamp = msg.getTimestamp();
		updated = true;
	}

	@Override
	/**
	 * Disconnects this ExchangeLink from the DataExchanger
	 */
	public void drop() {
		DataExchanger.removeConsumer(this);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void consumeBurst(List<DEMessage> messages) {
		// TODO Auto-generated method stub
	}
}
