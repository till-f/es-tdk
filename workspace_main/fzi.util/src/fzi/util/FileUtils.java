package fzi.util;

import java.io.File;

public class FileUtils
{
	
	public static String getExtension(File file)
	{
		if (file.isDirectory())
			return "";
	
		String fileName = file.getName();
		int idx = fileName.lastIndexOf('.');
		if (idx >= 0)
			return fileName.substring(idx+1);
		else
			return "";
	}

	public static String getNameWithoutExtension(File file)
	{
		if (file.isDirectory())
			return "";
	
		String fileName = file.getName();
		int idx = fileName.lastIndexOf('.');
		if (idx > 0)
			return fileName.substring(0, idx);
		else
			return "";
	}
}
