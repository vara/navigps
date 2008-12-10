/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.searchServices;

import app.gui.DetailsPanelForSearchServices;
import app.utils.MyLogger;
import app.utils.OutputVerboseStream;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.logging.Level;
import javax.swing.JComponent;
import javax.swing.JPanel;
import odb.inter.ODBridge;
import org.apache.batik.bridge.UpdateManagerEvent;
import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.JGVTComponentListener;

/**
 *
 * @author vara
 */
public class SearchServices extends JPanel implements MouseListener,
							  MouseMotionListener,ComponentListener
							  
{	
	private Point.Double centerPoint = new Point.Double(0,0);
	private Point.Double currentPos = new Point.Double(0,0);	
	private double radius=0;
	
	private DocumentStateChangedListener listeners;	
	private OutputVerboseStream verboseStream = null;	
	private boolean enabled = false;
	
	private ODBridge odbConnector=null;

    private DetailsPanelForSearchServices detailsPane;
    private SearchServicesPanel guiForSearchServ = new SearchServicesPanel();

	public SearchServices(OutputVerboseStream l){
	    verboseStream = l;
	    init();	    
	}
	private void init(){
        setLayout(null);
	    listeners = new DocumentStateChangedListener();
        add( (detailsPane = new DetailsPanelForSearchServices(getVerboseStream())) );
        detailsPane.add(guiForSearchServ);

        add(new GraphicsVisualVerboseMode());
        addComponentListener(this);
        setOpaque(false);
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
	    Graphics2D g2 = (Graphics2D)g.create();
	    
	    if(isEnabledSearchServices()){
	    
            AffineTransform svgTransform = listeners.getRenderingTransform();
            double offsetx = svgTransform.getTranslateX();
            double offsety = svgTransform.getTranslateY();
            double scalex = svgTransform.getScaleX();
            double scaley = svgTransform.getScaleY();

            AffineTransform oldTr = g2.getTransform();
            AffineTransform newTr = AffineTransform.getTranslateInstance(offsetx, offsety);
            oldTr.concatenate(newTr);
            g2.setTransform(oldTr);

            float dash[] = { 10.0f };
            float widthStroke  = (float)1.0;
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
            g2.setStroke(bsLine);
            g2.dispose();
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
        System.out.println("search");
	    if(e.getButton()==MouseEvent.BUTTON1){
            if(isEnabledSearchServices()){
                setCenterPoint(e.getX(),e.getY());
                //System.out.println(""+e.getX()+","+ e.getY() +" -> "+getCenterPoint().getX()+","+getCenterPoint().getY());
            }
	    }
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {}	
	@Override
	public void mouseEntered(MouseEvent e) {
	    
	    //getVerboseStream().outputVerboseStream("Search services component size "+getSize());
            //getVerboseStream().outputVerboseStream(getClass().getCanonicalName()+" rendering transform "+listeners.getRenderingTransform());
	    //System.out.println("Search services component size "+getSize()+"\nVisible rect "+getVisibleRect());
	    
	}	
	@Override
	public void mouseExited(MouseEvent e) {}	
	@Override	
	public void mouseDragged(MouseEvent e) {
		
	    if(isEnabledSearchServices()){
		setCurrentPosition(e.getX(),e.getY());		
		//System.out.println("drag "+e.getX()+","+ e.getY() +" -> "+getCurrentPosition().getX()+","+getCurrentPosition().getY());
		repaint();
	    }
	}	
	@Override
	public void mouseMoved(MouseEvent e) {}

    @Override
    public void componentResized(ComponentEvent e) {
        getVerboseStream().outputVerboseStream(""+getClass()+" component Resized "+getBounds());
        detailsPane.updateMyUI();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        getVerboseStream().outputVerboseStream(""+getClass()+" component Moved "+getBounds());
    }

    @Override
    public void componentShown(ComponentEvent e) {
        getVerboseStream().outputVerboseStream(""+getClass()+" component Shown "+getBounds());
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        getVerboseStream().outputVerboseStream(""+getClass()+" component Hidden "+getBounds());
    }
	
	public class DocumentStateChangedListener implements JGVTComponentListener,
							  UpdateManagerListener
	{
            private AffineTransform renderingTranform = new AffineTransform();

	    public DocumentStateChangedListener(){}
	    @Override
	    public void managerStarted(UpdateManagerEvent e) {}
	    @Override
	    public void managerSuspended(UpdateManagerEvent e) {}
	    @Override
	    public void managerResumed(UpdateManagerEvent e) {}
	    @Override
	    public void managerStopped(UpdateManagerEvent e) {}
	    @Override
	    public void updateStarted(UpdateManagerEvent e) {}
	    @Override
	    public void updateCompleted(UpdateManagerEvent e) {}
	    @Override
	    public void updateFailed(UpdateManagerEvent e) {}	   
	    
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
        private class GraphicsVisualVerboseMode extends JComponent{

            public GraphicsVisualVerboseMode(){
                setOpaque(false);
                setBounds(0,0,1024,600);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setColor(Color.BLACK);
                AffineTransform atText = AffineTransform.getScaleInstance(1,1);
                AffineTransform mainTra = g2.getTransform();
                mainTra.concatenate(atText);
                g2.setTransform(mainTra);
                String [] strings = {"transform "+listeners.getRenderingTransform(),
                                     "center point "+getCenterPoint(),
                                     "Radius "+getRadius(),
                                     "current point "+getCurrentPosition(),
                                     "getBounds "+getBounds()};

                int locationX = 10;
                int locationY = 20;
                int gapy = 5;
                double minFrameX=50;
                double minFrameY=50;

                FontRenderContext frc = g2.getFontRenderContext();
                Rectangle2D charBaunds = g2.getFont().getMaxCharBounds(frc);

                for (String str : strings) {
                    Rectangle2D rec = g2.getFont().getStringBounds(str, frc);
                    minFrameX = minFrameX = Math.max(minFrameX,rec.getWidth());
                }
                minFrameX+=locationX;
                minFrameY = locationY+(gapy+charBaunds.getHeight()*strings.length);
                Shape frame = new RoundRectangle2D.Double(locationX/2,locationY/3,minFrameX,minFrameY,20,20);
                g2.setClip(new Rectangle2D.Double(locationX/2,locationY/3,minFrameX+10,minFrameY+10));
                super.paintComponent(g2);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,200));
                g2.fill(frame);
                g2.setColor(new Color(255,22,255,255));
                g2.draw(frame);

                g2.setColor(new Color(255,255,255,255));
                for (int i=0;i<strings.length;i++) {
                    g2.drawString(strings[i],locationX,locationY+(float)(i*(gapy+charBaunds.getHeight())) );
                }
            }
        }
}
