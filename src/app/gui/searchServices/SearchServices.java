/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.searchServices;

import app.gui.detailspanel.RoundWindow;
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
import javax.swing.JButton;
import javax.swing.JPanel;
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
public class SearchServices extends JPanel implements MouseListener,
							  MouseMotionListener,ComponentListener{

	private Point.Double centerPoint = new Point.Double(0,0);
	private Point.Double currentPos = new Point.Double(0,0);	
	private double radius=0;
	
	private DocumentStateChangedListener svgViewListener;
	private OutputVerboseStream verboseStream = null;	
	private boolean enabled = false;
	
	private ODBridge odbConnector=null;

    private RoundWindow detailsPane;
    private SearchServicesPanel guiForSearchServ = new SearchServicesPanel();
    private Rectangle visibleRec = new Rectangle(0,0,0,0);
    private boolean needRepaint = false;

    private boolean dragged = false;

    private AffineTransform renderingTransform = AffineTransform.getTranslateInstance(1,1);

	public SearchServices(OutputVerboseStream l){
	    verboseStream = l;
	    init();	    
	}
	private void init(){
        setLayout(null);
	    svgViewListener = new DocumentStateChangedListener();
        detailsPane = new RoundWindow(getVerboseStream());
        detailsPane.setDynamicRevalidate(true);
        detailsPane.setUpperThresholdAlpha(0.6f);
        detailsPane.setAlpha(0.0f);
        detailsPane.getContentPane().setUpperThresholdAlpha(0.75f);
        detailsPane.setTitle("Search Services");
        add(detailsPane);
        detailsPane.getContentPane().add(guiForSearchServ);
        detailsPane.setVisible(false);

        //add(new GraphicsVisualVerboseMode());
        addComponentListener(this);
        //don't paint background
        setOpaque(false);

	}
	
    @Override
	public void setEnabled(boolean val){
        //super.setEnabled(val);
	    enabled = val;
        detailsPane.setEnabled(val);

	    setCenterPoint(0.0,0.0);
	    setCurrentPosition(0.0,0.0);
	    repaint(visibleRec);
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
	    g2.draw(visibleRec);
	    if(isEnabledSearchServices()){
            AffineTransform g2t = g2.getTransform();
            AffineTransform svgRen = svgViewListener.getRenderingTransform();
            AffineTransform svgRenInv = svgViewListener.getInvTransform();
            //System.out.println("component tr. "+g2t);
            //System.out.println("svg rendering tr. "+svgRen);
            //System.out.println("svg inv. rendering tr. "+svgRenInv);
            
            //AffineTransform at = AffineTransform.getTranslateInstance(svgInv.getTranslateX(), svgInv.getTranslateY());
            //System.out.println("Orginal rend tran "+renderingTransform);
            //renderingTransform.concatenate(svgViewListener.getRenderingTransform());
            //System.out.println("Orginal rend tran "+renderingTransform);
            paintCircle(g2,getRadius(),getCenterPoint(),getCurrentPosition());

	    }else{}
	}
	protected static void paintCircle(Graphics2D g2,double radius,Point.Double center,Point.Double currPos){
        
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
    }

	public DocumentStateChangedListener getDocumentStateChanged(){
	    return svgViewListener;
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
	    return convertPointToSvgTransform(oldX, oldY);
	}
	public Point.Double convertPointToSvgTransform(Point.Double oldPoint){
        
	    AffineTransform at = svgViewListener.getRenderingTransform();
        System.out.println(""+at);
	    double xx = (oldPoint.getX()*at.getScaleX())+at.getTranslateX();
	    double yy = (oldPoint.getY()*at.getScaleY())+at.getTranslateY();	    
	    return new Point.Double(xx,yy);
	}

	public void convertShapeOnLayer(AffineTransform at){
        double offsetx = at.getTranslateX();
        double offsety = at.getTranslateY();
        double scalex = at.getScaleX();
        double scaley = at.getScaleY();

        Point.Double cp = getCenterPoint();
        Point.Double newCp = new Point.Double(cp.getX()-offsetx, cp.getY()-offsety);
        setCenterPoint(newCp.getX(),newCp.getY());

        Point.Double currP = getCurrentPosition();
        Point.Double newCurrP = new Point.Double(currP.getX()-offsetx, currP.getY()-offsety);
        setCenterPoint(newCurrP.getX(),newCurrP.getY());

        visibleRec.translate((int)offsetx, (int)offsety);
        repaint(visibleRec);
    }
	public OutputVerboseStream getVerboseStream(){
	    return verboseStream;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
	    if(e.getButton()==MouseEvent.BUTTON1 && !e.isAltDown() && !e.isControlDown()
                && !e.isShiftDown() && isEnabledSearchServices()){

            setDragged(true);
            setCenterPoint(e.getX(),e.getY());
            needRepaint = true;
            //System.out.println(""+e.getX()+","+ e.getY() +" -> "+getCenterPoint().getX()+","+getCenterPoint().getY());
	    }
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
        setDragged(false);
    }
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
		
	    if(isEnabledSearchServices() && isDragged()){

            setCurrentPosition(e.getX(),e.getY());
            int gap = 10;
            int x = (int)(getCenterPoint().getX()-getRadius()-gap);
            int y = (int)(getCenterPoint().getY()-getRadius()-gap);
            int w = (int)(getRadius()*2+gap*2);
            int h = w;

            if(x<0){ w+=x; x=0; }
            if(y<0){ h+=y; y=0; }
            
            if(visibleRec.getWidth()>w || visibleRec.getHeight()>h || needRepaint){
                repaint(visibleRec);
                needRepaint = false;
            }
            //getVerboseStream().outputVerboseStream("Search Services : Visible Rect. "+visibleRec);
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

    /**
     * @return the dragged
     */
    private boolean isDragged() {
        return dragged;
    }

    /**
     * @param dragged the dragged to set
     */
    private void setDragged(boolean dragged) {
        this.dragged = dragged;
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

            AffineTransform at = canvas.getRenderingTransform();
            setRenderingTransform(at);
            if(gn!=null){
                System.err.println("CanvasGraphicsNode != null !!!");
                setInvTransform(gn.getInverseTransform());
                System.out.println("InvTransform =" +getInvTransform());
            }else{
                if(at.getDeterminant() != 0){
                    try {
                        AffineTransform inv = at.createInverse();
                        setInvTransform(inv);
                    } catch (NoninvertibleTransformException ex) {
                        MyLogger.log.log(Level.SEVERE, null, ex);
                        getVerboseStream().outputErrorVerboseStream(""+ex);
                    }
                }else setInvTransform(at);
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
