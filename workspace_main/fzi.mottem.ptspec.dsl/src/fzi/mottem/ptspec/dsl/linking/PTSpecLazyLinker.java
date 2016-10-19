package fzi.mottem.ptspec.dsl.linking;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtext.linking.lazy.LazyLinker;

import fzi.mottem.model.baseelements.BaseelementsPackage;
import fzi.mottem.model.baseelements.IExecutor;

public class PTSpecLazyLinker extends LazyLinker
{
	
	@Override
	protected EClass globalFindInstantiableCompatible(EClass eType)
	{
		EClass iexecutorEClass = (EClass) BaseelementsPackage.eINSTANCE.getEClassifier(IExecutor.class.getSimpleName());
		if (iexecutorEClass.equals(eType))
		{
			EClass result = findSubTypeInEPackage(getRegistry().getEPackage("http://www.fzi.de/mottem/model/testrigmodel"), eType);
			return result;
		}
		
		return super.globalFindInstantiableCompatible(eType);
	}

}
