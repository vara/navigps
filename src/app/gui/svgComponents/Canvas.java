/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import app.gui.searchServices.SearchServices;
import app.utils.Utils;
import config.SVGConfiguration;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.JLabel;
import org.apache.batik.bridge.UpdateManagerEvent;
import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author vara
 */

/* important "Rendering Process"
 * 
 *  The rendering process can be broken down into five phases. 
 *  Not all of those steps are required - depending on the method used to specify 
 *  the SVG document to display, but basically the steps in the rendering process are:
 *
 *  --Building a DOM tree 
 *	If the loadSVGDocument(String) method is used, the SVG file is parsed 
 *	and an SVG DOM Tree is built.
 * 
 *  --Building a GVT tree 
 *	Once an SVGDocument is created (using the step 1 or if the setSVGDocument(SVGDocument) 
 *	method has been used) - a GVT tree is constructed. 
 *	The GVT tree is the data structure used internally to render an SVG document. 
 *	see the org.apache.batik.gvt package.
 * 
 *  --Executing the SVGLoad event handlers 
 *	If the document is dynamic, the scripts are initialized and the SVGLoad 
 *	event is dispatched before the initial rendering. 
 * 
 *  --Rendering the GVT tree 
 *	Then the GVT tree is rendered. see the org.apache.batik.gvt.renderer package.
 * 
 *  --Running the document 
 *	If the document is dynamic, the update threads are started.
 */

public class Canvas extends JSVGCanvas{

    private JLabel labelViewMousePosyton = new JLabel("");
    private SVGConfiguration svgConfig = new SVGConfiguration();
    
    private SVGBridgeListeners listeners;
    private ZoomAndMove zoomListener;
        
    
    private SearchServices search;
    
    public Canvas(SVGBridgeListeners listeners){
		
        //setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        setDocumentState(Canvas.ALWAYS_STATIC);
        setRecenterOnResize(false);
        setDoubleBuffered(false);

        addSVGDocumentLoaderListener(listeners);
        addGVTTreeBuilderListener(listeners);
        addGVTTreeRendererListener(listeners);

        zoomListener=new  ZoomAndMove();
            //addMouseListener(zoomListener);
            addMouseMotionListener(zoomListener);
        this.listeners = listeners;

        setLayout(new BorderLayout());
        search = new SearchServices(listeners.getVerboseStream());
        add(search,BorderLayout.CENTER);
        addMouseMotionListener(search);
        addMouseListener(search);
        addJGVTComponentListener(search.getDocumentStateChanged());

        //for test
        addUpdateManagerListener(new UpdateManagerInfoListener());
    }
    
    public SearchServices getSearchServices(){
        return search;
    }
    
    @Override
    public void setURI(String uri){	
        super.setURI(uri);
        
    }
    public boolean isDocumentSet(){ return (getSVGDocument() != null);}
    
    public JLabel getLabelViewMousePosyton() {
	return labelViewMousePosyton;
    }
    
    @Override
    public void paintComponent(Graphics g){
	super.paintComponent(g);
    }
    
    private class ZoomAndMove extends MouseAdapter{	
        private boolean zoomAtMousePoit = false;

        @Override
        public void mouseClicked(MouseEvent evt) {

            Point2D p2d = new Point2D.Double(evt.getX(),evt.getY());
            if(evt.getButton()==1){
                zoomIn(p2d);
                search.getVerboseStream().outputVerboseStream("Zoom In\n\tPoint "+p2d+
                                "\n\tRate ["+svgConfig.getZoomInRateX()+","+svgConfig.getZoomInRateY()+"]");
            }
            else {
                zoomOut(p2d);
                search.getVerboseStream().outputVerboseStream("Zoom Out\n\tPoint "+p2d+
                                "\n\tRate ["+svgConfig.getZoomOutRateX()+","+svgConfig.getZoomOutRateY()+"]");
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            SVGDocument doc = getSVGDocument();
            if(doc != null && !listeners.isRendering()){
                SVGOMPoint svgp =Utils.getLocalPointFromDomElement(doc.getRootElement(),e.getX() ,e.getY());
                //0.position on source component 1.positon on screen 2. posytion on svg doc (root element)
                String str = e.getX()+","+e.getY()+";"+
                         e.getXOnScreen()+","+e.getYOnScreen()+";"+
                         (int)svgp.getX()+","+(int)svgp.getY();

                listeners.setLabelInformationPosytion(str);
            }
        }

        private void zoomIn(Point2D p2d){
            AffineTransform at;
            at = getRenderingTransform();
            if (at != null) {
                at.preConcatenate(AffineTransform.getTranslateInstance(-p2d.getX(),-p2d.getY()));
                at.preConcatenate(AffineTransform.getScaleInstance(svgConfig.getZoomInRateX(),
                                               svgConfig.getZoomInRateY()));
                at.preConcatenate(AffineTransform.getTranslateInstance(p2d.getX(),p2d.getY()));
                setRenderingTransform(at);
            }else
                search.getVerboseStream().outputVerboseStream(getClass().getName()+" \t[Exception] Rendering transform == null !!!");
        }

        private void zoomOut(Point2D p2d){
            AffineTransform at;
            at = getRenderingTransform();
            if (at != null) {
                at.preConcatenate(AffineTransform.getTranslateInstance(-p2d.getX(),-p2d.getY()));
                at.preConcatenate(AffineTransform.getScaleInstance(svgConfig.getZoomOutRateX(),
                                           svgConfig.getZoomOutRateX()));
                at.preConcatenate(AffineTransform.getTranslateInstance(p2d.getX(),p2d.getY()));
                setRenderingTransform(at);
            }
        }
        public void zoomFromCenterDocumnet(boolean zoomIn){

            Point2D p2d = new Point(getSize().width/2,getSize().height/2);
            if(zoomIn) zoomIn(p2d);
            else zoomOut(p2d);
        }	
    }//class ZoomAndMoveMouseListener
    
    
    public void zoomFromCenterDocumnet(boolean zoomIn){
	zoomListener.zoomFromCenterDocumnet(zoomIn);
    }
    public void zoomFromMouseCoordinationEnable(boolean setZoom){
		
        MouseListener[] ml = getMouseListeners();
        //for (MouseListener mouseListener : ml) {
            //System.out.println(""+mouseListener);
        //}

        if(setZoom){
            addMouseListener(zoomListener);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        else{
            setCursor(Cursor.getDefaultCursor());
            removeMouseListener(zoomListener);
        }
    }

    protected class UpdateManagerInfoListener implements UpdateManagerListener{

        @Override
        public void managerStarted(UpdateManagerEvent e) {
            search.getVerboseStream().outputVerboseStream("managerStarted ");
        }

        @Override
        public void managerSuspended(UpdateManagerEvent e) {
            search.getVerboseStream().outputVerboseStream("managerSuspend ");
        }

        @Override
        public void managerResumed(UpdateManagerEvent e) {
            search.getVerboseStream().outputVerboseStream("managerResumed ");
        }

        @Override
        public void managerStopped(UpdateManagerEvent e) {
            search.getVerboseStream().outputVerboseStream("managerStoped ");
        }

        @Override
        public void updateStarted(UpdateManagerEvent e) {
            search.getVerboseStream().outputVerboseStream("updateStarted ");
        }

        @Override
        public void updateCompleted(UpdateManagerEvent e) {
            search.getVerboseStream().outputVerboseStream("updateCompleted ");
        }

        @Override
        public void updateFailed(UpdateManagerEvent e) {
            search.getVerboseStream().outputVerboseStream("updateFailed ");
        }
    }
}
