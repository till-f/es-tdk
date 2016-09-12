package fzi.mottem.runtime.interfaces;

import org.eclipse.emf.ecore.EObject;

import fzi.mottem.ptspec.dsl.common.PTSpecUtils;

public interface IUIDResolver
{
	public enum UIDType { Element, Property }
	
	/*
	 * Returns the UID type. For "Property" the UIDResolver
	 * resolves to the element with which the property is associated.
	 */
	public static UIDType classify(String elementUID)
	{
		boolean isProperty = elementUID.lastIndexOf(PTSpecUtils.UID_PROPERTY_SEPARATOR) > 1;
		
		return isProperty ? IUIDResolver.UIDType.Property :  IUIDResolver.UIDType.Element;
	}
	
	/*
	 * Resets hashtables for quick UID lookup
	 * Musst be called before test execution to
	 * ensure work is done on latest snapshot
	 */
	public void reset();
	
	/*
	 * Returns the object representation of the element
	 * defined by the provided ID. If the UID is classified
	 * as property, the associated element will be returned
	 * (as properties do not have an object to represent them)
	 */
	public EObject getElement(String elementUID);
	
}
