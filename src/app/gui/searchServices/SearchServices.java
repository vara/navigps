/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.searchServices;

import app.gui.detailspanel.DetailsPanelForSearchServices;
import app.utils.MyLogger;
import app.utils.OutputVerboseStream;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import odb.inter.ODBridge;
import org.apache.batik.bridge.UpdateManagerEvent;
import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.JGVTComponentListener;

/**
 *
 * @author vara
 */
public class SearchServices extends JComponent implements MouseListener,
							  MouseMotionListener,ComponentListener{

	private Point.Double centerPoint = new Point.Double(0,0);
	private Point.Double currentPos = new Point.Double(0,0);	
	private double radius=0;
	
	private DocumentStateChangedListener listeners;	
	private OutputVerboseStream verboseStream = null;	
	private boolean enabled = false;
	
	private ODBridge odbConnector=null;

    private DetailsPanelForSearchServices detailsPane;
    private SearchServicesPanel guiForSearchServ = new SearchServicesPanel();
    Rectangle visibleRec = new Rectangle(0,0,0,0);

	public SearchServices(OutputVerboseStream l){
	    verboseStream = l;
	    init();	    
	}
	private void init(){
        setLayout(null);
	    listeners = new DocumentStateChangedListener();
        add( (detailsPane = new DetailsPanelForSearchServices(getVerboseStream())) );
        detailsPane.add(guiForSearchServ);

        //add(new GraphicsVisualVerboseMode());
        addComponentListener(this);
        //don't paint background
        setOpaque(false);
	}
	
    @Override
	public void setEnabled(boolean val){
        super.setEnabled(val);
	    enabled = val;
	    //setVisible(enabled);
	    setRadius(0.0);
	    setCenterPoint(0.0,0.0);
	    setCurrentPosition(0.0,0.0);
	    //repaint();
	}
	public boolean isEnabledSearchServices(){
	    return enabled;	    
	}	
	
	public void initODBConnector(ODBridge odbb){
	    odbConnector = odbb;
	}
	@Override
	public void paintComponent(Graphics g){
	    //super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D)g.create();
	    //g2.draw(visibleRec);
	    if(isEnabledSearchServices()){
            paintCircle(g,getRadius(),getCenterPoint(),getCurrentPosition());                        
            g2.dispose();
	    }else{
				
	    }
	}
	protected static void paintCircle(Graphics g,double radius,Point.Double center,Point.Double currPos){
        Graphics2D g2 = (Graphics2D)g.create();
        float dash[] = { 10.0f};
        float widthStroke  = 1.0f;
        BasicStroke bsLine = new BasicStroke(widthStroke);
        BasicStroke bsCircle = new BasicStroke(widthStroke, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Color fillBacgroundColor = new Color(0,150,255,100);
        Ellipse2D circle = new Ellipse2D.Double(-radius,-radius,radius*2,radius*2);
        Ellipse2D centerCircle = new Ellipse2D.Double(center.x-2,center.y-2,4,4);

        g2.setStroke(bsLine);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GREEN);
        g2.fill(centerCircle);
        g2.setColor(Color.BLACK);
        g2.drawLine((int)center.x,(int)center.y,(int)currPos.x,(int)currPos.y);
        g2.setStroke(bsCircle);
        g2.translate(center.x,center.y);
        g2.draw(circle);

        g2.setColor(fillBacgroundColor);
        g2.fill(circle);
        g2.translate(-center.x,-center.y);
        g2.setStroke(bsLine);
        g2.dispose();
    }

	public DocumentStateChangedListener getDocumentStateChanged(){
	    return listeners;
	}
	
	public void setCenterPoint(double x,double y){	            
	    centerPoint.setLocation(x, y);
        guiForSearchServ.setCenterPoint(getCenterPoint());
	}
	public void setRadius(double r){
	    radius =r;
	}
	public double getRadius(){
	    return radius;
	}
	public void setCurrentPosition(double x,double y){	    
	    currentPos.setLocation(x, y);
	    setRadius(centerPoint.distance(getCurrentPosition()));
        guiForSearchServ.setRadius(getRadius());
        guiForSearchServ.setCurrentPos(getCurrentPosition());
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
        
	    AffineTransform at = new AffineTransform(listeners.getInvTransform());
	    double xx = (oldPoint.getX()*at.getScaleX())+at.getTranslateX();
	    double yy = (oldPoint.getY()*at.getScaleY())+at.getTranslateY();
	    oldPoint.setLocation(xx, yy);
	    return oldPoint;
	}
	
	public OutputVerboseStream getVerboseStream(){
	    return verboseStream;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

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
            int gap = 10;
            int x = (int)(getCenterPoint().getX()-getRadius()-gap);
            int y = (int)(getCenterPoint().getY()-getRadius()-gap);
            int w = (int)(getRadius()*2+gap*2);
            int h = w;

            if(x<0){ w+=x; x=0; }
            if(y<0){ h+=y; y=0; }
            
            if(visibleRec.getWidth()>w || visibleRec.getHeight()>h){                
                repaint(visibleRec);
            }
            getVerboseStream().outputVerboseStream("Search Services : Visible Rect. "+visibleRec);
            visibleRec = new Rectangle(x,y,w,h);
            repaint(visibleRec);
	    }
	}	
	@Override
	public void mouseMoved(MouseEvent e) {}

    @Override
    public void componentResized(ComponentEvent e) {        
        new Thread(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {                        
                        detailsPane.updateMyUI();
                    }
                });
            }
        }).start();
        
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
							  UpdateManagerListener{

        private AffineTransform rendTransform = new AffineTransform();
        private AffineTransform invTransform = new AffineTransform();

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
		
            JSVGCanvas canvas = (JSVGCanvas)event.getComponent();
            CanvasGraphicsNode gn = canvas.getCanvasGraphicsNode();
            if(gn!=null){
                AffineTransform at = gn.getGlobalTransform();
                setRenderingTransform(at);                
                if(at.getDeterminant() != 0){
                    try {
                        AffineTransform inv = at.createInverse();
                        setInvTransform(inv);
                    } catch (NoninvertibleTransformException ex) {
                        MyLogger.log.log(Level.SEVERE, null, ex);
                        getVerboseStream().outputErrorVerboseStream(""+ex);
                    }
                }else setInvTransform(at);
            }else{
                getVerboseStream().outputErrorVerboseStream("Canvas Graphics Node == null");
            }
        }

        public AffineTransform getRenderingTransform(){
            return rendTransform;
        }

        /**
         * @param renderingTranform the renderingTranform to set
         */
        public void setRenderingTransform(AffineTransform renderingTranform) {
            this.rendTransform = renderingTranform;
        }

        /**
         * @return the invTransform
         */
        public AffineTransform getInvTransform() {
            return invTransform;
        }

        /**
         * @param invTransform the invTransform to set
         */
        public void setInvTransform(AffineTransform invTransform) {
            this.invTransform = invTransform;
        }
	}//DocumentStateChangedListener
}
