/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

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

     protected int recw=20;
     protected int rech=20;
     protected Color borderColor=new Color(90,100,190,10);
     protected Insets insets = new Insets(6,10,6,10);
     public OvalBorder() {
             recw=6;
             rech=6;
     }

     public OvalBorder(int recw, int rech) {
             this.recw=recw;
             this.rech=rech;
     }

     public OvalBorder(int recw, int rech, Color topColor) {
        this.recw=recw;
        this.rech=rech;
        borderColor = topColor;
     }

     public OvalBorder(int top, int left, int bottom, int right,int recw, int rech, Color topColor) {
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

    @Override
     public Insets getBorderInsets(Component c) {
        return insets;
     }

    @Override
     public boolean isBorderOpaque() { return true; }

    @Override
     public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {        
        
        RoundRectangle2D border = new RoundRectangle2D.Double(x,y,w-1,h-1,recw,rech);
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        float alpha = (float)borderColor.getAlpha()/255;
        AlphaComposite newComposite =
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
        g2.setComposite(newComposite);
        g2.setColor(borderColor);
        g2.draw(border);
        g2.dispose();
     }

}