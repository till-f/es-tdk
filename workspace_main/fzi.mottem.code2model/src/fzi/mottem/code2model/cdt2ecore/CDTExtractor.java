package fzi.mottem.code2model.cdt2ecore;

import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
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
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICContainer;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ISourceRoot;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTTranslationUnit;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import fzi.mottem.code2model.Code2ModelResourceDeltaVisitor;
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
import fzi.util.FileUtils;
import fzi.util.ecore.EcoreUtils;

@SuppressWarnings("restriction")
public class CDTExtractor
{
	private final IResource _cdtResource;
	private final boolean _isRemoved;
	private final String _cdtResourcePath;
	private final ICProject _cproject;
    private final IIndex _cindex;
	
	private boolean _isModelFileDirty = false;
    
    /*
     * Creates a new CDTInterface for the provided project
     */
	public CDTExtractor(IResource cdtResource, boolean isRemoved) throws CoreException
	{
		_cdtResource = cdtResource;
		_isRemoved = isRemoved;
		_cdtResourcePath = cdtResource.getFullPath().toPortableString();
	    _cproject = CoreModel.getDefault().create(cdtResource.getProject());
	    _cindex = CCorePlugin.getIndexManager().getIndex(_cproject);
	}
	
	/*
	 * Refreshes/creates CodeModel files in the given folder
	 */
	public void extractInto(IFolder targetModelFolder)
	{
        ISourceRoot[] sourceRoots;
        
        try
        {
            sourceRoots = _cproject.getAllSourceRoots();
        
	        if (sourceRoots.length <= 0)
	        {
	            System.out.println("Skipped: No source roots");
	        	return;
	        }
	        
			for (ISourceRoot sourceRoot : sourceRoots)
	        {
				ITranslationUnit tu = getTranslationUnit(sourceRoot, _cdtResource);
				
				if (tu != null)
				{
					IFile modelFile = targetModelFolder.getFile(new Path(
							sourceRoot.getPath().toString().substring(1).replace('/', '.') + ".etm-code"
						));

					CodeInstance ci = getCodeModel(modelFile, sourceRoot.getElementName());
					
					_isModelFileDirty = false;

					if (_isRemoved)
					{
						cleanupSourceFile(ci, _cdtResourcePath);
					}
					else
					{
						extractAST(ci, tu);
					}

					if (_isModelFileDirty)
						saveCodeInstance(modelFile, ci);
					
					return;
				}
	        }
			
            System.err.println("Skipped: File '" + _cdtResourcePath + "' (no TranslationUnit)");
        }
        catch (CModelException ex)
        {
            System.err.println("Skipped: File '" + _cdtResourcePath + "' (internal failure)");
            return;
        }
	}
	
	private ITranslationUnit getTranslationUnit(ICContainer rootContainer, IResource resource) throws CModelException
	{
		IPath resourcePath = resource.getFullPath();
		IPath containerPath = rootContainer.getResource().getFullPath();

		if (!containerPath.isPrefixOf(resourcePath))
			return null; 

		IPath relativePath = resourcePath.makeRelativeTo(containerPath);
		
		for (String segment : relativePath.segments())
		{
			String extension = FileUtils.getExtension(segment);
			if (ArrayUtils.contains(Code2ModelResourceDeltaVisitor.C_FILE_EXTENSIONS, extension.toLowerCase()))
			{
				return rootContainer.getTranslationUnit(segment);
			}
			else
			{
				rootContainer = rootContainer.getCContainer(segment);
			}
		}
		
		return null;
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
	
	private void extractAST(CodeInstance ci, ITranslationUnit tu)
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
            return;
        }
        catch (InterruptedException e)
        {
            System.err.println("Skipped: Couldn't acquire C index lock");
            return;
        }
        catch (Exception e)
        {
            System.err.println("Skipped: Unknown failure");
            return;
        }
        
