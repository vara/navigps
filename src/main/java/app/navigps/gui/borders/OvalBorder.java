/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.borders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * Created on 2008-12-09, 04:50:17
 * @author vara
 */

public class OvalBorder extends RoundBorder{
     
     private Color borderColor=new Color(90,100,190,255);
     private Insets insets = new Insets(4, 4, 4, 4);

     /**
      *
      */
     public OvalBorder() {
     }
     
     /**
      *
      * @param recw
      * @param rech
      */
     public OvalBorder(float recw, float rech) {
        super(recw,rech);
     }

     /**
      *
      * @param recw
      * @param rech
      * @param topColor
      */
     public OvalBorder(float recw, float rech, Color topColor) {
        this(recw,rech);
        borderColor = topColor;
        setAlpha((float)borderColor.getAlpha()/255);
        setUpperThresholdAlpha(getAlpha());
     }

     /**
      *
      * @param top
      * @param left
      * @param bottom
      * @param right
      * @param recw
      * @param rech
      * @param topColor
      */
     public OvalBorder(int top, int left, int bottom, int right,float recw, float rech, Color topColor) {
        this(recw,rech);
        insets = new Insets(top, left, bottom, right);
        borderColor = topColor;
     }

     /**
      *
      * @param top
      * @param left
      * @param bottom
      * @param right
      * @param topColor
      */
     public OvalBorder(int top, int left, int bottom, int right,Color topColor) {
        insets = new Insets(top, left, bottom, right);
        borderColor = topColor;
     }

     /**
      *
      * @param top
      * @param left
      * @param bottom
      * @param right
      */
     public OvalBorder(int top, int left, int bottom, int right) {
        insets = new Insets(top, left, bottom, right);
     }
     /**
      *
      * @param top
      * @param left
      * @param bottom
      * @param right
      * @param recw
      * @param rech
      */
     public OvalBorder(int top, int left, int bottom, int right,float recw, float rech) {
        this(top, left, bottom, right);
        setRecH(rech);
        setRecW(recw);
     }
     /**
      *
      * @param c
      * @return
      */
     @Override
     public Insets getBorderInsets(Component c) {
        return getInsets();
     }

     /**
      *
      * @return
      */
     @Override
     public boolean isBorderOpaque() { return true; }

     /**
      *
      * @param c
      * @param g
      * @param x
      * @param y
      * @param w
      * @param h
      */
     @Override
     public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        if(getAlpha()>0){
            super.paintBorder(c, g, x, y, h, h);
            RoundRectangle2D border = createOuterShape(x,y,w-1,h-1,getRecW(),getRecH(),getInsets());
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setColor(getBorderColor());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.draw(border);
            g2.dispose();
        }
     }

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param arcx
     * @param arcy
     * @param insetsOuter
     * @return
     */
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

    /**
     *
     * @param ins
     */
    @Override
    public void setBorderInsets(Insets ins) {
        setInsets(ins);
    }
}