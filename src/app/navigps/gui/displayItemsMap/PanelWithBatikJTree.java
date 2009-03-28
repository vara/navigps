/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.displayItemsMap;

import app.navigps.gui.MainWindowIWD;
import app.navigps.gui.Scrollbar.ui.LineScrollBarUI;
import app.navigps.gui.svgComponents.DOMDocumentTree;
import app.navigps.gui.svgComponents.DOMDocumentTreeController;
import app.navigps.utils.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.batik.bridge.svg12.ContentManager;
import org.apache.batik.bridge.svg12.DefaultXBLManager;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg12.XBLOMContentElement;
import org.apache.batik.dom.xbl.NodeXBL;
import org.apache.batik.dom.xbl.XBLManager;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

/**
 * Created on 2008-12-30, 19:37:03
 * @author vara
 */
public class PanelWithBatikJTree extends JScrollPane{

    //create only with -s start up parameter(window with properties svg doc)
    private DOMDocumentTree tree;    


    /**
     *
     */
    public final DOMTreeSelectionListener treeSelectionListener = new DOMTreeSelectionListener();
    private final GVTTreeListener listener = new GVTTreeListener();

    //test
    protected DetailsPanel attributePanel;

    public PanelWithBatikJTree(DetailsPanel dp){

        attributePanel = dp;
        init();
    }

    /**
     *
     */
    public void reloadTree(){
        getGVTTreeListener().reload();
    }

