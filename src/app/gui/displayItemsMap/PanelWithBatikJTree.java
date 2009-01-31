/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.displayItemsMap;

import app.gui.MainWindowIWD;
import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.DOMDocumentTree;
import app.gui.svgComponents.DOMDocumentTreeController;
import app.utils.OutputVerboseStream;
import app.utils.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.metal.MetalScrollBarUI;
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
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created on 2008-12-30, 19:37:03
 * @author vara
 */
public class PanelWithBatikJTree extends JScrollPane{

    //create only with -s start up parameter(window with properties svg doc)
    private DOMDocumentTree tree;
    private OutputVerboseStream verbose;
    private Canvas canvas;

    private JTextArea documentInfo;

    public final DOMTreeSelectionListener treeSelectionListener = new DOMTreeSelectionListener();

    public PanelWithBatikJTree(Canvas canvas,OutputVerboseStream verbose){
        this.verbose = verbose;
        this.canvas = canvas;
        init();
    }

    private void init(){

        setBorder(null);
        getViewport().setOpaque(false);
        getViewport().setBorder(null);

        tree = new DOMDocumentTree(null,
                    new DOMDocumentTreeController() {
            public boolean isDNDSupported() {
                return true;
            }
        });

        tree.setOpaque(false);
        tree.setBorder(null);
        tree.addTreeSelectionListener(treeSelectionListener);        
        tree.setCellRenderer(new NodeRenderer());

        setViewportView(tree);
        canvas.addGVTTreeBuilderListener(new GVTTreeListener());
        JScrollBar scbH = getHorizontalScrollBar();
        JScrollBar scbV = getVerticalScrollBar();
        scbH.setOpaque(false);
        scbV.setOpaque(false);
        scbV.setUI(new MyScrollBarUI());
        scbH.setUI(new MyScrollBarUI());

        scbH.removeAll();
        scbV.removeAll();
    }

