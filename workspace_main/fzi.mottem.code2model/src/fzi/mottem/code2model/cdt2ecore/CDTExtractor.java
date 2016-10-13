package fzi.mottem.code2model.cdt2ecore;

import java.io.File;
import java.io.IOException;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNamedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTPointer;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICContainer;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ISourceRoot;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTTranslationUnit;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import fzi.mottem.model.codemodel.CodeInstance;
import fzi.mottem.model.codemodel.CodemodelFactory;
import fzi.mottem.model.codemodel.DTFloatingPoint;
import fzi.mottem.model.codemodel.DTInteger;
import fzi.mottem.model.codemodel.DTReference;
import fzi.mottem.model.codemodel.DataType;
import fzi.mottem.model.codemodel.Function;
import fzi.mottem.model.codemodel.SourceCodeLocation;
import fzi.mottem.model.codemodel.SourceFile;
import fzi.mottem.model.codemodel.Symbol;
import fzi.mottem.model.codemodel.Variable;
import fzi.mottem.model.util.ModelUtils;
import fzi.util.ecore.EcoreUtils;

@SuppressWarnings("restriction")
public class CDTExtractor
{
	private ICProject _cproject = null;
    private IIndex _cindex = null;
	private IWorkspace _workspace = null;
    
    /*
     * Creates a new CDTInterface for the provided project
     */
	public CDTExtractor(IProject project) throws CoreException
	{
	    _cproject = CoreModel.getDefault().create(project);
	    _cindex = CCorePlugin.getIndexManager().getIndex(_cproject);
	    _workspace = ResourcesPlugin.getWorkspace();
	}
	
	/*
	 * Refreshes/creates .etm-code files for C/C++ projects in the given folder
	 */
	public void refresh(IFolder modelFolder)
	{
        ISourceRoot[] sourceRoots;
        
        try
        {
            sourceRoots = _cproject.getAllSourceRoots();
        }
        catch (Exception ex)
        {
            System.err.println("Skipped: Could not get source roots");
            return;
        }
        
        if (sourceRoots.length <= 0)
        {
            System.out.println("Skipped: No source roots");
        	return;
        }
        
		for (ISourceRoot sourceRoot : sourceRoots)
        {
			if (sourceRoot.getElementType() == ICElement.C_CCONTAINER)
			{
				handleCContainer(modelFolder, sourceRoot);
				continue;
			}
        }
		
	}
	
	private void handleCContainer(IFolder modelFolder, ICContainer cContainer)
	{
		IFile modelFile = modelFolder.getFile(new Path(
				cContainer.getPath().toString().substring(1).replace('/', '.') + ".etm-code"
			));
	
		CodeInstance ci = getCodeModel(modelFile, cContainer.getElementName());
		iterateCContainer(ci, cContainer);
		
		if (!ci.getSymbols().isEmpty())
			saveCodeInstance(modelFile, ci);
	}
	
	private CodeInstance getCodeModel(IFile modelFile, String defaultName)
	{
		String defaultCodeInstanceName = "Pkg" + defaultName.replaceAll("[^a-zA-Z0-9_]", "_");
		
		CodeInstance codeInstance;
		if (modelFile.exists())
		{
			URI codeInstanceURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);
			try
			{
				codeInstance = (CodeInstance)EcoreUtils.loadFullEMFModel(codeInstanceURI);
				
				if (!_cproject.getProject().getName().equals(codeInstance.getCProjectName()))
					throw new IOException("Corrupt CodeModel file: " + modelFile.getFullPath().toString());
				
				ModelUtils.clearCodeInstance(codeInstance);
				return codeInstance;
			}
			catch (IOException e)
			{
				System.err.println("Could not load existing CodeModel from file");
				e.printStackTrace();
			}
		}
		
		codeInstance = CodemodelFactory.eINSTANCE.createCodeInstance();
		codeInstance.setName(defaultCodeInstanceName);
		codeInstance.setCProjectName(_cproject.getProject().getName());
		
