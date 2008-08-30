/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import app.gui.StatusPanel;
import app.utils.Utils;
import config.MainConfiguration;
import config.SVGConfiguration;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.JLabel;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
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
        
    
    public Canvas(SVGBridgeListeners listeners){
		
	//setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
	setDocumentState(Canvas.ALWAYS_STATIC);
	setDoubleBuffered(true);
	
	addSVGDocumentLoaderListener(listeners);	    
	addGVTTreeBuilderListener(listeners);
	addGVTTreeRendererListener(listeners);
		
	zoomListener=new  ZoomAndMove();
        addMouseListener(zoomListener);
        addMouseMotionListener(zoomListener);
	this.listeners = listeners;
    }
    @Override
    public void setURI(String uri){
	listeners.setAbsoluteFilePath(uri);
	super.setURI(uri);
    }
    public boolean isDocumentSet(){ return (getSVGDocument() != null);}
    
    public JLabel getLabelViewMousePosyton() {
	return labelViewMousePosyton;
    }
    
    public class ZoomAndMove extends MouseAdapter
    {
	private String flag;
	
	@Override
	public void mouseClicked(MouseEvent evt) {
	    
	    Point2D p2d = new Point2D.Double(evt.getX(),evt.getY());
	    if(evt.getButton()==1){
	    
		zoomIn(p2d);
		if(MainConfiguration.getMode())
		    System.out.println("zoomout");
	    }
	    else {
		zoomOut(p2d);
		if(MainConfiguration.getMode())
		    System.out.println("zoomin");
	    }
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	    //throw new UnsupportedOperationException("Not supported yet.");
	    
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	    SVGDocument doc = getSVGDocument();	    
	    if(doc != null && !listeners.isRedering()){
		SVGOMPoint svgp =Utils.getLocalPointFromDomElement(doc.getRootElement(),e.getX() ,e.getY());
		//1.position on source component 2.positon on screen 3. posytion on svg doc (root element)
		String str = e.getX()+","+e.getY()+";"+
			     e.getXOnScreen()+","+e.getYOnScreen()+";"+
			     svgp.getX()+","+svgp.getY();
		//System.out.println(""+str);
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
	    }
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
	    if(zoomIn)
		zoomIn(p2d);
	    else
		zoomOut(p2d);
	}
	
    }//class ZoomAndMoveMouseListener
    
    
    public void zoomFromCenterDocumnet(boolean zoomIn){
	zoomListener.zoomFromCenterDocumnet(zoomIn);
    }
    public void zoomFromMouseCoordinationEnable(boolean setZoom){
	resetRenderingTransform();
	MouseListener[] ml = getMouseListeners();
	if(setZoom){
	    
	    if(ml.length == 0)
		addMouseListener(listener);
	}
	else
	    if(ml.length > 0)
		removeMouseListener(listener);
    }
    
}
