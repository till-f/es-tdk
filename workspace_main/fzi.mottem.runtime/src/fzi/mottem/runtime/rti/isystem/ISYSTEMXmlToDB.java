/**
 * 
 */
package fzi.mottem.runtime.rti.isystem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * @author deuchler
 * 
 * reads an winIDEA generated xml file with profiling data and stores the content in a sql database
 *
 */
public class ISYSTEMXmlToDB 
{
	private final ISYSTEMTraceDriver _traceCtrl;
	
	public ISYSTEMXmlToDB(ISYSTEMTraceDriver traceCtrl)
	{
		_traceCtrl = traceCtrl;
	}

	/**
	 * parses xml and writes it to a database
	 */
	public void run()
	{
		try
		{
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			FileReader fileReader = new FileReader(_traceCtrl.getTraceXMLFile());
			InputSource inputSource = new InputSource(fileReader);
			
			xmlReader.setContentHandler(new ISYSTEMXmlContentHandler(_traceCtrl));
			xmlReader.parse(inputSource);
		}
		catch(FileNotFoundException e)
		{
			System.err.println(e);
		}
		catch(SAXException | SQLException | IOException e)
		{
			System.err.println(e);
		} 
	}
}
