package fzi.mottem.runtime.rtgraph.listeners;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

public class IntegerListener  implements VerifyListener  {

	@Override
	public void verifyText(VerifyEvent e) {
		
		Text text = (Text)e.getSource();
		final String oldS = text.getText();
		String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

		boolean isInt = true;
		
		if(newS.length() > 0) {
			try
	        {
	            Integer.parseInt(newS);
	        }
	        catch(NumberFormatException ex)
	        {
	            isInt = false;        
	        }
		}
       
        if(!isInt) e.doit = false;
		
	}

}
