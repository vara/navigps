/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.borders;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;

/**
 * Created on 2008-12-09, 04:50:17
 * @author vara
 */

public class OvalBorder extends AbstractBorder {

     private double recw=0;
     private double rech=0;
     private Color borderColor=new Color(90,100,190,255);
     private Insets insets = new Insets(6, 10, 6, 10);

     public OvalBorder() {
             recw=6;
             rech=6;
     }

     public OvalBorder(double recw, double rech) {
             this.recw=recw;
             this.rech=rech;
     }

     public OvalBorder(double recw, double rech, Color topColor) {
        this.recw=recw;
        this.rech=rech;
        borderColor = topColor;
     }

     public OvalBorder(int top, int left, int bottom, int right,double recw, double rech, Color topColor) {
        insets = new Insets(top, left, bottom, right);
        this.recw=recw;
        this.rech=rech;
        borderColor = topColor;
     }

     public OvalBorder(int top, int left, int bottom, int right,Color topColor) {
        insets = new Insets(top, left, bottom, right);
        borderColor = topColor;
     }

     public OvalBorder(int top, int left, int bottom, int right) {
        insets = new Insets(top, left, bottom, right);
     }
     public OvalBorder(int top, int left, int bottom, int right,double recw, double rech) {
        this(top, left, bottom, right);
        setRecH(rech);
        setRecW(recw);
     }
    @Override
     public Insets getBorderInsets(Component c) {
        return getInsets();
     }

    @Override
     public boolean isBorderOpaque() { return true; }

    @Override
     public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        RoundRectangle2D border = createOuterShape(x,y,w-1,h-1,getRecW(),getRecH(),getInsets());
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        float alpha = (float)getBorderColor().getAlpha()/255;
        AlphaComposite newComposite =
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
        g2.setComposite(newComposite);
        g2.setColor(getBorderColor());
        g2.draw(border);
        g2.dispose();
     }

    public static RoundRectangle2D.Double createOuterShape(double x, double y, double w, double h,
            double arcx,double arcy,Insets insetsOuter){

        //double outerX=x+insetsOuter.left;
        //double outerY=y+insetsOuter.top;
        //double outerWidth = w-insetsOuter.right-outerX;
        //double outerHeight = h-insetsOuter.bottom-outerY;
        double outerX=x;
        double outerY=y;
        double outerWidth = w;
        double outerHeight = h;
        return new RoundRectangle2D.Double(outerX,outerY,outerWidth,outerHeight,arcx,arcy);
    }

    /**
     * @return the recw
     */
    public double getRecW() {
        return recw;
    }

    /**
     * @param recw the recw to set
     */
    public void setRecW(double recw) {
        this.recw = recw;
    }

    /**
     * @return the rech
     */
    public double getRecH() {
        return rech;
    }

    /**
     * @param rech the rech to set
     */
    public void setRecH(double rech) {
        this.rech = rech;
    }

    /**
     * @return the borderColor
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * @param borderColor the borderColor to set
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * @return the insets
     */
    public Insets getInsets() {
        return insets;
    }

    /**
     * @param insets the insets to set
     */
    public void setInsets(Insets insets) {
        this.insets = insets;
    }

}