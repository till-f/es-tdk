package fzi.mottem.runtime.rtgraph.listeners;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

public class DoubleDigitListener implements VerifyListener {

	@Override
	public void verifyText(VerifyEvent e) {
		
		Text text = (Text)e.getSource();
		final String oldS = text.getText();
		String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);
		
		boolean isDouble = true;
		
		if(newS.length() > 0) {
			if(newS.equals("-")) {
				isDouble = true;
			} else {
				try
		        {
		            Double.parseDouble(newS);
		        }
		        catch(NumberFormatException ex)
		        {
		            isDouble = false;        
		        }
			}
		}
       
        if(!isDouble)
            e.doit = false;
		
	}
}
