package fzi.util.ecore;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;

public class EcoreUtils
{
	private static AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE));

	/**
	 * Returns the xmi:id attribute value for the given eObject as a <tt>String</tt>.
	 * This is the ID that was assigned with setID(); if there is no ID, it returns null.
	 * Returns <b>null</b> in case there's no containing resource or the eObject simply
	 * has no respective ID attribute (i.e. is not contained in a XMLResource).
	 */
	public static String getXmiId (EObject eObject)
	{
		Resource resource = eObject.eResource();
		
		if (resource instanceof XMLResource)
		{
			return ((XMLResource) resource).getID(eObject);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the image associated with the given eClass
	 */
	public static Image getImage(EClass eClass)
	{
		EObject eObject = eClass.getEPackage().getEFactoryInstance().create(eClass);
		return getImage(eObject);
	}

	/**
	 * Returns the image associated with the given eObject
	 */
	public static Image getImage(EObject eObject)
	{
		return labelProvider.getImage(eObject);
	}

	/**
	 * Goes upwards the container hierarchy and returns true if any container
	 * is an instance of the provided type. recursion is startet with first container
	 * (and not the object itself).
	 */
	public static boolean hasContainerInstanceOf(EObject eObject, Class<?> type)
	{
		while (eObject.eContainer() != null)
		{
			eObject = eObject.eContainer();
			if (type.isInstance(eObject))
				return true;
		}
		return false;
	}

	/**
	 * Goes upwards the container hierarchy and returns the first element of the provided type.
	 * Recursion is startet with object itself (and not the first container).
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getContainerInstanceOf(EObject eObject, Class<?> type)
	{
		while (eObject != null)
		{
			if (type.isInstance(eObject))
				return (T)eObject;

			eObject = eObject.eContainer();
		}
		
		return null;
	}
	
	/*
	 * Returns list of all first-elemen-in-resource which are contained in the provided resource set
	 */
	public static Collection<EObject> getAllRoots(ResourceSet resourceSet)
	{
		Collection<EObject> roots = new LinkedList<EObject>();

		for (Resource resource : resourceSet.getResources())
		{
			roots.add(resource.getContents().get(0));
		}
		
		return roots;
	}

	/*
	 * Returns the Eclipse IFile object for the given EMF URI. 
	 */
	public static IFile getFileForEMFURI(URI uri)
	{
		String scheme = uri.scheme();
		if ("platform".equals(scheme) && uri.segmentCount() > 1 && "resource".equals(uri.segment(0)))
		{
			StringBuffer platformResourcePath = new StringBuffer();
			
			for (int j = 1, size = uri.segmentCount(); j < size; ++j)
			{
				platformResourcePath.append('/');
				platformResourcePath.append(uri.segment(j));
			}
			
			return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformResourcePath.toString()));
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * TODO: possible improvement: should be equivalent to implementation above. Test and substitute...
	 */
	public static IFile getFileForEMFURI2(URI uri)
	{
		if (uri.isPlatformResource())
			return ResourcesPlugin.getWorkspace().getRoot().getFile(getPathForEMFURI(uri));
		else
			return null;
	}
	
	public static IPath getPathForEMFURI(URI uri)
	{
		return new Path(uri.toPlatformString(true));
	}
	
	public static EObject loadFullEMFModel(URI platformResourceURI) throws IOException
	{
		if(!platformResourceURI.isPlatform())
			System.err.println("EMF model files should always be loaded using a platform URI");
		
		ResourceSet emfResourceSet = new ResourceSetImpl();
		Resource emfResource = emfResourceSet.getResource(platformResourceURI, true);

		emfResource.load(null);
		EcoreUtil.resolveAll(emfResourceSet);
		EObject root = null;
		if (emfResource.getContents().size() > 0) {
			root = emfResource.getContents().get(0);
		}
		
		
		return root;
	}

}
