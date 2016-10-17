package fzi.mottem.code2model.elf2ecore;

import com.bicirikdwarf.emf.dwarf.BaseType;
import com.bicirikdwarf.emf.dwarf.Type;
import com.bicirikdwarf.emf.dwarf.Typedef;

public class DwarfModelUtils
{

	public static BaseType getBaseType(Type type) 
	{
		if (type instanceof BaseType)
			return (BaseType)type;
		
		if (type instanceof Typedef)
			return getBaseType(((Typedef) type).getType());
					
		return null;
	}
	
	public static String getTypeName(BaseType type)
	{
		if (type == null)
		{
			return "unknown";
		}
		else if (type.getName() == null)
		{
			return "void";
		}
		else if (type.getName().contains("int") || type.getName().contains("char"))
		{
			String unsignedPrefix = "";
			if (type.getName().startsWith("unsigned"))
				unsignedPrefix += "u";
			
			return unsignedPrefix + "int" + String.valueOf(type.getByteSize() * 8);
		}
		else if (type.getName().contains("float"))
		{
			return "float";
		}
		else if (type.getName().contains("double"))
		{
			return "double";
		}
		else
		{
			return "unknown";
		}
	}

}
