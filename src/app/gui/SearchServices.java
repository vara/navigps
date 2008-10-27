/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.utils.MyLogger;
import app.utils.OutputVerboseStream;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import odb.inter.ODBridge;
import org.apache.batik.bridge.UpdateManagerEvent;
import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.swing.gvt.JGVTComponentListener;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderListener;

/**
 *
 * @author vara
 */
public class SearchServices extends JComponent implements MouseListener,
							  MouseMotionListener
							  
{
	
	private Point.Double centerPoint = new Point.Double(0,0);
	private Point.Double currentPos = new Point.Double(0,0);
	
	private double radius=0;
	
	private DocumentStateChangedListener listeners;
	
	private OutputVerboseStream verboseStream = null;
	
	private boolean enabled = false;
	
	private ODBridge odbConnector=null;
	
	public SearchServices(OutputVerboseStream l){
	    verboseStream = l;
	    init();	    
	}
	private void init(){
	    
	    listeners = new DocumentStateChangedListener();
	}
	
	public void setEnabledSerchServices(boolean val){
	    enabled = val;
	    setVisible(enabled);
	    setRadius(0.0);
	    setCenterPoint(0.0,0.0);
	    setCurrentPosition(0.0,0.0);
	    repaint();
	}
	public boolean isEnabledSearchServices(){
	    return enabled;	    
	}	
	
	public void initODBConnector(ODBridge odbb){
	    odbConnector = odbb;
	}
	
	@Override
	public void paintComponent(Graphics g){
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D)g;
	    
	    if(isEnabledSearchServices()){
	    
		AffineTransform svgTransform = listeners.getRenderingTransform();
		g2.setTransform(svgTransform);
		float dash[] = { 10.0f };	    	    
		float widthStroke  = (float)(1.0 / svgTransform.getScaleX());
		BasicStroke bsLine = new BasicStroke(widthStroke);
		g2.setStroke(bsLine);
		BasicStroke bsCircle = new BasicStroke(widthStroke, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);	    
		Ellipse2D circle = new Ellipse2D.Double(-radius,-radius,radius*2,radius*2);
		Ellipse2D centerPiont = new Ellipse2D.Double(centerPoint.x-2,centerPoint.y-2,4,4);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);		
		g2.setColor(Color.GREEN);		
		g2.fill(centerPiont);
		g2.setColor(Color.BLACK);		
		g2.drawLine((int)centerPoint.x,(int)centerPoint.y,(int)currentPos.x,(int)currentPos.y);		
		g2.setStroke(bsCircle);
		g2.translate(centerPoint.x,centerPoint.y);		
		g2.draw(circle);				
		Color c = new Color(0,150,255,100);
		g2.setColor(c);
		g2.fill(circle);
		g2.translate(-centerPoint.x,-centerPoint.y);

		//for test
		g2.setColor(Color.BLACK);
		g2.setTransform(AffineTransform.getScaleInstance(1,1));
		String showCenterPoint = "center point "+getCenterPoint();
		String showRadius = "Radius "+getRadius();
		String showCurrenPoint = "current point "+getCurrentPosition();
		String sizeComp = getPreferredSize()+" getBounds "+getBounds();
		
		FontRenderContext frc = g2.getFontRenderContext();
		Rectangle2D rec = g2.getFont().getMaxCharBounds(frc);
		g2.drawString(showCenterPoint,30,15);
		g2.drawString(showCurrenPoint,30,(int)(15+5+rec.getHeight()));
		g2.drawString(showRadius,30,(int)(15+10+(rec.getHeight()*2)));
		g2.drawString(sizeComp,30,(int)(15+10+(rec.getHeight()*3)));
	    }else{
				
	    }
	}
	
	public DocumentStateChangedListener getDocumentStateChanged(){
	    return listeners;
	}
	
	public void setCenterPoint(double x,double y){
	    centerPoint.setLocation(convertPointToSvgTransform(x, y));
	    //centerPoint.setLocation(x, y);
	}
	public void setRadius(double r){
	    radius =r;
	}
	public double getRadius(){
	    return radius;
	}
	public void setCurrentPosition(double x,double y){
	    currentPos.setLocation(convertPointToSvgTransform(x, y));
	    //currentPos.setLocation(x, y);	    
	    setRadius(centerPoint.distance(getCurrentPosition()));
	}
	public Point.Double getCenterPoint(){
	    return centerPoint;
	}
	
	public Point2D.Double getCurrentPosition(){
	    return currentPos;
	}
	
	public Point.Double convertPointToSvgTransform(double oldX,double oldY){
	    return convertPointToSvgTransform(new Point.Double(oldX, oldY));
	}
	public Point.Double convertPointToSvgTransform(Point.Double oldPoint){
	    AffineTransform at = new AffineTransform(listeners.getRenderingTransform());
	
	    //System.out.println("affine "+at);
	try {
	    at.invert();
	} catch (NoninvertibleTransformException ex) {
	    
	    MyLogger.log.log(Level.SEVERE,SearchServices.class.getName(), ex);
	    getVerboseStream().outputVerboseStream(SearchServices.class.getName()+"\n\t"+ex);
	}
	    double xx = (oldPoint.getX()*at.getScaleX())+at.getTranslateX();
	    double yy = (oldPoint.getY()*at.getScaleY())+at.getTranslateY();
	    oldPoint.setLocation(xx, yy);
	    return oldPoint;
	}
	
	public OutputVerboseStream getVerboseStream(){
	    return verboseStream;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	    if(e.getButton()==MouseEvent.BUTTON1){
		if(isEnabledSearchServices()){
		    setCenterPoint(e.getX(),e.getY());
		    //System.out.println(""+e.getX()+","+ e.getY() +" -> "+getCenterPoint().getX()+","+getCenterPoint().getY());
		}
	    }
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	    //odbConnector.
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	    
	    getVerboseStream().outputVerboseStream("Search services component size "+getSize());
	    //System.out.println("Search services component size "+getSize()+"\nVisible rect "+getVisibleRect());
	    
	}
	
	@Override
	public void mouseExited(MouseEvent e) {	   
	}
	
	@Override	
	public void mouseDragged(MouseEvent e) {
		
	    if(isEnabledSearchServices()){
		setCurrentPosition(e.getX(),e.getY());		
		//System.out.println("drag "+e.getX()+","+ e.getY() +" -> "+getCurrentPosition().getX()+","+getCurrentPosition().getY());
		repaint();
	    }
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	    //System.out.println(""+e.getX()+","+e.getY());
	}
	
	public class DocumentStateChangedListener implements JGVTComponentListener,
							  GVTTreeBuilderListener,
							  GVTTreeRendererListener,
							  UpdateManagerListener
	{
	    
	    private AffineTransform renderingTranform = new AffineTransform();
	    
	    public DocumentStateChangedListener(){
		
	    }
	    @Override
	    public void gvtBuildStarted(GVTTreeBuilderEvent e) {	
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void gvtBuildCompleted(GVTTreeBuilderEvent e) {	
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void gvtBuildCancelled(GVTTreeBuilderEvent e) {	
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void gvtBuildFailed(GVTTreeBuilderEvent e) {	    
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void managerStarted(UpdateManagerEvent e) {	    
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void managerSuspended(UpdateManagerEvent e) {	
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void managerResumed(UpdateManagerEvent e) {	    
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void managerStopped(UpdateManagerEvent e) {	    
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void updateStarted(UpdateManagerEvent e) {	    
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void updateCompleted(UpdateManagerEvent e) {	    
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void updateFailed(UpdateManagerEvent e) {
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void gvtRenderingStarted(GVTTreeRendererEvent e) {
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void gvtRenderingCompleted(GVTTreeRendererEvent e) {	    
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void gvtRenderingCancelled(GVTTreeRendererEvent e) {	    
		getVerboseStream().outputVerboseStream("11111111111");
	    }
	    @Override
	    public void gvtRenderingFailed(GVTTreeRendererEvent e) {
		    getVerboseStream().outputVerboseStream("11111111111");
	    }
	    
	    //JGVTComponentListener
	    @Override
	    public void componentTransformChanged(ComponentEvent event) {
		
		AffineTransform at = ((JSVGCanvas)event.getComponent()).getRenderingTransform();		
		renderingTranform = at;
	    }
	    
	    public AffineTransform getRenderingTransform(){
		return renderingTranform;
	    }
	}
}
