package fzi.mottem.runtime.rtgraph.listeners;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class NameVerifier implements VerifyListener {

	@Override
	public void verifyText(VerifyEvent event) {
		event.doit = true;
		
		char myChar = event.character;
		
		if (event.text.length() > 0) {
			if (event.text.charAt(0) == '.') {
				event.doit = false;
			} else if (!Character.isAlphabetic(myChar)
					&& !(myChar == '.' || myChar == '-' || myChar == '_' || myChar == '(' || myChar == ')')
					&& !Character.isDigit(myChar)) {
				event.doit = false;
			}
		}
	}
}
