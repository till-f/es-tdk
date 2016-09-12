package fzi.mottem.runtime.util;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.runtime.interfaces.IUIDResolver;

public class UIDResolver implements IUIDResolver
{
	private final HashMap<String, EObject> _loadedResources = new HashMap<String, EObject>();
	
	private final HashMap<String, EObject> _knownElements = new HashMap<String, EObject>();
	
	private ResourceSet _resourceSet = new ResourceSetImpl();

	@Override
	public void reset()
	{
		_loadedResources.clear();
		_knownElements.clear();
		_resourceSet = new ResourceSetImpl();
	}

	@Override
	public EObject getElement(String elementUID)
	{
		if (IUIDResolver.classify(elementUID) == UIDType.Property)
		{
			elementUID = elementUID.substring(0, elementUID.lastIndexOf(PTSpecUtils.UID_PROPERTY_SEPARATOR));
		}
		
		int splitPoint = elementUID.lastIndexOf('/');
		String platformResourceUriString = elementUID.substring(0, splitPoint);
		
		if (!_loadedResources.keySet().contains(platformResourceUriString))
			loadUIDs(platformResourceUriString);
		
		if (_knownElements.containsKey(elementUID))
		{
			return _knownElements.get(elementUID);
		}
		else
		{
			StringBuilder sb = new StringBuilder();
			for (String res : _loadedResources.keySet())
			{
				sb.append("\n  ");
				sb.append(res);
			}
			throw new RuntimeException("No element with UID '" + elementUID + "' in loaded resources:" + sb.toString());
		}
	}
	
	private void loadUIDs(String platformResourceUriString)
	{
		EObject root = loadResource(platformResourceUriString);
		
		TreeIterator<EObject> treeIterator = root.eAllContents();
		while (treeIterator.hasNext())
		{
			EObject element = treeIterator.next();
			String elementUID = PTSpecUtils.getElementUID(element);
			if (elementUID != null && !_knownElements.containsKey(elementUID))
			{
				_knownElements.put(elementUID, element);
			}
		}
		
		_loadedResources.put(platformResourceUriString, root);
	}
	
	private EObject loadResource(String platformResourceUriString)
	{
		URI platformResourceUri = URI.createPlatformResourceURI(platformResourceUriString, true);
		
		Resource resource = _resourceSet.getResource(platformResourceUri, true);

		try
		{
			resource.load(null);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		EcoreUtil.resolveAll(_resourceSet);
		EObject root = resource.getContents().get(0);
		
		return root;
	}
	

}
