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

/**
 * Created on 2008-12-10, 03:55:21
 * @author vara
 */
public class DoubleOvalBorder extends OvalBorder{
  
    private double roundInnerX = 20;
    private double roundInnerY = 20;

    //private Color colorForOuterBorder = new Color(90,100,190,255);
    private Color colorForInnerBorder = new Color(90, 100, 190, 255);
    //private Insets insetsOuter = new Insets(6, 6, 6, 6);
    private Insets insetsInner = new Insets(6, 10, 6, 10);
    private int thickness=0;

    public DoubleOvalBorder(int w, int h) {
        super(w, h);
    }

    public DoubleOvalBorder(int arcw1, int arch1, Color topColor1,int arcw2, int arch2, Color topColor2) {
       super(arcw1, arch1, topColor1);
       roundInnerX=arcw2;
       roundInnerY=arch2;
       colorForInnerBorder = topColor2;
    }

    public DoubleOvalBorder(int arcw1, int arch1, Color topColor1,int arcw2, int arch2, Color topColor2,int tick) {
       this(arcw1, arch1, topColor1, arcw2, arch2, topColor2);
       thickness=tick;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(getInsetsInner().top+getInsetsOuter().top,getInsetsInner().left+getInsetsOuter().left,
                getInsetsInner().bottom+getInsetsOuter().bottom, getInsetsInner().right+getInsetsOuter().right);
    }

    @Override
    public boolean isBorderOpaque() { return true; }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {

        RoundRectangle2D outerBorder = DoubleOvalBorder.createOuterShape(x, y, w-1,h-1,getRoundOuterX(),getRoundOuterY(), getInsetsOuter());
        RoundRectangle2D innerBorder = DoubleOvalBorder.createInnerShape(outerBorder.getX(),outerBorder.getY(),
                outerBorder.getWidth(),outerBorder.getHeight(),getRoundInnerX(),getRoundInnerY(), getInsetsInner());

        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        float alpha = (float) ((float) getColorForOuterBorder().getAlpha() * 0.003921568627450980392);
        AlphaComposite newComposite =
               AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha);
        g2.setComposite(newComposite);
        g2.setColor(getColorForOuterBorder());

        g2.draw(outerBorder);
        alpha = (float) ((float) getColorForInnerBorder().getAlpha() * 0.003921568627450980392);
        newComposite =
               AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha);
        g2.setComposite(newComposite);
        g2.setColor(getColorForInnerBorder());
        g2.draw(innerBorder);

        g2.dispose();
     }
    
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
        return insetsInner;
    }

    /**
     * @param insetsInner the insetsInner to set
     */
    public void setInsetsInner(Insets insetsInner) {
        this.insetsInner = insetsInner;
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
     * @return the roundOuterX
     */
    public double getRoundOuterX() {
        return super.getRecW();
    }

    /**
     * @param roundOuterX the roundOuterX to set
     */
    public void setRoundOuterX(double roundOuterX) {
        super.setRecW(roundOuterX);
    }

    /**
     * @return the roundOuterY
     */
    public double getRoundOuterY() {
        return super.getRecH();
    }

    /**
     * @param roundOuterY the roundOuterY to set
     */
    public void setRoundOuterY(double roundOuterY) {
        super.setRecH(roundOuterY);
    }

    /**
     * @return the roundInnerX
     */
    public double getRoundInnerX() {
        return roundInnerX;
    }

    /**
     * @param roundInnerX the roundInnerX to set
     */
    public void setRoundInnerX(int roundInnerX) {
        this.roundInnerX = roundInnerX;
    }

    /**
     * @return the roundInnerY
     */
    public double getRoundInnerY() {
        return roundInnerY;
    }

    /**
     * @param roundInnerY the roundInnerY to set
     */
    public void setRoundInnerY(double roundInnerY) {
        this.roundInnerY = roundInnerY;
    }
}
