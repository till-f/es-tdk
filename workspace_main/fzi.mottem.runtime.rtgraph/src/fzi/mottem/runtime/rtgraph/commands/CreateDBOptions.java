package fzi.mottem.runtime.rtgraph.commands;

import java.util.ArrayList;

public class CreateDBOptions {
	
	public CreateDBOptions() {
		
	}
	
	public ArrayList<String> uids;
	public String graphName;
	
	public boolean connectToDataExchanger = false;
	public boolean applyMetaData = true;
	
	
}
