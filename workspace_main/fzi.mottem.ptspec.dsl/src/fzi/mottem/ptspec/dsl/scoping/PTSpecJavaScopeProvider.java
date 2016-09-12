package fzi.mottem.ptspec.dsl.scoping;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.xtext.TypesAwareDefaultGlobalScopeProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;

import com.google.common.base.Function;

import fzi.mottem.ptspec.dsl.pTSpec.PTSJavaImport;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;

@SuppressWarnings("restriction")
public class PTSpecJavaScopeProvider extends TypesAwareDefaultGlobalScopeProvider
{
	//@Inject
	//private JdtTypeProviderFactory _jdtTypeProviderFatory;
	
	private static final Function<JvmIdentifiableElement, QualifiedName> _jvmIdentifiableElement2SimpleName = new Function<JvmIdentifiableElement, QualifiedName>() 
	{
 		@Override
		public QualifiedName apply(JvmIdentifiableElement input)
 		{
 			if (input.getSimpleName() == null)
 				return null;
 			return QualifiedName.create(input.getSimpleName());
		}
	};
	
	public IScope getPTSpecJavaScope(PTSRoot root)
	{
		List<JvmIdentifiableElement> scopeElements = new LinkedList<JvmIdentifiableElement>();
		
		for (PTSJavaImport jImport : root.getImports())
		{
			JvmType type;
			if (jImport.getImportedType().eIsProxy())
			{
				type = jImport.getImportedType();
			}
			else
			{
				type = jImport.getImportedType();
			}
			
			scopeElements.add(type);
		}
		
		return Scopes.scopeFor(scopeElements, _jvmIdentifiableElement2SimpleName, IScope.NULLSCOPE);
	}
	
	public IScope calculateScopeForStaticMembers(JvmIdentifiableElement jElement)
	{
		List<JvmIdentifiableElement> scopeElements = new LinkedList<JvmIdentifiableElement>();
		
		boolean nonStatic = false;
		
		// if jElement is a field, we already have a reference to an object, thus we can also access non-static members
		if (jElement instanceof JvmField)
		{
			JvmField field = (JvmField)jElement;
			jElement = field.getType().getType();
			nonStatic = true;
		}
		
		for (EObject eObj : jElement.eContents())
		{
			if (eObj instanceof JvmField || eObj instanceof JvmOperation)
			{
				JvmFeature feature = (JvmFeature)eObj;
				if (feature.getVisibility() == JvmVisibility.PUBLIC &&
					(feature.isStatic() || nonStatic))
				{
					scopeElements.add(feature);
				}
			}
		}
		
		return Scopes.scopeFor(scopeElements, _jvmIdentifiableElement2SimpleName, IScope.NULLSCOPE);
	}
	
	public void addNonStaticMembers(JvmIdentifiableElement jElement, LinkedList<JvmIdentifiableElement> scopeElements)
	{
		if (!(jElement instanceof JvmGenericType))
			return;
		
		JvmGenericType startType = (JvmGenericType)jElement;
		
		if (startType.getExtendedClass() != null)
		{
			addNonStaticMembers(startType.getExtendedClass().getType(), scopeElements);
		}
		
		for (JvmTypeReference interfacRef : startType.getExtendedInterfaces())
		{
			addNonStaticMembers(interfacRef.getType(), scopeElements);
		}
		
		for (EObject eObj : startType.eContents())
		{
			if (eObj instanceof JvmField || eObj instanceof JvmOperation)
			{
				JvmFeature feature = (JvmFeature)eObj;
				if (feature.getVisibility() == JvmVisibility.PUBLIC &&
					(!feature.isStatic()))
				{
					scopeElements.add(feature);
				}
			}
		}

	}
	
	public IScope scopeFor(LinkedList<JvmIdentifiableElement> scopeElements)
	{
		return Scopes.scopeFor(scopeElements, _jvmIdentifiableElement2SimpleName, IScope.NULLSCOPE);
	}
}