        extractAST(ci, atu);
    }
	
	private void extractAST(CodeInstance ci, IASTTranslationUnit atu)
	{
		if (atu instanceof IASTTranslationUnit ||
			atu instanceof CASTTranslationUnit)
		{
			_isModelFileDirty = true;

			cleanupSourceFile(ci, _cdtResourcePath);
			
			SourceFile modelSrcFile = CodemodelFactory.eINSTANCE.createSourceFile();
			modelSrcFile.setFilePath(_cdtResourcePath);
			ci.getSourceFiles().add(modelSrcFile);

	        for (IASTDeclaration d: atu.getDeclarations())
	        {
	            getChildrenRecursive(modelSrcFile, d);
	        }
		}
		else
		{
			// ATU skipped.
			return;
		}
	}
	
	private void cleanupSourceFile(CodeInstance ci, String filePath)
	{
		SourceFile foundFile = null;
		
		for (SourceFile sf : ci.getSourceFiles())
		{
			if (sf.getFilePath().equals(filePath))
			{
				_isModelFileDirty = true;
				foundFile = sf;
				
				// delete all declarations from this file in the model
				for (SourceCodeLocation scl : sf.getSourceCodeLocations())
				{
					Symbol sym = scl.getSymbol();
					
					if (sym.eContainer() == null)
					{
						continue;
					}
					else if (sym.eContainer() instanceof CodeInstance)
					{
						((CodeInstance)sym.eContainer()).getSymbols().remove(sym);
					}
					else if (sym.eContainer() instanceof Function)
					{
						((Function)sym.eContainer()).getLocalvariables().remove(sym);
					}
					else
					{
						System.err.println("Skipped removing symbol - unexpected container " + sym.eContainer().getClass().getSimpleName());
					}
				}
			}
		}
		
		if (foundFile != null)
		{
			ci.getSourceFiles().remove(foundFile);
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
			// Global Functions

			Function function = CodemodelFactory.eINSTANCE.createFunction();
			addSymbolForDeclaration(srcFile, null, function, node);
			
			// function could not be added (not supported), skip adding children
			if (function.getName() == null)
				return; 

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
		else if (parentFunc == null && ModelUtils.containsElementWithName(srcFile.getCodeInstance().getSymbols(), declarator.getName().toString()))
		{
			// element with duplicate name. should not occur, we simply skip the second occurence.
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
			
			if (type == null)
			{
				// NOTE: Complex types and #typedef types are not supported by PTSpec.
				//       Respective symbold get "void" data type.
				type = getDummyDataType(srcFile.getCodeInstance());
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
				{
					_isModelFileDirty = true;
					parentFunc.getLocalvariables().add(variable);
				}
				else
				{
					_isModelFileDirty = true;
					srcFile.getCodeInstance().getSymbols().add(symbol);
				}
			}
			else if (symbol instanceof Function)
			{
				_isModelFileDirty = true;
				srcFile.getCodeInstance().getSymbols().add(symbol);
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
		DataType existingRefDataType =  ModelUtils.findDataTypeForName(codeInstance, refDataTypeName);	
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
	
	private DataType getDummyDataType(CodeInstance codeInstance)
	{
		String name =  "void";
		DataType existingVoidDataType =  ModelUtils.findDataTypeForName(codeInstance, name);	
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
				DataType existingVoidDataType =  ModelUtils.findDataTypeForName(codeInstance, name);	
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
				DataType existingBooleanDataType = ModelUtils.findDataTypeForName(codeInstance, booleanName);
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
					DataType existingUCharDataType = ModelUtils.findDataTypeForName(codeInstance, uCharName);
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
					DataType charDataType = ModelUtils.findDataTypeForName(codeInstance, charName);		
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
					DataType existingUIntDataType = ModelUtils.findDataTypeForName(codeInstance, uIntName);
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
					DataType existingIntDataType = ModelUtils.findDataTypeForName(codeInstance, intName);
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
					DataType existingUFloatDataType = ModelUtils.findDataTypeForName(codeInstance, floatName);
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
					DataType existingFloatDataType = ModelUtils.findDataTypeForName(codeInstance, floatName);
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
					DataType existingUDoubleDataType = ModelUtils.findDataTypeForName(codeInstance, uDoubleName);
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
					DataType existingDoubleDataType = ModelUtils.findDataTypeForName(codeInstance, doubleName);
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
						DataType existingUShortDataType = ModelUtils.findDataTypeForName(codeInstance, unsignedShort);
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
						DataType existingShortDataType = ModelUtils.findDataTypeForName(codeInstance, kurz);
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
						DataType existingULongDataType = ModelUtils.findDataTypeForName(codeInstance, unsignedLong);
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
						DataType existingLongDataType = ModelUtils.findDataTypeForName(codeInstance, lang);
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
						DataType existingULongLongDataType = ModelUtils.findDataTypeForName(codeInstance, uLongLong);
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
						DataType existingLongLongDataType = ModelUtils.findDataTypeForName(codeInstance, langLang);
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
		Resource res;
		if (ci.eResource() == null)
		{
			URI resourceUri = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);
			ResourceSetImpl resSet = new ResourceSetImpl();
			res = resSet.createResource(resourceUri);
			res.getContents().add(ci);
		}
		else
		{
			res = ci.eResource();
		}

		try
		{
			res.save(null);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
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

