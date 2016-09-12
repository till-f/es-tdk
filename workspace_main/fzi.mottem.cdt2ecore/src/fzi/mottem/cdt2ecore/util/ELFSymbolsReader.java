package fzi.mottem.cdt2ecore.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;

import fzi.util.eclipse.IntegrationUtils;
import fzi.util.ecore.EcoreUtils;

public class ELFSymbolsReader
{
	private final File _symbolsFile;
	private final HashMap<String, Long> _symbolNameToAddressMap = new HashMap<String, Long>();
	
	public ELFSymbolsReader(URI symbolsFileURI) throws IOException
	{
		IPath path = EcoreUtils.getPathForEMFURI(symbolsFileURI);
		IPath systemPath = IntegrationUtils.getSystemPathForWorkspaceRelativePath(path);
		
		_symbolsFile = new File(systemPath.toOSString());

		checkFile();
		
		parseFile();
	}

	private void checkFile() throws IOException
	{
		if (!_symbolsFile.exists() || !_symbolsFile.isFile())
			throw new IOException("No valid ELF file");

		// !TODO: check for correct format
	}

	Long getAddress(String symbolName)
	{
		if (_symbolNameToAddressMap.containsKey(symbolName))
		{
			return _symbolNameToAddressMap.get(symbolName);
		}
		else
		{
			System.out.println("Address Lookup failed for '" + symbolName + "'");
			return null;
		}
	}

	private void parseFile()
	{
		BufferedReader reader = null;
		String key, symbolName = null, line;
		Long symbolAddress;
		try
		{
			reader = new BufferedReader(new FileReader(_symbolsFile));
			int index = 0;

			while ((line = reader.readLine()) != null)
			{
				String found = index + ":";
				if (line.contains(found))
				{

					String[] elfLine = line.split("\\s+");

					key = elfLine[2];
					symbolName = elfLine[elfLine.length - 1];

					key.replaceAll("\\s+", "");
					symbolName.replaceAll("\\s+", "");
					symbolAddress = Long.parseLong(key,16);
					
					_symbolNameToAddressMap.put(symbolName, symbolAddress);
					index++;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
