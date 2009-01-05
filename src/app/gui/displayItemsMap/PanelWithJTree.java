/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.displayItemsMap;

import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.SVGDOMTreeModel;
import app.gui.svgComponents.SVGDOMTreeRenderer;
import app.utils.OutputVerboseStream;
import app.utils.Utils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.tree.ExpandVetoException;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;

/**
 * Created on 2008-12-30, 19:37:03
 * @author vara
 */
public class PanelWithJTree extends JScrollPane{

    //create only with -s start up parameter(window with properties svg doc)
    private JTree tree = new JTree();
    private OutputVerboseStream verbose;
    private Canvas canvas;

    public final TreeListener treeSelectionListener = new TreeListener();

    public PanelWithJTree(Canvas canvas,OutputVerboseStream verbose){
        this.verbose = verbose;
        this.canvas = canvas;        
        init();
    }

    private void init(){
        
        setBorder(null);
        getViewport().setOpaque(false);
        getViewport().setBorder(null);
        
        tree.setOpaque(false);
        tree.setBorder(null);
        tree.setModel(null);
        tree.addTreeSelectionListener(treeSelectionListener);
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

    private class GVTTreeListener extends GVTTreeBuilderAdapter{
        @Override
        public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
            long startTime = System.nanoTime();
            getVerboseStream().outputVerboseStream("----Create content window properties----");
            getVerboseStream().outputVerboseStream("Build Tree Nodes ...");
            getVerboseStream().outputVerboseStream("Build Tree Model for Tree Nodes ...");
            SVGDOMTreeModel model = new SVGDOMTreeModel(canvas.getSVGDocument());
            tree.setModel(model);
            getVerboseStream().outputVerboseStream("Build Tree Model Completed");
            tree.setCellRenderer(new SVGDOMTreeRenderer());
            getVerboseStream().outputVerboseStream("Build Tree Nodes Completed");
            long stopTime = System.nanoTime();
            String time = Utils.roundsValue(((stopTime-startTime)/1000000),5);
            getVerboseStream().outputErrorVerboseStream("Time : "+time+" milisec.");
        }
        @Override
        public void gvtBuildStarted(GVTTreeBuilderEvent e) {
            tree.setModel(null);
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

     public class TreeListener implements TreeSelectionListener,TreeWillExpandListener{

        public void valueChanged(TreeSelectionEvent e) {
            System.out.println(""+e.getPath().toString());

        }

        public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        }
        public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        }

    }
}
