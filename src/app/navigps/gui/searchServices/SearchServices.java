package app.navigps.gui.searchServices;

import app.navigps.gui.searchServices.swing.SearchServicesPanel;
import app.navigps.gui.NaviRootWindow;
import app.navigps.gui.detailspanel.RoundWindow;
import app.navigps.gui.detailspanel.RoundWindowUtils;
import app.navigps.gui.svgComponents.Canvas;
import app.navigps.gui.svgComponents.SVGCanvasLayers;
import app.navigps.gui.svgComponents.SynchronizedSVGLayer;
import app.navigps.utils.NaviPoint;
import app.navigps.utils.Utils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D.Float;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.MouseInputAdapter;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author vara
 */
public class SearchServices extends SynchronizedSVGLayer{


    private NaviPoint centerPoint = new NaviPoint(0, 0);
    private NaviPoint currentPos = new NaviPoint(0, 0);
    private float radius = 0;

    private NaviPoint paintCenterPoint = new NaviPoint(0,0);
    private NaviPoint paintCurrentPos = new NaviPoint(0,0);
    private float paintRadius = 0;

    private boolean enabled = false;

    private RoundWindow roundWindowInstace;
    
    private SearchServicesPanel guiForSearchServ = new SearchServicesPanel();
    private SSMouseEvents me = new SSMouseEvents();   

    private Rectangle visibleArea = new Rectangle(0, 0);

