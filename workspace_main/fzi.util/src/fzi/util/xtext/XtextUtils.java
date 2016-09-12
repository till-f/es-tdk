package fzi.util.xtext;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

public class XtextUtils
{
	public static String getTextForFeature(EObject element, EReference ref)
	{
		INode node = NodeModelUtils.findNodesForFeature(element, ref).get(0);
		return node.getText().trim();
	}

}
