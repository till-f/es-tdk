package fzi.mottem.ptspec.dsl.ui.wizard;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandFacade;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.OutputImpl;
import org.eclipse.xtend.type.impl.java.JavaBeansMetaModel;
import org.eclipse.xtext.ui.util.PluginProjectFactory;
import org.eclipse.xtext.ui.util.ProjectFactory;
import org.eclipse.xtext.ui.wizard.AbstractPluginProjectCreator;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Provider;

import fzi.mottem.model.util.ModelUtils;
import fzi.mottem.ptspec.dsl.ui.nature.PTSpecNature;

public class PTSpecProjectCreator extends AbstractPluginProjectCreator
{

	@Inject
	private Provider<PTSpecProjectFactory> projectFactoryProvider;

	protected final List<String> JAVA_FOLDERS = ImmutableList.of("src-gen");
	protected final List<String> NON_JAVA_FOLDERS = ImmutableList.of(ModelUtils.PTS_SOURCE_FILES_ROOT, ModelUtils.PTS_MODEL_FILES_ROOT);
	protected final List<String> REQUIRED_BUNDLES = ImmutableList.of("fzi.mottem.model", "fzi.mottem.runtime");
	
	@Override
	protected PTSpecProjectInfo getProjectInfo()
	{
		return (PTSpecProjectInfo) super.getProjectInfo();
	}
	
	@Override
	protected String getModelFolderName()
	{
		return ModelUtils.PTS_SOURCE_FILES_ROOT;
	}
	
	@Override
	protected List<String> getAllFolders()
	{
        return JAVA_FOLDERS;
    }

	protected List<String> getNonJavaFolders()
	{
        return NON_JAVA_FOLDERS;
    }

    @Override
	protected List<String> getRequiredBundles()
	{
        return REQUIRED_BUNDLES;
	}
    
    @Override
    protected String[] getProjectNatures()
    {
    	return super.getProjectNatures();
    }
    
    @Override
    protected PluginProjectFactory createProjectFactory()
    {
    	return projectFactoryProvider.get();
    }
    
    @Override
	protected ProjectFactory configureProjectFactory(ProjectFactory factory)
    {
    	// PTSpec nature comes first
    	// (in order to display PTSpec icon instead of icon of any other nature)
    	factory.addProjectNatures(PTSpecNature.NATURE_ID);

		PTSpecProjectFactory ptspecFactory = (PTSpecProjectFactory) super.configureProjectFactory(factory);

		ptspecFactory.addNonJavaFolders(getNonJavaFolders());
		
		return ptspecFactory;
	}

    @Override
	protected void enhanceProject(final IProject project, final IProgressMonitor monitor) throws CoreException
	{
		OutputImpl output = new OutputImpl();
		output.addOutlet(new Outlet(false, getEncoding(), null, true, project.getLocation().makeAbsolute().toOSString()));

		XpandExecutionContextImpl execCtx = new XpandExecutionContextImpl(output, null);
		execCtx.getResourceManager().setFileEncoding("UTF-8");
		execCtx.registerMetaModel(new JavaBeansMetaModel());

		XpandFacade facade = XpandFacade.create(execCtx);
		facade.evaluate("fzi::mottem::ptspec::dsl::ui::wizard::PTSpecNewProject::main", getProjectInfo());

		project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

}