    private ActionListener closeAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(RoundWindow.CLOSE_WINDOW_ACTION == e.getID()){
                        //System.err.println("id: "+e.getID());
                        //setEnabledSearchServices(false);
                        uninstall();
                        enabled = false;
                    }
                }
            };

    private PropertyChangeListener removeContent = new PropertyChangeListener(){
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(ALPHA_CHANGE)) {
                //System.out.println("new Alpa "+newAlpha);
                if ( setAlpha( (java.lang.Float) evt.getNewValue()) ) {                    
                        repaint(visibleArea);
                }
            }
        }
    };

    public SearchServices(Canvas canvas) {
        super(canvas);
    }

    private void install() {

        //svgCanvas.add(synch,BorderLayout.CENTER);
        System.out.println("Mouse listeners "+svgCanvas.getMouseListeners().length);

        svgCanvas.removeMouseMotionListener(me);
        svgCanvas.removeMouseListener(me);


        svgCanvas.addMouseMotionListener(me);
        svgCanvas.addMouseListener(me);

        System.out.println("Mouse listeners after "+svgCanvas.getMouseListeners().length);
        if(roundWindowInstace != null){
            roundWindowInstace.addPropertyChangeListener(removeContent);
            roundWindowInstace.getWinBehavior().addEndAction(closeAction);
            Container cont = roundWindowInstace.getContentPane();
            cont.setLayout(new BorderLayout());
            cont.add(guiForSearchServ,BorderLayout.CENTER);
        }else{
            // probably never will have a place
            System.err.println(getClass().getCanonicalName()+" method [install] msg: [roundWindowInstace null]");
        }
        System.out.println(getClass().getCanonicalName()+" [install components]");
    }

    public void uninstall(){      

        svgCanvas.removeMouseMotionListener(me);
        svgCanvas.removeMouseListener(me);
        
        roundWindowInstace.getContentPane().remove(guiForSearchServ);
        roundWindowInstace.removePropertyChangeListener(removeContent);
        //roundWindowInstace.getWinBehavior().removeEndAction(closeAction);
        Container parent = getParent();
        if(parent!=null){
            parent.remove(this);
            parent.repaint();
        }
        System.out.println(getClass().getCanonicalName()+" [uninstall components]");
    }

    protected void installRoundWindow(RoundWindow rw) {

        if (roundWindowInstace == null || !roundWindowInstace.equals(rw)) {
            //System.out.println("Initial round window and fill content");
            roundWindowInstace = rw;
            roundWindowInstace.setIcon(NaviRootWindow.createNavigationIcon("searchServices32"));
            roundWindowInstace.setDynamicRevalidate(true);
            roundWindowInstace.setUpperThresholdAlpha(0.6f);
            roundWindowInstace.setAlpha(0.0f);
            roundWindowInstace.getContentPane().setUpperThresholdAlpha(0.75f);
            roundWindowInstace.setTitle("Search Services");            
            roundWindowInstace.setVisible(false);                        
        } else {
            System.out.println(getClass().getCanonicalName() + " [content roundWin no changed]");
        }        
    }

    public void setEnabledSearchServices(boolean val){        
        if (val) {
            Container parent = svgCanvas.getParent();
            enabled = true;
            if (parent instanceof SVGCanvasLayers) {
                Container cont = RoundWindowUtils.getRoundWindowFromContainer(parent);
                if (cont != null) {
                    installRoundWindow((RoundWindow) cont);
                    install();
                    roundWindowInstace.pack();
                    roundWindowInstace.setEnabled(true);
                }

            } else {
                String info = getClass().getCanonicalName() + " method init -> " +
                        "parent SVGCanvasLayers no detect, details window will not be displayed !";
                System.err.println(info);
            }
        } else {
            roundWindowInstace.setEnabled(false);
            //uninstall();
        }

    }
    public boolean isEnabledSearchServices() {
        return enabled;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        //g.drawRect(visibleArea.x,visibleArea.y,visibleArea.width,visibleArea.height);
        if(isEnabledSearchServices() && (int)paintRadius>0){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();            
            paintCircle(g2, paintRadius, paintCenterPoint,paintCurrentPos);            
            g2.dispose();
        }
    }    

    protected void paintCircle(Graphics2D g2, float radius, NaviPoint center, NaviPoint currPos) {

        float dash[] = {10.0f};
        float widthStroke = 1.0f;
        BasicStroke bsLine = new BasicStroke(widthStroke);
        BasicStroke bsCircle = new BasicStroke(widthStroke, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Color fillBacgroundColor = new Color(0, 150, 255, 100);
        Ellipse2D circle = new Ellipse2D.Double(-radius, -radius, radius * 2, radius * 2);
        Ellipse2D centerCircle = new Ellipse2D.Double(center.getX() - 2, center.getY() - 2, 4, 4);

        g2.setStroke(bsLine);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GREEN);
        g2.fill(centerCircle);
        g2.setColor(Color.BLACK);
        g2.drawLine((int) center.getX(), (int) center.getY(), (int) currPos.getX(), (int) currPos.getY());
        g2.setStroke(bsCircle);
        g2.translate(center.getX(), center.getY());
        g2.draw(circle);

        g2.setColor(fillBacgroundColor);
        g2.fill(circle);
        g2.translate(-center.getX(), -center.getY());
        g2.setStroke(bsLine);
    }

    public void setCenterPoint(NaviPoint p){
        setCenterPoint(p.getX(), p.getY());
    }

    public void setCurrentPosition(NaviPoint p) {
        setCurrentPosition(p.getX(), p.getY());
    }

    public void setCenterPoint(float x, float y) {
        paintCenterPoint.setLocation(x, y);
        SVGDocument doc = svgCanvas.getSVGDocument();
        if(doc!=null){
            SVGOMPoint svgPoint = 
                    Utils.getLocalPointFromDomElement(doc.getRootElement(),x,y);
            centerPoint.setLocation(svgPoint);
        }
        guiForSearchServ.setCenterPoint(getCenterPoint());
    }

    public void setRadius(float r) {
        radius = r;
    }

    public float getRadius() {
        return radius;
    }

    public void setCurrentPosition(float x, float y) {
        paintCurrentPos.setLocation(x,y);
        SVGDocument doc = svgCanvas.getSVGDocument();
        if(doc!=null){
            SVGOMPoint svgPoint =
                    Utils.getLocalPointFromDomElement(doc.getRootElement(),(int) x,(int) y);
            currentPos.setLocation(svgPoint);            
        }

        setRadius((float)centerPoint.distance(getCurrentPosition()));
        paintRadius = (float)paintCenterPoint.distance(paintCurrentPos);

        guiForSearchServ.setRadius(getRadius());
        guiForSearchServ.setCurrentPos(getCurrentPosition());        
    }

    @Override
    public void updateComponent(){
        SVGDocument doc = svgCanvas.getSVGDocument();
        if(doc!=null){
            NaviPoint [] tabPoints = {centerPoint,currentPos};
            NaviPoint [] retPoint =
                    Utils.getComponentPointRelativeToDomElement(
                        doc.getRootElement(),tabPoints,null);                 
            paintCenterPoint.setLocation(retPoint[0]);
            paintCurrentPos.setLocation(retPoint[1]);
            paintRadius = (float)paintCenterPoint.distance(paintCurrentPos);

            visibleArea = paintCenterPoint.createAreaSquareI((float)paintRadius+
                                                            SSMouseEvents.REPAINT_GAP);
            needUpdate = false;
        }
    }

    public NaviPoint getCenterPoint() {
        return centerPoint;
    }

    public NaviPoint getCurrentPosition() {
        return currentPos;
    }

    private class SSMouseEvents extends MouseInputAdapter{
        
        private boolean dragged;
        private NaviPoint startPoint = new NaviPoint(0,0);

        public static final int REPAINT_GAP = 10;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && !e.isAltDown() && !e.isControlDown() && !e.isShiftDown() && isEnabledSearchServices()) {
                
                startPoint.setLocation(e.getX(), e.getY());
                setDragged(true);                
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {

            if (isEnabledSearchServices() && isDragged()) {

                setCenterPoint(startPoint);
                float paintRadiusTmp = paintRadius;
                setCurrentPosition(e.getX(),e.getY());

                boolean needReapint = true;
                if(paintRadius < paintRadiusTmp ){
                    // we need repaint only a bigger(current) painted area
                    // only becouse we don't need twice (needRepaint sets on false)
                    repaint(visibleArea);
                    needReapint = false;                    
                }
                //calculate repainted area
                visibleArea = paintCenterPoint.createAreaSquareI((float)paintRadius+REPAINT_GAP);
                //If user increment radius then we need repaint bigger area.
                //This block has been used only if radius is incrementing
                if(needReapint){
                    repaint(visibleArea);                   
                }
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            setDragged(false);
        }

        private boolean isDragged() {
            return dragged;
        }

        private void setDragged(boolean dragged) {
            this.dragged = dragged;
        }
    }
}
