/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author vara
 */
public class SVGDOMTreeModel implements TreeModel{

    private Document doc;
    
    public SVGDOMTreeModel(Document d){
	doc =d;
    }

    public Object getRoot() {
	return doc.getDocumentElement();
    }

    public Object getChild(Object parent, int index) {
	Node node = (Node)parent;
	NodeList nl = node.getChildNodes();
	return nl.item(index);
    }

    public int getChildCount(Object parent) {
	Node node = (Node)parent;
	NodeList nl = node.getChildNodes();
	return nl.getLength();
    }

    public boolean isLeaf(Object node) {
	return getChildCount(node)==0;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
	
    }

    public int getIndexOfChild(Object parent, Object child) {
	Node node = (Node)parent;
	NodeList nl = node.getChildNodes();
	for (int i = 0; i < nl.getLength(); i++) {
	    if(getChild(node, i)==child)
		return i;	    
	}
	return -1;
    }

    public void addTreeModelListener(TreeModelListener l) {
	
    }

    public void removeTreeModelListener(TreeModelListener l) {
	
    }
}