    private void init(){

        setBorder(null);
        getViewport().setOpaque(false);
        getViewport().setBorder(null);

        tree = new DOMDocumentTree(null,
                    new DOMDocumentTreeController() {
            @Override
            public boolean isDNDSupported() {
                return true;
            }
        });

        tree.setOpaque(false);
        tree.setBorder(null);
        tree.addTreeSelectionListener(treeSelectionListener);        
        tree.setCellRenderer(new NodeRenderer());

        setViewportView(tree);
        JScrollBar scbH = getHorizontalScrollBar();
        JScrollBar scbV = getVerticalScrollBar();
        scbH.setOpaque(false);
        scbV.setOpaque(false);
        scbV.setUI(new LineScrollBarUI());
        scbH.setUI(new LineScrollBarUI());

        scbH.removeAll();
        scbV.removeAll();

        attributePanel.addActionToButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadTree();
            }
        });
    }

    /**
     *
     * @param doc
     */
    public void createModelTree(SVGDocument doc){
        long startTime = System.nanoTime();
        System.out.println("----Create content window properties----");
        System.out.println("Build Tree Nodes ...");
        System.out.println("Build Tree Model for Tree Nodes ...");
        TreeNode root = createTree(doc,false);
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        if(model==null){
            model = new DefaultTreeModel(root);
            tree.setModel(model);
        }else
            model.setRoot(root);

        System.out.println("Build Tree Model Completed");
        System.out.println("Build Tree Nodes Completed");
        long stopTime = System.nanoTime();
        String time = Utils.roundsValue(((stopTime-startTime)/1000000),5);
        System.err.println("Time : "+time+" milisec.");
    }

    /**
     *
     * @return
     */
    public GVTTreeListener getGVTTreeListener(){
        return listener;
    }

    private class GVTTreeListener extends GVTTreeBuilderAdapter
            implements SVGDocumentLoaderListener{
        
        private SVGDocument doc;

        @Override
        public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
            reload();
        }
        @Override
        public void gvtBuildStarted(GVTTreeBuilderEvent e) {
            //tree.setModel(null);
        }
        @Override
        public void gvtBuildCancelled(GVTTreeBuilderEvent e) {}
        @Override
        public void gvtBuildFailed(GVTTreeBuilderEvent e) {}
        @Override
        public void documentLoadingStarted(SVGDocumentLoaderEvent e) {}
        @Override
        public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
            doc = e.getSVGDocument();
        }
        @Override
        public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {}
        @Override
        public void documentLoadingFailed(SVGDocumentLoaderEvent e) {}

        public void reload(){
            if(doc!=null){
                createModelTree(doc);
            }
        }
    }

    /**
     * Finds and returns the node in the tree that represents the given node
     * in the document.
     *
     * @param theTree
     *            The given JTree
     * @param node
     *            The given org.w3c.dom.Node
     * @return Node or null if not found
     */
    protected DefaultMutableTreeNode findNode(JTree theTree, Node node) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) theTree
                .getModel().getRoot();
        Enumeration treeNodes = root.breadthFirstEnumeration();
        while (treeNodes.hasMoreElements()) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) treeNodes
                    .nextElement();
            NodeInfo userObject = (NodeInfo) currentNode.getUserObject();
            if (userObject.getNode() == node) {
                return currentNode;
            }
        }
        return null;
    }

    /**
     * Finds and selects the given node in the document tree.
     *
     * @param targetNode
     *            The node to be selected
     */
    public void selectNode(final Node targetNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DefaultMutableTreeNode node = findNode(tree, targetNode);
                if (node != null) {
                    TreeNode[] path = node.getPath();
                    TreePath tp = new TreePath(path);
                    // Changes the selection
                    tree.setSelectionPath(tp);
                    // Expands and scrolls the TreePath to visible if
                    // needed
                    tree.scrollPathToVisible(tp);
                }
            }
        });
    }

    /**
     *
     * @param node
     * @param showWhitespace
     * @return
     */
    protected MutableTreeNode createTree(Node node,boolean showWhitespace) {
        DefaultMutableTreeNode result;
        result = new DefaultMutableTreeNode(new NodeInfo(node));

        for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (!showWhitespace && (n instanceof org.w3c.dom.Text)) {
                String txt = n.getNodeValue();
                if (txt.trim().length() == 0)
                    continue;
            }
            result.add(createTree(n, showWhitespace));
        }

        if (node instanceof NodeXBL) {
            Element shadowTree = ((NodeXBL) node).getXblShadowTree();
            if (shadowTree != null) {
                DefaultMutableTreeNode shadowNode
                    = new DefaultMutableTreeNode
                        (new ShadowNodeInfo(shadowTree));
                shadowNode.add(createTree(shadowTree, showWhitespace));
                result.add(shadowNode);
            }
        }
        if (node instanceof XBLOMContentElement) {
            AbstractDocument doc = (AbstractDocument) node.getOwnerDocument();
            XBLManager xm = doc.getXBLManager();
            if (xm instanceof DefaultXBLManager) {
                DefaultMutableTreeNode selectedContentNode
                    = new DefaultMutableTreeNode(new ContentNodeInfo(node));
                DefaultXBLManager dxm = (DefaultXBLManager) xm;
                ContentManager cm = dxm.getContentManager(node);
                if (cm != null) {
                    NodeList nl = cm.getSelectedContent((XBLOMContentElement) node);
                    for (int i = 0; i < nl.getLength(); i++) {
                        selectedContentNode.add(createTree(nl.item(i),
                                                           showWhitespace));
                    }
                    result.add(selectedContentNode);
                }
            }
        }
        return result;
    }

    /**
     *
     */
    protected class DOMTreeSelectionListener implements TreeSelectionListener {
        //protected CSSStyleDeclaration style;
        //protected ViewCSS viewCSS;
        /**
         * Called when the selection changes.
         * @param ev
         */
        @Override
        public void valueChanged(TreeSelectionEvent ev) {
            
            
            DefaultMutableTreeNode mtn;
            mtn = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();

            if (mtn == null) {
                return;
            }

            Object nodeInfo = mtn.getUserObject();            
            if (nodeInfo instanceof NodeInfo) {
                final Node node = ((NodeInfo) nodeInfo).getNode();
                switch (node.getNodeType()) {
                case Node.DOCUMENT_NODE:
                    attributePanel.getXMLEditorPanel().getNodeXmlArea().setText(
                            createDocumentText((Document) node));
                    break;
                case Node.ELEMENT_NODE:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            attributePanel.setPreviewElement((Element) node);
                        }
                    }).start();
                    break;
                case Node.COMMENT_NODE:
                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                   attributePanel.getXMLEditorPanel().getNodeXmlArea().setText(
                        node.getNodeValue());
                    
                }
            }
        }

        /**
         *
         * @param doc
         * @return
         */
        protected String createDocumentText(Document doc) {
            StringBuffer sb = new StringBuffer();
            sb.append("Nodes: ");
            sb.append(nodeCount(doc));
            return sb.toString();
        }

        /**
         *
         * @param node
         * @return
         */
        protected int nodeCount(Node node) {
            int result = 1;
            for (Node n = node.getFirstChild();
                 n != null;
                 n = n.getNextSibling()) {
                result += nodeCount(n);
            }
            return result;
        }

        /**
         * Processes element selection overlay.
         *
         * @param ev
         *            Tree selection event
         */
        protected void handleElementSelection(TreeSelectionEvent ev) {
            TreePath[] paths = ev.getPaths();
            for (int i = 0; i < paths.length; i++) {
                TreePath path = paths[i];
                DefaultMutableTreeNode mtn =
                    (DefaultMutableTreeNode) path.getLastPathComponent();
                Object nodeInfo = mtn.getUserObject();
                if (nodeInfo instanceof NodeInfo) {
                    Node node = ((NodeInfo) nodeInfo).getNode();
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        
                    }
                }
            }
        }
    }

    /**
     * To render the tree nodes.
     */
    protected class NodeRenderer extends DefaultTreeCellRenderer {

        /**
         *
         */
        protected ImageIcon elementIcon;
        /**
         *
         */
        protected ImageIcon commentIcon;
        /**
         *
         */
        protected ImageIcon piIcon;
        /**
         *
         */
        protected ImageIcon textIcon;

        /**
         *
         */
        public NodeRenderer() {
            setBackgroundNonSelectionColor(new Color(0,0,0,0));
            String folder = "batik/treerenderer/";
            elementIcon = MainWindowIWD.createNavigationIcon(folder+"element","gif");
            commentIcon = MainWindowIWD.createNavigationIcon(folder+"comment", "gif");
            piIcon =MainWindowIWD.createNavigationIcon(folder+"pi", "gif");
            textIcon = MainWindowIWD.createNavigationIcon(folder+"text", "gif");
            setIconTextGap(8);

        }

        /**
         * Sets the value of the current tree cell.
         * @param tree 
         * @param value 
         * @param expanded
         * @param sel
         * @param hasFocus
         * @param leaf
         * @param row
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree,
                                                      Object value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf,
                                                      int row,
                                                      boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded,
                                               leaf, row, hasFocus);
            switch (getNodeType(value)) {
            case Node.ELEMENT_NODE:
                setIcon(elementIcon);
                break;
            case Node.COMMENT_NODE:
                setIcon(commentIcon);
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                setIcon(piIcon);
                break;
            case Node.TEXT_NODE:
            case Node.CDATA_SECTION_NODE:
                setIcon(textIcon);
                break;
            }
            return this;
        }

        /**
         * Returns the DOM type of the given object.
         * @param value
         * @return the type or -1.
         */
        protected short getNodeType(Object value) {
            DefaultMutableTreeNode mtn = (DefaultMutableTreeNode)value;
            Object obj = mtn.getUserObject();
            if (obj instanceof NodeInfo) {
                Node node = ((NodeInfo)obj).getNode();
                return node.getNodeType();
            }
            return -1;
        }
    }
    /**
     * To store the nodes informations
     */
    public static class NodeInfo {
        /**
         * The DOM node.
         */
        protected Node node;

        /**
         * Creates a new NodeInfo object.
         * @param n
         */
        public NodeInfo(Node n) {
            node = n;
        }

        /**
         * Returns the DOM Node associated with this node info.
         * @return
         */
        public Node getNode() {
            return node;
        }

        /**
         * Returns a printable representation of the object.
         */
        @Override
        public String toString() {
            if (node instanceof Element) {
                Element e = (Element) node;
                String id = e.getAttribute(SVGConstants.SVG_ID_ATTRIBUTE);
                if (id.length() != 0) {
                    return node.getNodeName() + " \"" + id + "\"";
                }
            }
            return node.getNodeName();
        }
    }

    /**
     * To store the node information for a shadow tree.
     */
    protected static class ShadowNodeInfo extends NodeInfo {

        /**
         *
         * @param n
         */
        public ShadowNodeInfo(Node n) {
            super(n);
        }

        @Override
        public String toString() {
            return "shadow tree";
        }
    }

    /**
     * To store the node information for an xbl:content node's
     * selected content.
     */
    protected static class ContentNodeInfo extends NodeInfo {

        /**
         *
         * @param n
         */
        public ContentNodeInfo(Node n) {
            super(n);
        }

        @Override
        public String toString() {
            return "selected content";
        }
    }
}
