/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.borders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;

/**
 * Created on 2008-12-10, 03:55:21
 * @author vara
 */
public class DoubleOvalBorder extends OvalBorder{
  
    private OvalBorder innerBorder = new OvalBorder();
    
    private Color colorForInnerBorder = new Color(90, 100, 190, 255);
    private int thickness=0;

    /**
     *
     * @param arcW
     * @param arcH
     */
    public DoubleOvalBorder(int arcW, int arcH) {
        super(arcW, arcH);
        innerBorder.setRound(arcW, arcH);
    }

    /**
     *
     * @param arcw1
     * @param arch1
     * @param topColor1
     * @param arcw2
     * @param arch2
     * @param topColor2
     */
    public DoubleOvalBorder(int arcw1, int arch1, Color topColor1,int arcw2, int arch2, Color topColor2) {
       super(arcw1, arch1, topColor1);
       innerBorder.setRound(arcw2, arch2);
       colorForInnerBorder = topColor2;       
    }

    /**
     *
     * @param arcw1
     * @param arch1
     * @param topColor1
     * @param arcw2
     * @param arch2
     * @param topColor2
     * @param tick
     */
    public DoubleOvalBorder(int arcw1, int arch1, Color topColor1,int arcw2, int arch2, Color topColor2,int tick) {
       this(arcw1, arch1, topColor1, arcw2, arch2, topColor2);
       thickness=tick;
    }

    /**
     *
     * @param c
     * @return
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(getInsetsInner().top+getInsetsOuter().top,getInsetsInner().left+getInsetsOuter().left,
                getInsetsInner().bottom+getInsetsOuter().bottom, getInsetsInner().right+getInsetsOuter().right);
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
        super.paintBorder(c, g, x, y, w, h);
        RoundRectangle2D tmpInnerBorder = DoubleOvalBorder.createInnerShape(x,y,w,h,
                getRoundInnerW(),getRoundInnerH(), getInsetsInner());

        innerBorder.paintBorder(c, g, (int)tmpInnerBorder.getX(), (int)tmpInnerBorder.getY(),
                (int)tmpInnerBorder.getWidth(), (int)tmpInnerBorder.getHeight());
     }
    
    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param arcx
     * @param arcy
     * @param insetsInner
     * @return
     */
    public static RoundRectangle2D.Double createInnerShape(double x, double y, double w, double h,
            double arcx,double arcy,Insets insetsInner){


        double innerX=x+insetsInner.left;
        double innerY=y+insetsInner.top;
        double innerWidth = w-insetsInner.right-innerX;
        double innerHeight = h-insetsInner.bottom-innerY;
        return new RoundRectangle2D.Double(innerX,innerY,innerWidth,innerHeight,arcx,arcy);
    }
    /**
     * @return the colorForOuterBorder
     */
    public Color getColorForOuterBorder() {
        return super.getBorderColor();
    }

    /**
     * @param colorForOuterBorder the colorForOuterBorder to set
     */
    public void setColorForOuterBorder(Color colorForOuterBorder) {
        super.setBorderColor(colorForOuterBorder);
    }

    /**
     * @return the colorForInnerBorder
     */
    public Color getColorForInnerBorder() {
        return colorForInnerBorder;
    }

    /**
     * @param colorForInnerBorder the colorForInnerBorder to set
     */
    public void setColorForInnerBorder(Color colorForInnerBorder) {
        this.colorForInnerBorder = colorForInnerBorder;
    }

    /**
     * @return the insetsOuter
     */
    public Insets getInsetsOuter() {
        return super.getInsets();
    }

    /**
     * @param insetsOuter the insetsOuter to set
     */
    public void setInsetsOuter(Insets insetsOuter) {
        super.setInsets(insetsOuter);
    }

    /**
     * @return the insetsInner
     */
    public Insets getInsetsInner() {
        return innerBorder.getInsets();
    }

    /**
     * @param insetsInner the insetsInner to set
     */
    public void setInsetsInner(Insets insetsInner) {
        innerBorder.setInsets(insetsInner);
    }

    /**
     * @return the thickness
     */
    public int getThickness() {
        return thickness;
    }

    /**
     * @param thickness the thickness to set
     */
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    /**
     * @return the roundInnerW
     */
    public double getRoundInnerW() {
        return innerBorder.getRecW();
    }

    /**
     * @param recW
     */
    public void setRoundInnerW(int recW) {
        innerBorder.setRecW(recW);
    }

    /**
     * @return the roundInnerH
     */
    public double getRoundInnerH() {
        return innerBorder.getRecH();
    }

    /**
     * @param arcH
     */
    public void setRoundInnerH(double arcH) {
        innerBorder.setRecH(arcH);
    }
}
