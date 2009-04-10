/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.detailspanel;

import app.navigps.gui.borders.EmptyOvalBorder;
import app.navigps.gui.borders.RoundBorder;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.Border;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class RoundJPanel extends AlphaJPanel{

    private float innerCornerW;
    private float innerCornerH;

    /**
     *
     */
    public RoundJPanel(){
        this(20,20);
    }

    /**
     *
     * @param recW
     * @param recH
     */
    public RoundJPanel(float recW,float recH){
        setOpaque(false);
        setBorder(new EmptyOvalBorder(recW,recH));
        setInnerCorners(recW, recH);
    }

    /**
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    /**
     *
     * @param g
     */
    @Override
    protected void paintChildren(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;

        Rectangle oldClip = g.getClipBounds();        
        RoundRectangle2D vis = computeVisibleChildrenArea();                
        Area inner = new Area(oldClip);
        Area outer = new Area(vis);
        inner.intersect(outer);
        //Rectangle2D rec2 = inner.getBounds2D();
        //System.out.println("visible rect x: "+rec2.getX()+" y: "+rec2.getY()+" w: "+rec2.getWidth()+" h: "+rec2.getHeight());
        GeneralPath gp = new GeneralPath(inner);
        g2.setClip(gp);
        super.paintChildren(g2);        
    }

    /**
     *
     * @return
     */
    public RoundRectangle2D.Double computeVisibleChildrenArea(){
        Rectangle bounds = getBounds();
        Insets ins = super.getInsets();
        int canX = ins.left;
        int canY = ins.top;
        int canWidth = bounds.width-ins.left-ins.right;
        int canHeight = bounds.height-ins.top-ins.bottom;
        float arcx = getInnerCornerW();
        float arcy = getInnerCornerH();
        return new RoundRectangle2D.Double(canX, canY, canWidth, canHeight, arcx,arcy);
    }

    /**
     *
     * @param arcW
     * @param arcH
     */
    public void setOuterCorners(float arcW,float arcH){
        RoundBorder bord = getRoundBorder();
        bord.setRecW(arcW);
        bord.setRecH(arcH);
    }

    /**
     *
     * @param val
     */
    public void setOuterCorners(Point2D.Float val){
        setOuterCorners((float)val.getX(),(float)val.getY());
    }

    /**
     *
     * @return
     */
    public Point2D getOuterCorners(){
        RoundBorder bord = getRoundBorder();
        return new Point2D.Float(bord.getRecW(),bord.getRecH());
    }

    /**
     *
     * @param border
     */
    public void setBorder(RoundBorder border) {
        super.setBorder(border);
    }

    /**
     *
     * @return
     */
    public RoundBorder getRoundBorder() {
        return (RoundBorder)super.getBorder();
    }

    public void setInsets(Insets ins){
        RoundBorder rb = getRoundBorder();
        rb.setBorderInsets(ins);
    }

    /**
     *
     * @return
     * @deprecated
     */
    @Deprecated
    @Override
    public Border getBorder() {
        return super.getBorder();
    }

    /**
     *
     * @param border
     * @deprecated
     */
    @Deprecated
    @Override
    public void setBorder(Border border) {
        if(border instanceof RoundBorder)
            super.setBorder(border);
    }

    @Override
    public Graphics getGraphics() {
        Graphics g = super.getGraphics();
        Point2D p = getOuterCorners();
        g.setClip(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),p.getX(),p.getY()));
        return g;
    }

    /**
     * @return the innerCornerW
     */
    public float getInnerCornerW() {
        return innerCornerW;
    }

    /**
     * @param innerCornerW the innerCornerW to set
     */
    public void setInnerCornerW(float innerCornerW) {
        this.innerCornerW = innerCornerW;
    }

    /**
     * @return the innerCornerH
     */
    public float getInnerCornerH() {
        return innerCornerH;
    }

    /**
     * @param innerCornerH the innerCornerH to set
     */
    public void setInnerCornerH(float innerCornerH) {
        this.innerCornerH = innerCornerH;
    }

    public void setInnerCorners(float arcW,float arcH){
        setInnerCornerW(arcW);
        setInnerCornerH(arcH);
    }

    /**
     *
     * @param val
     */
    public void setInnerCorners(Point2D.Float val){
        setInnerCorners((float)val.getX(),(float)val.getY());
    }

    public Point2D getInnerCorners(){
        return new Point2D.Float(getInnerCornerW(),getInnerCornerH());
    }
}
