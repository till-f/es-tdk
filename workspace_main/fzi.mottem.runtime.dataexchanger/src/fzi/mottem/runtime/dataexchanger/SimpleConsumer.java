package fzi.mottem.runtime.dataexchanger;

import java.util.List;

import fzi.mottem.runtime.dataexchanger.interfaces.ITargetDataConsumer;


//for testing purposes only
public class SimpleConsumer implements ITargetDataConsumer {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SimpleConsumer(String name) {
		this.name = name;
	}

	@Override
	public void drop() {
		DataExchanger.removeConsumer(this);
	}

	@Override
	public void consume(DEMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void consumeBurst(List<DEMessage> messages) {
		// TODO Auto-generated method stub
		
	}

	

}
