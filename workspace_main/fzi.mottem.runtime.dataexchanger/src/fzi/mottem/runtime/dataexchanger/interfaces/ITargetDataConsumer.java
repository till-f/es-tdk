package fzi.mottem.runtime.dataexchanger.interfaces;


public interface ITargetDataConsumer extends IConsumingEntity{

	/**
	 * Drops this consumer and removes it from the Data exchange framework
	 * 
	 * @param de
	 */
	public void drop();
	
}