		return codeInstance;
	}
	
	private void iterateCContainer(CodeInstance ci, ICContainer ccontainer)
	{
		try
		{
			iterateTUs(ci, ccontainer.getTranslationUnits());
		}
		catch (CModelException e1)
		{
			e1.printStackTrace();
		}

		try
		{
			ICContainer ccontainers[] = ccontainer.getCContainers();

	    	for (ICContainer childCContainer : ccontainers)
	    	{
	    		iterateCContainer(ci, childCContainer);
	    	}
		}
		catch (CModelException e2)
		{
			e2.printStackTrace();
		}
	}

	private void iterateTUs(CodeInstance ci, ITranslationUnit tus[])
	{
        for (ITranslationUnit tu : tus)
        {
            IASTTranslationUnit atu;
                            
            try
            {
                _cindex.acquireReadLock();
                atu = tu.getAST(_cindex, ITranslationUnit.AST_SKIP_INDEXED_HEADERS);
                _cindex.releaseReadLock();
            } 
            catch (CoreException e)
            {
                System.err.println("Skipped: No AST available");
                continue;
            }
            catch (InterruptedException e)
            {
                System.err.println("Skipped: Couldn't acquire index lock");
                continue;
            }
            catch (Exception e)
            {
                System.err.println("Skipped: Unknown failure");
                continue;
            }
            extractASTInfo(ci, atu);
        }
    }
	
	private void extractASTInfo(CodeInstance ci, IASTTranslationUnit atu)
	{
		File workspaceDirectory = _workspace.getRoot().getLocation().toFile();
		
		SourceFile modelSrcFile;
		
		if (atu instanceof IASTTranslationUnit ||
			atu instanceof CASTTranslationUnit)
		{
			File srcFile = new File (atu.getFilePath());
	
			String filePath = srcFile.getAbsolutePath();
		    
			String workspacePath = workspaceDirectory.getAbsolutePath();
			
			if (filePath.startsWith(workspacePath))
			{
				String relativePath = filePath.substring(workspacePath.length() + 1);
				
				modelSrcFile = CodemodelFactory.eINSTANCE.createSourceFile();	
				modelSrcFile.setFilePath(relativePath); 
				ci.getSourceFiles().add(modelSrcFile);
			}
			else
			{
				return;
			}
		}
		else
		{
			// ATU skipped.
			return;
		}
		
		for (IASTPreprocessorIncludeStatement inc: atu.getIncludeDirectives())
        {
            getChildrenRecursive(modelSrcFile, inc);
        }
		
        for (IASTDeclaration d: atu.getDeclarations())
        {
            getChildrenRecursive(modelSrcFile, d);
        }
	}

	private void getChildrenRecursive(SourceFile srcFile, IASTNode node)
	{
		if (node instanceof IASTSimpleDeclaration)
		{
			// Global Variables
			
			addSymbolForDeclaration(srcFile, null, null, node);
			
			return;
		}
		else if (node instanceof IASTFunctionDefinition)
		{
			Function function = CodemodelFactory.eINSTANCE.createFunction();

			// Global Functions
			addSymbolForDeclaration(srcFile, null, function, node);

			for (IASTNode child : node.getChildren())
			{
				getChildrenRecursiveFunctionBody(srcFile, function, child);
			}
						
			return;
		}
		
		for (IASTNode subNode : node.getChildren())
		{
			getChildrenRecursive(srcFile, subNode);			
		}
	}
	
	private void getChildrenRecursiveFunctionBody(SourceFile srcFile, Function parentFunc, IASTNode node)
	{
		if (node instanceof IASTSimpleDeclaration)
		{		
			// Local Variables
			
			addSymbolForDeclaration(srcFile, parentFunc, null, node);
			
			return;
		}
		else if (node instanceof IASTFunctionDeclarator) 
		{
			// Parameter Variables

			IASTFunctionDeclarator funcDecl = (IASTFunctionDeclarator) node;
			
			for (IASTNode childNode : funcDecl.getChildren())
			{
				if (childNode instanceof IASTParameterDeclaration)
				{
					addSymbolForDeclaration(srcFile, parentFunc, null, childNode); 
				}
			}
			
			return;
		}
		
		for (IASTNode subsubNode : node.getChildren())
		{
			getChildrenRecursiveFunctionBody(srcFile, parentFunc, subsubNode);
		}
	}
	
	private void addSymbolForDeclaration(SourceFile srcFile, Function parentFunc, Function selfFunc, IASTNode node)
	{
		if (!(node instanceof IASTParameterDeclaration ||
			 node instanceof IASTSimpleDeclaration ||
			 node instanceof IASTFunctionDefinition))
		{
			throw new RuntimeException("Invalid arguments for addVariableForSimpleDeclaration(): decl");
		}
		
		IASTNode[] declChildren = node.getChildren();

		/* !TODO: improve CDT2Ecore
		 * Currently the algorithm expects two children (IASTDeclSpecifier and IASTDeclarator).
		 * More complex declarations only have one child (IASTCompositeTypeSpecifier). The 
		 * following implementations should be adapted to support this.
		 */
		
		if (declChildren.length < 2 ||
			!(declChildren[0] instanceof IASTDeclSpecifier) ||
			!(declChildren[1] instanceof IASTDeclarator))
		{
			System.err.println("Skipped: unsupported structure of IASTSimpleDeclaration");
			return;
		}
			
		IASTDeclSpecifier declSpecifier = (IASTDeclSpecifier) declChildren[0];
		
		IASTDeclarator declarator = null;

		for (int i=1; i<declChildren.length; i++)
		{
			if (declChildren[i] instanceof IASTDeclarator)
			{
				declarator = (IASTDeclarator) declChildren[i];
				Symbol symbol = selfFunc != null ? selfFunc : CodemodelFactory.eINSTANCE.createVariable();
				addSymbolForDeclaration(srcFile, parentFunc, symbol, node, declSpecifier, declarator);
			}
		}
	}
	
	private void addSymbolForDeclaration(SourceFile srcFile, Function parentFunc, Symbol symbol, IASTNode declNode, IASTDeclSpecifier declSpecifier, IASTDeclarator declarator)
	{
		if (declarator.getName().toString().isEmpty())
		{
			// name of declarator is empty?! We don't want such elements...
			return;
		}
		else if (declarator instanceof IASTFunctionDeclarator && !(symbol instanceof Function))
		{
			// skip function declarators not found in the context of a function definition
			// (we are only interested in functions that are actually implemented)
			return;
		}
		else
		{
			DataType type = null;
			if (declSpecifier instanceof IASTSimpleDeclSpecifier)
			{
				type = getPrimitiveDataType(srcFile.getCodeInstance(), (IASTSimpleDeclSpecifier)declSpecifier);
			}
//			else if (declSpecifier instanceof CASTTypedefNameSpecifier)
//			{
//				String sig1 = declarator.getRawSignature();
//				String sig2 = declSpecifier.getRawSignature();
//				System.out.println("DEBUG: " + sig1 + " -- " + sig2);
//			}
			
			// NOTE: Complex types and #typedef types are not supported by PTSpec.
			//       Respective variables cannot be referenced.
			if (type == null)
			{
				return;
			}

			IASTFileLocation cdtLoc = declarator.getName().getFileLocation();
			int globalVariableOffset = cdtLoc.getNodeOffset();
			int globalVariableLineNumber = cdtLoc.getStartingLineNumber();
			int globalVariableLength = cdtLoc.getNodeLength();
				
			SourceCodeLocation modelLoc = CodemodelFactory.eINSTANCE.createSourceCodeLocation();
			modelLoc.setSourceFile(srcFile);
			modelLoc.setLength(globalVariableLength);
			modelLoc.setLineNumber(globalVariableLineNumber);
			modelLoc.setOffset(globalVariableOffset);

			symbol.setName(declarator.getName().toString());
			symbol.setDeclarationLocation(modelLoc);
			symbol.setDataType(type);
			
			if (symbol instanceof Variable)
			{
				Variable variable = (Variable)symbol;
				variable.setFunctionParameter(declNode instanceof IASTParameterDeclaration);
				if (parentFunc != null)
					parentFunc.getLocalvariables().add(variable);
				else
					addSymbol(srcFile.getCodeInstance(), variable);
			}
			else if (symbol instanceof Function)
			{
				addSymbol(srcFile.getCodeInstance(), symbol);
			}
			else
			{
				throw new RuntimeException("Invalid arguments for addVariableForSimpleDeclaration(): symbol");
			}
			
			if (declarator.getChildren()[0] instanceof IASTName)
			{
				// everything's done, it seems to be a "regular" declaration (no pointer or anything else)
				return;
			}
			if (declarator.getChildren()[0] instanceof IASTPointer)
			{	
				if (declarator.getChildren()[1] instanceof IASTName)
				{
					DataType pointerReference = getReferenceDataType(srcFile.getCodeInstance(), type, type.getName());
					symbol.setDataType(pointerReference);				
				}
				else if (declarator.getChildren()[1] instanceof IASTPointer)
				{
					int pointersCounter = 0;
					
					for (IASTNode subPointer : declarator.getChildren())	
					{
						if (subPointer instanceof IASTPointer)
						{
							pointersCounter++;
							DataType pointerToReference = getReferenceToReferenceDataType(subPointer, srcFile.getCodeInstance(), getReferenceDataType(srcFile.getCodeInstance(), type, type.getName()), type.getName(), pointersCounter - 1);
							symbol.setDataType(pointerToReference);
						}
					}						
					
				}
				else
				{
					System.err.println("IASTPointer type not supported: " + declarator.getChildren()[1].getClass().getSimpleName());
				}						
			}
			else
			{
				System.err.println("IASTDeclarator type not supported: " + declarator.getChildren()[0].getClass().getSimpleName());
			}
		}
	}
	
	private void addSymbol(CodeInstance codeInstance, Symbol symbol)
	{
		// renaming of duplicates
		int idx = 1;
		String originalName = symbol.getName();
		
		while (ModelUtils.containsElementWithName(codeInstance.getSymbols(), symbol.getName()))
		{
			symbol.setName(originalName + "_" + idx);
		}
		
		codeInstance.getSymbols().add(symbol);
		
//		if (_elfSymoblsReader != null)
//		{
//			Long address = _elfSymoblsReader.getAddress(symbol.getName());
//
//			if (address != null)
//			{
//				BinaryLocation bLoc = CodemodelFactory.eINSTANCE.createBinaryLocation();
//				bLoc.setAddress(address);
//				symbol.setBinaryLocation(bLoc);
//			}
//		}
	}

	private DataType findDataType(CodeInstance ci, String name)
	{
		if (ci == null)
			throw new RuntimeException("CodeInstance Is Not Supossed To Be Null");
		
		for(DataType dt : ci.getDataTypes())
		{
			if (dt.getName() == null)
			{
				System.err.println("Error Getting DataType Name");
				continue;
			}
			
			if (dt.getName().equals(name))
			{
				return dt;
			}	
		}
		
		return null;
	}
	
	private DataType getReferenceToReferenceDataType(IASTNode subNode, CodeInstance codeInstance, DataType pointedDataType, String name, int numberOfPointers)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < numberOfPointers; i++)
		{
			stringBuilder.append("Ref_");
		}
		String refRefDataTypeName = stringBuilder + name;
		DataType existingRefRefDataType =  getReferenceDataType(codeInstance, pointedDataType, refRefDataTypeName);
		if (existingRefRefDataType == null)
		{
			existingRefRefDataType =  CodemodelFactory.eINSTANCE.createDTReference();
			existingRefRefDataType.setName(refRefDataTypeName);
			((DTReference)existingRefRefDataType).setTargetType(pointedDataType);
			codeInstance.getDataTypes().add(existingRefRefDataType);
			return existingRefRefDataType;	
		}	
				
		return existingRefRefDataType;

	}
	
	private DataType getReferenceDataType(CodeInstance codeInstance, DataType pointedDataType, String name)
	{	
		String refDataTypeName = "Ref_" + name;
		DataType existingRefDataType =  findDataType(codeInstance, refDataTypeName);	
		if (existingRefDataType == null)
		{
			existingRefDataType =  CodemodelFactory.eINSTANCE.createDTReference();
			existingRefDataType.setName(refDataTypeName);
			((DTReference)existingRefDataType).setTargetType(pointedDataType);
			codeInstance.getDataTypes().add(existingRefDataType);
			return existingRefDataType;						
		}	
		else
		{
			return existingRefDataType;
		}
	}
		
	private DataType getPrimitiveDataType(CodeInstance codeInstance, IASTSimpleDeclSpecifier cdtDataType)
	{
		if (cdtDataType instanceof IASTNamedTypeSpecifier ||
			cdtDataType.getStorageClass() == IASTDeclSpecifier.sc_typedef ||
			cdtDataType.getStorageClass() == IASTDeclSpecifier.sc_extern)
		{
			// type declarations and externals are not supposed to be included in model
			return null;
		}
		
		switch(cdtDataType.getType())
		{
			case IASTSimpleDeclSpecifier.t_void:
				String name =  "void";
				DataType existingVoidDataType =  findDataType(codeInstance, name);	
				if (existingVoidDataType == null)
				{
					existingVoidDataType = CodemodelFactory.eINSTANCE.createDTVoid();
					existingVoidDataType.setName(name);
					codeInstance.getDataTypes().add(existingVoidDataType);
					return existingVoidDataType;
				}
				else
				{
					return existingVoidDataType;
				}
			case IASTSimpleDeclSpecifier.t_bool:
				String booleanName = "bool";
				DataType existingBooleanDataType = findDataType(codeInstance, booleanName);
				if(existingBooleanDataType == null)
				{
					existingBooleanDataType = CodemodelFactory.eINSTANCE.createDTInteger();
					existingBooleanDataType.setName(booleanName);
					codeInstance.getDataTypes().add(existingBooleanDataType);
					return existingBooleanDataType;
				}
				else
				{
					return existingBooleanDataType;
				}
				
			case IASTSimpleDeclSpecifier.t_char:
				if (cdtDataType.isUnsigned() == true)
				{
					String uCharName = "uint8";
					DataType existingUCharDataType = findDataType(codeInstance, uCharName);
					if (existingUCharDataType == null)
					{
						existingUCharDataType =  CodemodelFactory.eINSTANCE.createDTInteger();
						existingUCharDataType.setName(uCharName);
						((DTInteger)existingUCharDataType).setBitSize(8);
						((DTInteger)existingUCharDataType).setIsSigned(false);
						codeInstance.getDataTypes().add(existingUCharDataType);
						return existingUCharDataType;
					}
					else
					{
						return existingUCharDataType;
					}
				}
				else
				{
					String charName = "char";
					DataType charDataType = findDataType(codeInstance, charName);		
					if (charDataType == null)
					{
						charDataType =  CodemodelFactory.eINSTANCE.createDTInteger();
						charDataType.setName(charName);
						((DTInteger)charDataType).setBitSize(8);
						((DTInteger)charDataType).setIsSigned(true);
						codeInstance.getDataTypes().add(charDataType);
						return charDataType;
					}
					else 
					{
						return charDataType;
					}
				}
			case IASTSimpleDeclSpecifier.t_int:
				if (cdtDataType.isUnsigned() == true)
				{
					String uIntName = "uint32";
					DataType existingUIntDataType = findDataType(codeInstance, uIntName);
					if (existingUIntDataType == null)
					{
						existingUIntDataType = CodemodelFactory.eINSTANCE.createDTInteger();
						existingUIntDataType.setName(uIntName);
						((DTInteger)existingUIntDataType).setBitSize(32);
						((DTInteger)existingUIntDataType).setIsSigned(false);
						codeInstance.getDataTypes().add(existingUIntDataType);
						return existingUIntDataType;
					}
					else
					{
						return existingUIntDataType;
					} 
				}
				else
				{
					String intName = "int32";
					DataType existingIntDataType = findDataType(codeInstance, intName);
					if (existingIntDataType == null)
					{
						existingIntDataType = CodemodelFactory.eINSTANCE.createDTInteger();
						existingIntDataType.setName(intName);
						((DTInteger)existingIntDataType).setBitSize(32);
						((DTInteger)existingIntDataType).setIsSigned(true);
						codeInstance.getDataTypes().add(existingIntDataType);
						return existingIntDataType;
					}
					else
					{
						return existingIntDataType;
					}
				}	
			case IASTSimpleDeclSpecifier.t_float:
				if (cdtDataType.isUnsigned() == true)
				{
					String floatName = "unsigned float";
					DataType existingUFloatDataType = findDataType(codeInstance, floatName);
					if (existingUFloatDataType == null)
					{
						existingUFloatDataType = CodemodelFactory.eINSTANCE.createDTFloatingPoint();
						existingUFloatDataType.setName(floatName);
						((DTFloatingPoint)existingUFloatDataType).setExponentBitSize(8);
						((DTFloatingPoint)existingUFloatDataType).setSignificandBitSize(23);
						codeInstance.getDataTypes().add(existingUFloatDataType);
						return existingUFloatDataType;
					}
					else
					{
						return existingUFloatDataType;
					}
				}
				else
				{
					String floatName = "float";
					DataType existingFloatDataType = findDataType(codeInstance, floatName);
					if (existingFloatDataType == null)
					{
						existingFloatDataType = CodemodelFactory.eINSTANCE.createDTFloatingPoint();
						existingFloatDataType.setName(floatName);
						((DTFloatingPoint)existingFloatDataType).setExponentBitSize(8);
						((DTFloatingPoint)existingFloatDataType).setSignificandBitSize(23);
						codeInstance.getDataTypes().add(existingFloatDataType);
						return existingFloatDataType;
					}
					else
					{
						return existingFloatDataType;
					}
				}

			case IASTSimpleDeclSpecifier.t_double:
				if (cdtDataType.isUnsigned() == true)
				{
					String uDoubleName = "unsigned double";
					DataType existingUDoubleDataType = findDataType(codeInstance, uDoubleName);
					if (existingUDoubleDataType == null)
					{
						existingUDoubleDataType = CodemodelFactory.eINSTANCE.createDTFloatingPoint();
						existingUDoubleDataType.setName(uDoubleName);
						((DTFloatingPoint)existingUDoubleDataType).setExponentBitSize(11);
						((DTFloatingPoint)existingUDoubleDataType).setSignificandBitSize(52);
						codeInstance.getDataTypes().add(existingUDoubleDataType);
						return existingUDoubleDataType;
					}
					else
					{
						return existingUDoubleDataType;
					}
				}
				else
				{
					String doubleName = "double";
					DataType existingDoubleDataType = findDataType(codeInstance, doubleName);
					if (existingDoubleDataType == null)
					{
						existingDoubleDataType = CodemodelFactory.eINSTANCE.createDTFloatingPoint();
						existingDoubleDataType.setName(doubleName);
						((DTFloatingPoint)existingDoubleDataType).setExponentBitSize(11);
						((DTFloatingPoint)existingDoubleDataType).setSignificandBitSize(52);
						codeInstance.getDataTypes().add(existingDoubleDataType);
						return existingDoubleDataType;
					}
					else
					{
						return existingDoubleDataType;
					}
				}
			case IASTSimpleDeclSpecifier.t_unspecified:
				if (cdtDataType.isShort() == true)
				{	
					if (cdtDataType.isUnsigned() == true)
					{
						String unsignedShort = "uint16";
						DataType existingUShortDataType = findDataType(codeInstance, unsignedShort);
						if (existingUShortDataType == null)
						{
							existingUShortDataType = CodemodelFactory.eINSTANCE.createDTInteger();
							existingUShortDataType.setName(unsignedShort);
							((DTInteger)existingUShortDataType).setBitSize(16);
							((DTInteger)existingUShortDataType).setIsSigned(false);
							codeInstance.getDataTypes().add(existingUShortDataType);
							return existingUShortDataType;
						}
						else
						{
							return existingUShortDataType;
						}

					}
					else
					{
						String kurz = "short";
						DataType existingShortDataType = findDataType(codeInstance, kurz);
						if (existingShortDataType == null)
						{
							existingShortDataType = CodemodelFactory.eINSTANCE.createDTInteger();
							existingShortDataType.setName(kurz);
							((DTInteger)existingShortDataType).setBitSize(16);
							((DTInteger)existingShortDataType).setIsSigned(true);
							codeInstance.getDataTypes().add(existingShortDataType);
							return existingShortDataType;
						}
						else
						{
							return existingShortDataType;
						}
					}
				}
				
				if (cdtDataType.isLong() == true)
				{
					if (cdtDataType.isUnsigned() == true)
					{
						String unsignedLong = "uint32";
						DataType existingULongDataType = findDataType(codeInstance, unsignedLong);
						if (existingULongDataType == null)
						{
							existingULongDataType = CodemodelFactory.eINSTANCE.createDTInteger();
							existingULongDataType.setName(unsignedLong);
							((DTInteger)existingULongDataType).setBitSize(32);
							((DTInteger)existingULongDataType).setIsSigned(false);
							codeInstance.getDataTypes().add(existingULongDataType);
							return existingULongDataType;
						}
						else
						{
							return existingULongDataType;
						}

					}
					else
					{
						String lang = "int32";
						DataType existingLongDataType = findDataType(codeInstance, lang);
						if (existingLongDataType == null)
						{
							existingLongDataType = CodemodelFactory.eINSTANCE.createDTInteger();
							existingLongDataType.setName(lang);
							((DTInteger)existingLongDataType).setBitSize(32);
							((DTInteger)existingLongDataType).setIsSigned(true);
							codeInstance.getDataTypes().add(existingLongDataType);
							return existingLongDataType;
						}
						else
						{
							return existingLongDataType;
						}
					}
				}
				
				if (cdtDataType.isLongLong() == true)
				{
					if (cdtDataType.isUnsigned() == true)
					{
						String uLongLong = "uint64";
						DataType existingULongLongDataType = findDataType(codeInstance, uLongLong);
						if (existingULongLongDataType == null)
						{
							existingULongLongDataType = CodemodelFactory.eINSTANCE.createDTInteger();
							existingULongLongDataType.setName(uLongLong);
							((DTInteger)existingULongLongDataType).setBitSize(64);
							((DTInteger)existingULongLongDataType).setIsSigned(false);
							codeInstance.getDataTypes().add(existingULongLongDataType);
							return existingULongLongDataType;
						}
						else
						{
							return existingULongLongDataType;
						}
					}
					else
					{
						String langLang = "int64";
						DataType existingLongLongDataType = findDataType(codeInstance, langLang);
						if (existingLongLongDataType == null)
						{
							existingLongLongDataType = CodemodelFactory.eINSTANCE.createDTInteger();
							existingLongLongDataType.setName(langLang);
							((DTInteger)existingLongLongDataType).setBitSize(64);
							((DTInteger)existingLongLongDataType).setIsSigned(true);
							codeInstance.getDataTypes().add(existingLongLongDataType);
							return existingLongLongDataType;
						}
						else
						{
							return existingLongLongDataType;
						}
					}
				}		
		}
		
		return null;
	}
	
	private void saveCodeInstance(IFile modelFile, CodeInstance ci)
	{
		URI resourceUri = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);
		ResourceSetImpl resSet = new ResourceSetImpl();
		Resource res = resSet.createResource(resourceUri);
		res.getContents().add(ci);

		try
		{
			res.save(null);
		}
		catch (IOException e)
		{
			// ignore silently
		}
		return;
	}
	
	@SuppressWarnings("unused")
	private void printNodeInfo(IASTNode node, int level)
	{
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<level; i++)
		{
			sb.append(" ");
		}
		
		for (IASTNode n : node.getChildren())
		{
			System.out.println(sb.toString() + n.getRawSignature());
			printNodeInfo(n, level + 1);
		}
	}
}

