package app.navigps.gui.searchServices;

import app.navigps.gui.searchServices.swing.SearchServicesCoreGUI;
import app.navigps.gui.svgComponents.Canvas;
import app.navigps.gui.svgComponents.SVGCanvasLayers;
import app.navigps.gui.svgComponents.SynchronizedSVGLayer;
import app.navigps.utils.NaviPoint;
import app.navigps.utils.NaviUtilities;
import app.navigps.utils.Utils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.Rectangle;
import javax.swing.event.MouseInputAdapter;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class SearchServices extends SynchronizedSVGLayer{


    private NaviPoint centerPoint = new NaviPoint(0, 0);
    private NaviPoint currentPos = new NaviPoint(0, 0);
    private float radius = 0;

    private NaviPoint paintCenterPoint = new NaviPoint(0,0);
    private NaviPoint paintCurrentPos = new NaviPoint(0,0);
    private float paintRadius = 0;

    private boolean enabled = false;   
    
    private SSMouseEvents me = new SSMouseEvents();   

    private Rectangle visibleArea = new Rectangle(0, 0);

    private SearchServicesCoreGUI coreGUI;

    public SearchServices(Canvas canvas) {
        super(canvas);        
    }

    private void install() {

        svgCanvas.addMouseMotionListener(me);
        svgCanvas.addMouseListener(me);
        SVGCanvasLayers svgcl = NaviUtilities.getSVGCanvasLayers(svgCanvas);
        if(svgcl != null){
            Component [] comps = svgcl.getComponentsInLayer(SVGCanvasLayers.SEARCH_SERVICES_LAYER);
            boolean add = true;
            if(comps.length != 0){
                for (Component c : comps) {
                    if(c == this){
                        add = false;
                    }
                }
            }
            if(add){
                System.err.println("Add component to SVGCanvasLayers.SEARCH_SERVICES_LAYER");
                svgcl.add(this, SVGCanvasLayers.SEARCH_SERVICES_LAYER);
            }
            
        }
        
        if(coreGUI == null){
            coreGUI = new SearchServicesCoreGUI(this);
        }        
    }

    public void uninstall(){      

        svgCanvas.removeMouseMotionListener(me);
        svgCanvas.removeMouseListener(me);
        
        //roundWindowInstace.getContentPane().remove(guiForSearchServ);
        //roundWindowInstace.removePropertyChangeListener(removeContent);
        //roundWindowInstace.getWinBehavior().removeEndAction(closeAction);
        /*
        Container parent = getParent();
        if(parent!=null){
            parent.remove(this);
            parent.repaint();
        }
         */
        System.out.println(getClass().getCanonicalName()+" [uninstall components]");
    }

    public void setEnabledSearchServices(boolean val){
        enabled = val;
        if (val) {      
            install();
        } else {            
            uninstall();
        }
        coreGUI.setVisible(enabled);
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
        coreGUI.getPanel().setCenterPoint(getCenterPoint());
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

        coreGUI.getPanel().setRadius(getRadius());
        coreGUI.getPanel().setCurrentPos(getCurrentPosition());
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

    public void repaintVisibleArea(){
        repaint(visibleArea);
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
                    repaintVisibleArea();
                    needReapint = false;                    
                }
                //calculate repainted area
                visibleArea = paintCenterPoint.createAreaSquareI((float)paintRadius+REPAINT_GAP);
                //If user increment radius then we need repaint bigger area.
                //This block has been used only if radius is incrementing
                if(needReapint){
                    repaintVisibleArea();
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
