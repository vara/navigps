/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import javax.swing.event.EventListenerList;
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
    protected EventListenerList listenerList = new EventListenerList();

    public SVGDOMTreeModel(Document d){
        doc =d;
    }

    @Override
    public Object getRoot() {
        return doc.getDocumentElement();
    }

    @Override
    public Object getChild(Object parent, int index) {
        Node node = (Node)parent;
        NodeList nl = node.getChildNodes();
        return nl.item(index);
    }

    @Override
    public int getChildCount(Object parent) {
        Node node = (Node)parent;
        NodeList nl = node.getChildNodes();
        return nl.getLength();
    }

    @Override
    public boolean isLeaf(Object node) {
        return getChildCount(node)==0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
	
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        Node node = (Node)parent;
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if(getChild(node, i)==child)
            return i;
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
	
    }
}
