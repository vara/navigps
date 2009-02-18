/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import app.gui.borders.EmptyOvalBorder;
import app.gui.borders.RoundBorder;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author wara
 */
public class RoundJPanel extends AlphaJPanel{

    public RoundJPanel(){
        this(20,20);
        setOpaque(false);
    }

    public RoundJPanel(double recW,double recH){
        setOpaque(false);
        setBorder(new EmptyOvalBorder(recW,recH));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    @Override
    protected void paintChildren(Graphics g) {

        Rectangle oldClip = g.getClipBounds();
        Rectangle newClip = (Rectangle)oldClip.clone();
        RoundRectangle2D vis = computeVisibleChildrenArea();

        SwingUtilities.computeIntersection((int)vis.getX(),(int)vis.getY(),
                (int)vis.getWidth(),(int)vis.getHeight(), newClip);
        //g.setClip(new RoundRectangle2D.Double(newClip.x,newClip.y,
          //          newClip.width,newClip.height,vis.getArcWidth(),vis.getArcHeight()));
        g.setClip(new RoundRectangle2D.Double(newClip.x,newClip.y,
                    newClip.width,newClip.height,0,0));
        
        super.paintChildren(g);
        //g.setClip(oldClip);
    }


    protected RoundRectangle2D.Double computeVisibleChildrenArea(){
        Rectangle bounds = getBounds();
        Insets ins = super.getInsets();
        int canX = ins.left;
        int canY = ins.top;
        int canWidth = bounds.width-ins.left-ins.right;
        int canHeight = bounds.height-ins.top-ins.bottom;
        double arcx = getRoundBorder().getRecW();
        double arcy = getRoundBorder().getRecH();
        return new RoundRectangle2D.Double(canX, canY, canWidth, canHeight, arcx,arcy);
    }

    public void setRoundCorner(double arcW,double arcH){
        RoundBorder bord = getRoundBorder();
        bord.setRecW(arcW);
        bord.setRecH(arcH);
    }

    public void setRoundCorner(Point2D.Double val){
        setRoundCorner(val.getX(),val.getY());
    }

    public Point2D.Double getRoundCorner(){
        RoundBorder bord = getRoundBorder();
        return new Point2D.Double(bord.getRecW(),bord.getRecH());
    }

    public void setBorder(RoundBorder border) {
        super.setBorder(border);
    }

    public RoundBorder getRoundBorder() {
        return (RoundBorder)super.getBorder();
    }

    @Deprecated
    @Override
    public Border getBorder() {
        return super.getBorder();
    }

    @Deprecated
    @Override
    public void setBorder(Border border) {
        if(border instanceof RoundBorder)
            super.setBorder(border);
    }

}
