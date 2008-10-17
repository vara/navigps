/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import app.utils.Utils;
import config.MainConfiguration;
import config.SVGConfiguration;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
        
    /*
     *	enebled/desibled sercher services
     */
    private boolean findSerices = true;
    private Sercher sercher = new Sercher();
    
    public Canvas(SVGBridgeListeners listeners){
		
	//setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
	setDocumentState(Canvas.ALWAYS_STATIC);
	setRecenterOnResize(false);
	setDoubleBuffered(true);
	
	addSVGDocumentLoaderListener(listeners);	    
	addGVTTreeBuilderListener(listeners);
	addGVTTreeRendererListener(listeners);
		
	zoomListener=new  ZoomAndMove();
        //addMouseListener(zoomListener);
        addMouseMotionListener(zoomListener);
	this.listeners = listeners;
	
	setLayout(new GridLayout());
	add(sercher);
	addMouseMotionListener(sercher);
	addMouseListener(sercher);
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
    
    @Override
    public void paintComponent(Graphics g){
	super.paintComponent(g);
    }
    
    public class ZoomAndMove extends MouseAdapter
    {
	private boolean zoomAtMousePoit = false;
	
	@Override
	public void mouseClicked(MouseEvent evt) {
	    
	    Point2D p2d = new Point2D.Double(evt.getX(),evt.getY());
	    if(evt.getButton()==1){
	    
		zoomIn(p2d);
		if(MainConfiguration.getMode())
		    System.out.println("zoomIn");
	    }
	    else {
		zoomOut(p2d);
		if(MainConfiguration.getMode())
		    System.out.println("zoomOut");
	    }
	}
	@Override
	public void mousePressed(MouseEvent e) {
	    	
	}
	@Override
	public void mouseDragged(MouseEvent e) {
	    
	    
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	    SVGDocument doc = getSVGDocument();	    
	    if(doc != null && !listeners.isRedering()){
		SVGOMPoint svgp =Utils.getLocalPointFromDomElement(doc.getRootElement(),e.getX() ,e.getY());
		//1.position on source component 2.positon on screen 3. posytion on svg doc (root element)
		String str = e.getX()+","+e.getY()+";"+
			     e.getXOnScreen()+","+e.getYOnScreen()+";"+
			     (int)svgp.getX()+","+(int)svgp.getY();
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
		
	MouseListener[] ml = getMouseListeners();
	for (MouseListener mouseListener : ml) {
	    System.out.println(""+mouseListener);	    
	}
	
	if(setZoom){
	    addMouseListener(zoomListener);
	    //System.out.println("Dodano listenera");	    
	    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	else{
	    setCursor(Cursor.getDefaultCursor());
	    removeMouseListener(zoomListener);
	    //System.out.println("Usunieto listenera");		
	}	
    }
    
    private class Sercher extends JComponent implements MouseListener,MouseMotionListener{
	
	private Point.Double xy = new Point.Double(0,0);
	private double radius=0;
	private Point.Double currentPos = new Point.Double(0,0);
	private boolean transformRepaint = true;
	
	public Sercher(){
	    
	}
	@Override
	public void paintComponent(Graphics g){
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D)g;
	    AffineTransform mainTransform = g2.getTransform();
	    if(transformRepaint){
		System.out.println("auto repaint "+transformRepaint);
		g2.setTransform(getRenderingTransform());		
	    }
	    
	    float dash[] = { 10.0f };	    	    
	    BasicStroke bs = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	    Ellipse2D circle = new Ellipse2D.Double(-radius,-radius,radius*2,radius*2);
	    Ellipse2D centerPiont = new Ellipse2D.Double(xy.x-2,xy.y-2,4,4);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);		
	    g2.setColor(Color.GREEN);		
	    g2.fill(centerPiont);
	    g2.setColor(Color.BLACK);		
	    g2.drawLine((int)xy.x,(int)xy.y,(int)currentPos.x,(int)currentPos.y);		
	    g2.setStroke(bs);
	    g2.translate(xy.x,xy.y);		
	    g2.draw(circle);				
	    Color c = new Color(0,150,255,100);
	    g2.setColor(c);
	    g2.fill(circle);
	    g2.translate(-xy.x,-xy.y);
	    
	    if(transformRepaint){
		g2.setTransform(mainTransform);
	    }
	}
	public void setCenterPoint(double x,double y){
	    xy.setLocation(x, y);
	}
	public void setRadius(double r){
	    radius =r;
	}
	public void setCurrentPosition(double x,double y){
	    currentPos.setLocation(x, y);
	    setRadius(xy.distance(currentPos));
	}
	public Point.Double getCenterPoint(){
	    return xy;
	}

	public void mouseClicked(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	    if(findSerices){
		transformRepaint = false;
		sercher.setCenterPoint(e.getX(), e.getY());
		System.out.println(""+e.getX()+""+ e.getY());
	    }
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {	    
	}
	public void mouseExited(MouseEvent e) {	   
	}
	public void mouseDragged(MouseEvent e) {  
	    if(findSerices){
		sercher.setCurrentPosition(e.getX(), e.getY());		
		repaint();
		transformRepaint = true;
	    }
	}
	public void mouseMoved(MouseEvent e) {	    
	}
    }
    
}
