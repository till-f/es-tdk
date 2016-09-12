package fzi.mottem.runtime.rti.isystem;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.runtime.TraceDB;
import fzi.mottem.runtime.TraceDB.TraceDBEvent;


/**
 * 
 * The ContentHandler that is used by the SAX parser to handle XML files that contain winIDEA profiler variables and function calls
 * 
 * @author deuchler
 *
 */

public class ISYSTEMXmlContentHandler implements ContentHandler
{

	// MAPPING is the beginning of the xml document where variable and function mappings are defined
	// TIMELINE is the part of the xml document where reads writes and function calls (X for execute, E for exit) are stored
		
	// map the "handle" numerical values to the corresponding name of a variable/function
	private HashMap<String,String> _handleToUIDMap = new HashMap<String,String>();
	
	class Entry
	{
		public String name = null;
		public String value = null; 
	}
	
	private Entry entry = new Entry(); // xml entry of the current element
	
	private HashMap<String,String> domSubSet = new HashMap<String,String>(); // contains <name>value</name> pairs
	
	
	private final ISYSTEMTraceDriver _traceCtrl;
	private final TraceDB _traceDB;

	public ISYSTEMXmlContentHandler(ISYSTEMTraceDriver traceCtrl) throws SQLException 
	{
		_traceCtrl = traceCtrl;
		_traceDB = traceCtrl.getTrace().getTraceDB();
	}
	
	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException 
	{
		//TODO read only between arg1 and arg2
			entry.value = new String(arg0, arg1, arg2);
			if(entry.value.equals("\n") || entry.value.equals("\n\n")) //"TODO regular expression
				entry.value = null;
	}
	
	
	@Override
	public void endElement(String uri,String localName,String qName) throws SAXException 
	{ 
		if(entry.value != null && !entry.value.equals(""))
		{
			entry.name = localName;
			domSubSet.put(entry.name, entry.value);
			entry = new Entry();
		}
		if(localName.equals("AREA"))
		{
			String isystemName = domSubSet.get("NAME");
			if (_traceCtrl.ISYSTEMNameToElementMap.containsKey(isystemName))
			{
				ITestReferenceable tref = _traceCtrl.ISYSTEMNameToElementMap.get(isystemName);
				String ptsUID = PTSpecUtils.getElementUID(tref);
				_handleToUIDMap.put(domSubSet.get("HANDLE"), ptsUID);
			}
			else
			{
				System.err.println("Unexpected name in ISYSTEM XML: " + isystemName);
			}
			
			domSubSet = new HashMap<String, String>();
		}
		else if(localName.equals("T"))
		{
			// TIME
			Long time = Long.decode(domSubSet.get("TIME"));
			
			// EVENT
			/*
			 *  W _W_rite to [ele]
			 *  E [ele] is _E_ntered
			 *  S in [ele] _S_ubroutine is about to be called
			 *  R [ele] has _R_eturnd
			 *  X [ele] has e_X_ited function ?
			 */
			TraceDBEvent dbEvent;
			String isystemEventString = domSubSet.get("EVENT");
			if      (isystemEventString.contains("W")) dbEvent = TraceDBEvent.Write;
			else if (isystemEventString.contains("E")) dbEvent = TraceDBEvent.Call;
			else if (isystemEventString.contains("R")) dbEvent = TraceDBEvent.Return;
			else dbEvent = TraceDBEvent.UNSPECIFIED;

			// UID
			String isystemHandle = domSubSet.get("HANDLE");
			String uid;
			if (_handleToUIDMap.containsKey(isystemHandle))
			{
				uid = _handleToUIDMap.get(domSubSet.get("HANDLE"));
			}
			else
			{
				uid = isystemHandle;
				System.err.println("No UID for handle " + isystemHandle);
			}

			// VALUE
			String value = domSubSet.get("VALUE");
			
			_traceDB.insertValueMS(time.doubleValue()/1000/1000, dbEvent, uid, value);
				
			domSubSet = new HashMap<String, String>();
		}
	}

	
	@Override
	public void endDocument() throws SAXException 
	{
		// nothing to do
	}
	
	/**
	 * for debugging
	 * @param map
	 */
	@SuppressWarnings("unused")
	private static void printMap(Map<String,String> map)
	{
		Collection<java.util.Map.Entry<String, String>> c = map.entrySet();
		Iterator<java.util.Map.Entry<String, String>> it = c.iterator();
		while(it.hasNext())
		{
			java.util.Map.Entry<String, String> i = it.next();
			System.out.println("Key: " + i.getKey() + " Value: " + i.getValue());
		}
	}

	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException{}


	@Override
	public void endPrefixMapping(String arg0) throws SAXException {}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {}
	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {}
	@Override
	public void setDocumentLocator(Locator arg0) {}

	@Override
	public void skippedEntity(String arg0) throws SAXException {}

	@Override
	public void startDocument() throws SAXException {}

	@Override
	public void startElement(String uri,String localName,String qName, Attributes atts) throws SAXException {}

}
