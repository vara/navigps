/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.borders;

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

     public OvalBorder() {
     }
     
     public OvalBorder(double recw, double rech) {
        super(recw,rech);
     }

     public OvalBorder(double recw, double rech, Color topColor) {
        this(recw,rech);
        borderColor = topColor;
        setAlpha((float)borderColor.getAlpha()/255);
        setUpperThresholdAlpha(getAlpha());
     }

     public OvalBorder(int top, int left, int bottom, int right,double recw, double rech, Color topColor) {
        this(recw,rech);
        insets = new Insets(top, left, bottom, right);
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

    @Override
    public void setBorderInsets(Insets ins) {
        setInsets(ins);
    }
}