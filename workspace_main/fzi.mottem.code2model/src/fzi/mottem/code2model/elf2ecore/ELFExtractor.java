package fzi.mottem.code2model.elf2ecore;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import com.bicirikdwarf.dwarf.Dwarf32Context;
import com.bicirikdwarf.elf.Elf32Context;
import com.bicirikdwarf.emf.DwarfModelFactory;
import com.bicirikdwarf.emf.dwarf.BaseType;
import com.bicirikdwarf.emf.dwarf.DwarfModel;
import com.bicirikdwarf.emf.dwarf.Subprogram;

import fzi.mottem.model.codemodel.BinaryLocation;
import fzi.mottem.model.codemodel.CodeInstance;
import fzi.mottem.model.codemodel.CodemodelFactory;
import fzi.mottem.model.codemodel.DataType;
import fzi.mottem.model.codemodel.Function;
import fzi.mottem.model.codemodel.Variable;
import fzi.mottem.model.util.ModelUtils;
import fzi.util.eclipse.IntegrationUtils;

public class ELFExtractor
{
	private DwarfModel _dwarfModel = null;
	
	public ELFExtractor(IResource elfResource) throws IOException
	{
		String absoluteElfPathStr = IntegrationUtils.getStringSystemPathForWorkspaceRelativePath(elfResource.getFullPath());
		//File elfFile = new File(absoluteElfPathStr);

		RandomAccessFile elfRandomAccessFile = null;
		FileChannel elfFileChannel = null;
		try
		{
			elfRandomAccessFile = new RandomAccessFile(absoluteElfPathStr, "r");
			elfFileChannel = elfRandomAccessFile.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int)(elfFileChannel.size()));
			elfFileChannel.read(buffer);
			buffer.flip();
	    	Elf32Context elfContext = new Elf32Context(buffer);
	    	Dwarf32Context dwarfContext = new Dwarf32Context(elfContext);
	    	_dwarfModel = DwarfModelFactory.createModel(dwarfContext);
		}
		finally
		{
			try {
				elfFileChannel.close();
				elfRandomAccessFile.close();
			} catch (IOException e1) {
				return;
			}
		}
	}

	public void extractInto(CodeInstance ci)
	{
    	TreeIterator<EObject> iterator = _dwarfModel.eAllContents();
		while (iterator.hasNext())
		{
			EObject eObj = iterator.next();
			if (eObj instanceof com.bicirikdwarf.emf.dwarf.Variable)
			{
				com.bicirikdwarf.emf.dwarf.Variable dmVar = (com.bicirikdwarf.emf.dwarf.Variable) eObj;
				String name = dmVar.getName();
				Integer address = dmVar.getAddress();
				BaseType baseType = DwarfModelUtils.getBaseType(dmVar.getType());

				Variable ciVar = (Variable) ModelUtils.getGlobalSymbol(ci, name);
				
				if (ciVar == null)
				{
					String typeName = DwarfModelUtils.getTypeName(baseType);
					DataType dt = ModelUtils.findDataTypeForNameOrCreateDefault(ci, typeName);
					if (dt == null) continue;
					
					ciVar = CodemodelFactory.eINSTANCE.createVariable();
					ciVar.setName(name);
					ciVar.setDataType(dt);
					ci.getSymbols().add(ciVar);
				}
				
				BinaryLocation loc = CodemodelFactory.eINSTANCE.createBinaryLocation();
				loc.setAddress(address.longValue());
				ciVar.setBinaryLocation(loc);
			}
			else if(eObj instanceof Subprogram)
			{
				Subprogram dmFunc = (Subprogram) eObj;
				String name = dmFunc.getName();
				Integer address = dmFunc.getAddress();
				BaseType baseType = DwarfModelUtils.getBaseType(dmFunc.getReturnType());

				Function ciFunc = (Function) ModelUtils.getGlobalSymbol(ci, name);
				
				if (ciFunc == null)
				{
					String typeName = DwarfModelUtils.getTypeName(baseType);
					DataType dt = ModelUtils.findDataTypeForNameOrCreateDefault(ci, typeName);
					if (dt == null) continue;

					ciFunc = CodemodelFactory.eINSTANCE.createFunction();
					ciFunc.setName(name);
					ciFunc.setDataType(dt);
					ci.getSymbols().add(ciFunc);
				}

				BinaryLocation loc = CodemodelFactory.eINSTANCE.createBinaryLocation();
				loc.setAddress(address.longValue());
				ciFunc.setBinaryLocation(loc);
			}
		}
		
		try
		{
			ci.eResource().save(null);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
