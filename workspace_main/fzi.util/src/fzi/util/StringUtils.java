package fzi.util;

import java.util.Arrays;
import java.util.Collection;

public class StringUtils
{
	
	public static String prependPerLine(String lines, String prefix)
	{
		return lines.replaceAll("(?m)^", prefix);
	}
	
	public static String indentSpaces(String lines, int n)
	{
		char[] indention = new char[n];
		Arrays.fill(indention, ' ');
		return lines.replaceAll("(?m)^", new String(indention));
	}

	public static String indentTabs(String lines, int n)
	{
		char[] indention = new char[n];
		Arrays.fill(indention, '\t');
		return lines.replaceAll("(?m)^", new String(indention));
	}

	public static <T> String serializeCollection(Collection<T> collection, String separator)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int nArgs=0;
		for (Object arg : collection)
		{
			if (nArgs++ == 0)
				stringBuilder.append(arg.toString());
			else
				stringBuilder.append(separator + arg.toString());
		}
		
		return stringBuilder.toString();
	}
	
	public static String getTail(String str, char c, boolean searchLast)
	{
		int i;
		if (searchLast)
			i = str.lastIndexOf(c);
		else
			i = str.indexOf(c);
		
		if (i < 0)
			return str;
		else if (i >= str.length())
			return "";
		else
			return str.substring(i+1, str.length());
	}
	
	public static String getHead(String str, char c, boolean searchLast)
	{
		int i;
		if (searchLast)
			i = str.lastIndexOf(c);
		else
			i = str.indexOf(c);
		
		if (i < 0)
			return "";
		else if (i >= str.length())
			return str;
		else
			return str.substring(0, i);
	}
	
}
