/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gui.searchServices;

import app.gui.searchServices.swing.SearchServicesPanel;
import app.gui.MainWindowIWD;
import app.gui.detailspanel.AlphaJPanel;
import app.gui.detailspanel.RoundWindow;
import app.gui.detailspanel.RoundWindowUtils;
import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.SVGCanvasLayers;
import app.utils.Console;
import app.utils.NaviPoint;
import app.utils.Utils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import bridge.ODBridge;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.MouseInputAdapter;
import org.apache.batik.dom.svg.AbstractSVGMatrix;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.JGVTComponentListener;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author vara
 */
public class SearchServices extends AlphaJPanel{

    private Canvas svgCanvas;

    private NaviPoint centerPoint = new NaviPoint(0, 0);
    private NaviPoint currentPos = new NaviPoint(0, 0);
    private double radius = 0;

    private NaviPoint paintCenterPoint = new NaviPoint(0,0);
    private NaviPoint paintCurrentPos = new NaviPoint(0,0);
    private double paintRadius = 0;

    private DocumentStateChangedListener svgViewListener = new DocumentStateChangedListener();
    private boolean enabled = false;
    private ODBridge odbConnector = null;

    private RoundWindow roundWindowInstace;
    
    private SearchServicesPanel guiForSearchServ = new SearchServicesPanel();
    //private Rectangle visibleRec = new Rectangle(0, 0, 0, 0);
    //private boolean needRepaint = false;
    private SSMouseEvents me = new SSMouseEvents();
    //private AreaOverLay paintArea = new AreaOverLay();

    PropertyChangeListener removeContent = new PropertyChangeListener(){

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(ALPHA_CHANGE)) {
                float newAlpha = (Float) evt.getNewValue();
                float oldAlpha = (Float) evt.getOldValue();
                //System.out.println("new Alpa "+newAlpha);
                if (setAlpha(newAlpha)) {
                    repaint();
                }
                if (newAlpha < .01f && oldAlpha>newAlpha) {
                    enabled = false;
                    uninstall();
                }
            }
        }
    };

    public SearchServices(Canvas canvas) {
        svgCanvas = canvas;
        setOpaque(false);
        //setAlpha(0.5f);
        //install();
    }

    private void install() {

        //svgCanvas.add(this,BorderLayout.CENTER);        
        svgCanvas.addMouseMotionListener(me);
        svgCanvas.addMouseListener(me);
        svgCanvas.addJGVTComponentListener(svgViewListener);
        if(roundWindowInstace != null){
            roundWindowInstace.addPropertyChangeListener(removeContent);
            roundWindowInstace.getContentPane().add(guiForSearchServ);
        }else{
            // probably never will have a place
            System.err.println(getClass().getCanonicalName()+" method [install] msg: [roundWindowInstace null]");
        }
        System.out.println(getClass().getCanonicalName()+" [install components]");
    }

    public void uninstall(){

        svgCanvas.removeMouseMotionListener(me);
        svgCanvas.removeMouseListener(me);
        svgCanvas.removeJGVTComponentListener(svgViewListener);
        roundWindowInstace.getContentPane().remove(guiForSearchServ);
        roundWindowInstace.removePropertyChangeListener(removeContent);
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
            roundWindowInstace.setIcon(MainWindowIWD.createNavigationIcon("searchServices32"));
            roundWindowInstace.setDynamicRevalidate(true);
            roundWindowInstace.setUpperThresholdAlpha(0.6f);
            roundWindowInstace.setAlpha(0.0f);
            roundWindowInstace.getContentPane().setUpperThresholdAlpha(0.75f);
            roundWindowInstace.setTitle("Search Services");            
            roundWindowInstace.setVisible(false);                        
        } else {
            System.out.println(getClass().getCanonicalName() + " [content roundWin no changed]");
        }
        roundWindowInstace.setEnabled(true);
    }

    @Override
    public void setEnabled(boolean val) {
        //enabled = val;        
        if (val) {            
            Container parent = svgCanvas.getParent();
            enabled = true;
            if (parent instanceof SVGCanvasLayers) {
                Container cont = RoundWindowUtils.getRoundWindowFromContainer(parent);
                if (cont != null) {                                        
                    installRoundWindow((RoundWindow) cont);
                    install();
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

    public void initODBConnector(ODBridge odbb) {
        odbConnector = odbb;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(isEnabledSearchServices() && (int)paintRadius>0){
            Graphics2D g2 = (Graphics2D) g.create();            
            paintCircle(g2, paintRadius, paintCenterPoint,paintCurrentPos);
            g2.dispose();
        }
    }    

    protected static void paintCircle(Graphics2D g2, double radius, NaviPoint center, NaviPoint currPos) {

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

    public void setRadius(double r) {
        radius = r;
    }

    public double getRadius() {
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

        double rad = centerPoint.distance(getCurrentPosition());
        setRadius(rad);
        double radPaint = paintCenterPoint.distance(paintCurrentPos);
        paintRadius = radPaint;

        guiForSearchServ.setRadius(getRadius());
        guiForSearchServ.setCurrentPos(getCurrentPosition());        
    }

    public void updatePoint(){        
        SVGDocument doc = svgCanvas.getSVGDocument();
        if(doc!=null){
            NaviPoint [] tabPoints = {centerPoint,currentPos};
            NaviPoint [] retPoint =
                    Utils.getComponentPointRelativeToDomElement(
                        doc.getRootElement(),tabPoints,null);                 
            paintCenterPoint.setLocation(retPoint[0]);
            paintCurrentPos.setLocation(retPoint[1]);
            paintRadius = paintCenterPoint.distance(paintCurrentPos);
            repaint();
        }
    }

    public NaviPoint getCenterPoint() {
        return centerPoint;
    }

    public NaviPoint getCurrentPosition() {
        return currentPos;
    }

    /*
    *     Mouse listener for search services
    */

    private class SSMouseEvents extends MouseInputAdapter{
        
        private boolean dragged;
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && !e.isAltDown() && !e.isControlDown() && !e.isShiftDown() && isEnabledSearchServices()) {
                
                //NaviPoint p = new NaviPoint(e.getX(),e.getY());
                setCenterPoint(e.getX(), e.getY());
                //needRepaint = true;
                setDragged(true);                
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {

            if (isEnabledSearchServices() && isDragged()) {

                //NaviPoint p = new NaviPoint(e.getX(),e.getY());
                setCurrentPosition(e.getX(),e.getY());
                /*
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
                //System.out.println("Search Services : Visible Rect. "+visibleRec);
                visibleRec = new Rectangle(x,y,w,h);
                */
                //svgViewListener.resetTransform();
                //svgViewListener.updatePos();
                //repaint(visibleRec);                                
                repaint();
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            setDragged(false);
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
    }

    /*
     *  Test for OverLay's list (not worked properly)
     *
    private class AreaOverLay implements Overlay{

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();            

            Shape shape = getPaintingShape();
            NaviPoint centPoint = getCenterPoint();
            g2.translate(centPoint.getX(), centPoint.getY());
            g2.draw(shape);
            g2.translate(-centPoint.getX(), -centPoint.getY());

            g2.dispose();
       }

    }
    */
    private class DocumentStateChangedListener implements JGVTComponentListener{
        //JGVTComponentListener
        @Override
        public void componentTransformChanged(ComponentEvent event) {
            System.err.println("***<SearchServicesListener> svgCanvas transform changed ***");
            updatePoint();
        }
    }//DocumentStateChangedListener
}
