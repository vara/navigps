package app.gui.searchServices;

import app.gui.searchServices.swing.SearchServicesPanel;
import app.gui.MainWindowIWD;
import app.gui.detailspanel.AlphaJPanel;
import app.gui.detailspanel.RoundWindow;
import app.gui.detailspanel.RoundWindowUtils;
import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.SVGCanvasLayers;
import app.utils.NaviPoint;
import app.utils.Utils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.BorderLayout;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.MouseInputAdapter;
import org.apache.batik.dom.svg.SVGOMPoint;
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

    private RoundWindow roundWindowInstace;
    
    private SearchServicesPanel guiForSearchServ = new SearchServicesPanel();
    private SSMouseEvents me = new SSMouseEvents();

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
                if (newAlpha <= .015f && oldAlpha>newAlpha) {
                    setEnabledSearchServices(false);
                    uninstall();
                    enabled = false;
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

        //svgCanvas.add(synch,BorderLayout.CENTER);
        svgCanvas.addMouseMotionListener(me);
        svgCanvas.addMouseListener(me);
        svgCanvas.addJGVTComponentListener(svgViewListener);
        if(roundWindowInstace != null){
            roundWindowInstace.addPropertyChangeListener(removeContent);
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

        removeMouseMotionListener(me);
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
        roundWindowInstace.pack();
        roundWindowInstace.setEnabled(true);
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
        super.paintComponent(g);                
        if(isEnabledSearchServices() && (int)paintRadius>0){
            Graphics2D g2 = (Graphics2D) g.create();
            AffineTransform pt = svgCanvas.getPaintingTransform();
            if(pt!=null){
                AffineTransform orgT = g2.getTransform();
                orgT.concatenate(pt);
                g2.setTransform(orgT);
            }
            paintCircle(g2, paintRadius, paintCenterPoint,paintCurrentPos);
            g2.dispose();
        }
    }    

    protected void paintCircle(Graphics2D g2, double radius, NaviPoint center, NaviPoint currPos) {

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

    /**
     *
     */
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

    private class SSMouseEvents extends MouseInputAdapter{
        
        private boolean dragged;
        private NaviPoint startPoint = new NaviPoint(0,0);

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
                setCurrentPosition(e.getX(),e.getY());
                repaint();
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

    private class DocumentStateChangedListener implements JGVTComponentListener{
        @Override
        public void componentTransformChanged(ComponentEvent event) {
            System.err.println("***<SearchServicesListener> svgCanvas transform changed ***");
            updatePoint();
        }
    }
}
