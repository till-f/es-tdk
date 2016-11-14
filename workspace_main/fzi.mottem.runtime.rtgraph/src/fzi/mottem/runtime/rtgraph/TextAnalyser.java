package fzi.mottem.runtime.rtgraph;

import java.util.ArrayList;

import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.dataexchanger.interfaces.ITargetDataConsumer;

public class TextAnalyser {
	
	public TextAnalyser() {
		
	}
	
	/**
	 * Returns an ArrayList with the signal simple names
	 * written in a given text
	 * @param text the String that needs to be analyzed
	 * @return
	 */
	public static ArrayList<String> registerSignalArguments(String text, ITargetDataConsumer consumer) {
		
		ArrayList<Signal> signals = new ArrayList<Signal>(DataExchanger.getSignals());
		ArrayList<String> simpleNames = new ArrayList<String>();
		
		for(Signal s : signals) {
			simpleNames.add(s.getSimpleName());
		}
		
		for(int i = 0; i < text.length() - 1; i++) {
			if(text.charAt(i) == '%') {
				String token = getSignalToken(text, i);
				i += token.length();
				if(simpleNames.contains(token)) {
					int index = simpleNames.indexOf(token);
					DataExchanger.replaceSignal(signals.get(index), consumer);
				}
				//i++; ?
				//call subroutine that will
				//get String at %xxxxx 
				//and add it to ArrayList
				//if such simple name exists
				//and returns the new position of i
			}
		}
		
		return null;
	}
	private static String getSignalToken(String text, int startpos) {
		//String token;
		
		StringBuilder sb = new StringBuilder();
		//int endpos = 0;
		
		for(int i = startpos + 1; i < text.length() && text.charAt(i) != ' '; i++) {
			sb.append(text.charAt(i));
		}
		
		return sb.toString();
		
	}
	
}
