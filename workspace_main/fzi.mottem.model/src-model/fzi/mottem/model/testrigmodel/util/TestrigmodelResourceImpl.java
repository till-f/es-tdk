/**
 */
package fzi.mottem.model.testrigmodel.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import fzi.mottem.model.util.ModelUtils;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource </b> associated with the package.
 * <!-- end-user-doc -->
 * @see fzi.mottem.model.testrigmodel.util.TestrigmodelResourceFactoryImpl
 * @generated
 */
public class TestrigmodelResourceImpl extends XMIResourceImpl {
	/**
	 * Creates an instance of the resource.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param uri the URI of the new resource.
	 * @generated NOT
	 */
	public TestrigmodelResourceImpl(URI uri) {
		super(uri);
		
		this.getDefaultLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl#useUUIDs()
	 * @generated NOT
	 */
	@Override
	protected boolean useUUIDs() {
		return true;
	}
	
	/**
	 * @generated NOT
	 */
	@Override
	protected void attachedHelper(EObject eObject)
	{
	    if (useIDs() && useUUIDs())
	    {
			String id = getID(eObject);
			if (id == null)
			{
				id = ModelUtils.generateCustomUUID(eObject);
				setID(eObject, id);
			}
			else
			{
				getIDToEObjectMap().put(id, eObject);
			}
	    }
	}
	
} //TestRigModelResourceImpl