    public OutputVerboseStream getVerboseStream(){
        return verbose;
    }
    public void createModelTree(){
        long startTime = System.nanoTime();
        getVerboseStream().outputVerboseStream("----Create content window properties----");
        getVerboseStream().outputVerboseStream("Build Tree Nodes ...");
        getVerboseStream().outputVerboseStream("Build Tree Model for Tree Nodes ...");
        TreeNode root = createTree(canvas.getSVGDocument(),false);
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        if(model==null){
            model = new DefaultTreeModel(root);
            tree.setModel(model);
        }else
            model.setRoot(root);

        getVerboseStream().outputVerboseStream("Build Tree Model Completed");
        getVerboseStream().outputVerboseStream("Build Tree Nodes Completed");
        long stopTime = System.nanoTime();
        String time = Utils.roundsValue(((stopTime-startTime)/1000000),5);
        getVerboseStream().outputErrorVerboseStream("Time : "+time+" milisec.");
    }
    private class GVTTreeListener extends GVTTreeBuilderAdapter{
        @Override
        public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
            createModelTree();
        }
        @Override
        public void gvtBuildStarted(GVTTreeBuilderEvent e) {
            //tree.setModel(null);
        }
        @Override
        public void gvtBuildCancelled(GVTTreeBuilderEvent e) {}
        @Override
        public void gvtBuildFailed(GVTTreeBuilderEvent e) {}
    }

    public static class MyScrollBarUI extends MetalScrollBarUI {

        private Color darkShadowColor = new Color(100,100,100,200);
        private Color shadowColor = new Color(200,200,200,200);
        private Color highlightColor = new Color(200,255,200,200);
        @Override
        protected void installDefaults() {
            super.installDefaults();
            scrollBarWidth = 13;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            //super.paintTrack(g, c, trackBounds);
            Graphics2D g2 = (Graphics2D)g;
            //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.translate( trackBounds.x, trackBounds.y );
            boolean leftToRight = c.getComponentOrientation().isLeftToRight();
            if ( scrollbar.getOrientation() == JScrollBar.VERTICAL ){
                if ( !isFreeStanding ) {
                        trackBounds.width += 2;
                        if ( !leftToRight ) {
                            g2.translate( -1, 0 );
                        }
                }
                if ( c.isEnabled() ) {// VERTICAL
                    g2.setColor(darkShadowColor);
                    int halfArea = trackBounds.width/2;
                    g2.drawLine( halfArea, 3,halfArea, trackBounds.height );
                    g2.setColor(shadowColor);
                    for(int i=1;i<3;i++){
                        int delta = i*50;
                        g2.setColor(Utils.checkColor(darkShadowColor.getRed()+delta,
                                darkShadowColor.getGreen()+delta,
                                darkShadowColor.getBlue()+delta));
                        g2.drawLine( halfArea + i, 3-i, halfArea + i, trackBounds.height - i );
                    }
                } else {
                    //MetalUtils.drawDisabledBorder(g, 0, 0, trackBounds.width, trackBounds.height );
                }

                if ( !isFreeStanding ) {
                    trackBounds.width -= 2;
                    if ( !leftToRight ) {
                        g2.translate( 1, 0 );
                    }
                }
            }
            else{  // HORIZONTAL

                if ( !isFreeStanding ) {
                    trackBounds.height += 2;
                }
                if ( c.isEnabled() ) {
                    g2.setColor( darkShadowColor );
                    int halfArea = trackBounds.height/2;

                    g2.drawLine( 0,halfArea, trackBounds.width - 3,halfArea);
                    g2.setColor( shadowColor );
                    for (int i = 1; i < 3; i++){
                        int delta = i*50;
                        g2.setColor(Utils.checkColor(darkShadowColor.getRed()+delta,
                                darkShadowColor.getGreen()+delta,
                                darkShadowColor.getBlue()+delta));
                        g2.drawLine( i,halfArea+i, trackBounds.width-2,halfArea+i);
                    }

                } else {
                    //MetalUtils.drawDisabledBorder(g, 0, 0, trackBounds.width, trackBounds.height );
                }
                if ( !isFreeStanding ) {
                    trackBounds.height -= 2;
                }
            }
            g2.translate( -trackBounds.x, -trackBounds.y );
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

    protected class DOMTreeSelectionListener implements TreeSelectionListener {
        //protected CSSStyleDeclaration style;
        //protected ViewCSS viewCSS;
        /**
         * Called when the selection changes.
         */
        public void valueChanged(TreeSelectionEvent ev) {
            
            
            DefaultMutableTreeNode mtn;
            mtn = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();

            if (mtn == null) {
                return;
            }

            Object nodeInfo = mtn.getUserObject();
            System.out.println(""+nodeInfo);
            if (nodeInfo instanceof NodeInfo) {
                Node node = ((NodeInfo) nodeInfo).getNode();

                switch (node.getNodeType()) {
                case Node.DOCUMENT_NODE:
                    //documentInfo.setText
                      //  (createDocumentText((Document) node));
                    System.out.println(""+createDocumentText((Document) node));
                    break;
                case Node.ELEMENT_NODE:
                    //propertiesTable.setModel(new NodeCSSValuesModel(node));
                    //attributePanel.promptForChanges();
                    //attributePanel.setPreviewElement((Element) node);
                    //rightPanel.add(elementPanel);
                    break;
                case Node.COMMENT_NODE:
                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                    //characterDataPanel.setNode(node);
                    //characterDataPanel.getTextArea().setText
                        //(node.getNodeValue());
                    //rightPanel.add(characterDataPanel);
                }
            }

            //splitPane.revalidate();
            //splitPane.repaint();
        }

        protected String createDocumentText(Document doc) {
            StringBuffer sb = new StringBuffer();
            sb.append("Nodes: ");
            sb.append(nodeCount(doc));
            return sb.toString();
        }

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

        protected ImageIcon elementIcon;
        protected ImageIcon commentIcon;
        protected ImageIcon piIcon;
        protected ImageIcon textIcon;

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
         */
        public NodeInfo(Node n) {
            node = n;
        }

        /**
         * Returns the DOM Node associated with this node info.
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
         * Creates a new ShadowNodeInfo object.
         */
        public ShadowNodeInfo(Node n) {
            super(n);
        }

        /**
         * Returns a printable representation of the object.
         */
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
         * Creates a new ContentNodeInfo object.
         */
        public ContentNodeInfo(Node n) {
            super(n);
        }

        /**
         * Returns a printable representation of the object.
         */
        @Override
        public String toString() {
            return "selected content";
        }
    }
}
