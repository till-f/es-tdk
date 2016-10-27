package fzi.mottem.runtime.rtgraph.commands;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;

import fzi.mottem.model.baseelements.IDisplayable;
import fzi.mottem.model.baseelements.ISignal;
import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.model.codemodel.Variable;
import fzi.mottem.model.datastreammodel.EDirection;
import fzi.mottem.model.datastreammodel.MessageSignal;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.ui.nature.PTSpecNature;
import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal.SignalType;
import fzi.mottem.runtime.rtgraph.settingsViews.SetupUI;
import fzi.util.eclipse.IntegrationUtils;
import fzi.util.ecore.EcoreUtils;

public class RefreshCommand extends AbstractHandler 
{

	@Override
	public Object execute(ExecutionEvent event) 
	{
		DataExchanger.dropAllSignals();
		
    	try
    	{
    		for (IProject ptsProject : ResourcesPlugin.getWorkspace().getRoot().getProjects())
    		{
        		if (ptsProject == null || !ptsProject.isOpen())
        		{
        			continue;
        		}

        		if (ptsProject.hasNature(PTSpecNature.NATURE_ID))
    			{
        			List<IResource> modelFiles = IntegrationUtils.getResourcesOfProject(ptsProject, "etm-testrig");
        			for(IResource modelFile : modelFiles)
        			{
            			URI codeInstanceURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);
            			TestRigInstance tri = (TestRigInstance)EcoreUtils.loadFullEMFModel(codeInstanceURI);

            			Collection<IDisplayable> readables = PTSpecUtils.getAllDisplayables(tri);
            			
            			for (IDisplayable displayable : readables)
            			{
            				String uid = PTSpecUtils.getElementUID(displayable);
            				
            				SignalType type;
            				if (displayable instanceof MessageSignal)
            				{
            					type = ((MessageSignal) displayable).getDirection() == EDirection.INPUT ? SignalType.HW_OUTPUT : SignalType.HW_INPUT;
            				}
            				else
            				{
            					type = SignalType.BIDIRECTIONAL;
            				}
            				
                			DataExchanger.setUpSignal(uid, displayable.getDisplayName(), type);
                			
                			// !TODO: Register Readables which are auto-updated (event driven) differently from
                			//        Readables that must be polled by the GUI (advanced use-case, not important atm)
            			}
        			}
    			}
			}

			SetupUI.refreshSignals();
		}
    	catch (Exception e)
    	{
			e.printStackTrace();
		}
    	
		return null;
	}

}
