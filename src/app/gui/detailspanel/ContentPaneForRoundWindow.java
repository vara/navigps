/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import app.gui.borders.OvalBorder;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author wara
 */
public class ContentPaneForRoundWindow extends RoundJPanel implements AlphaInterface{


    private Color [] colorBorderEfect = {new Color(200,200,255,200),
                                         new Color(255,255,255,100)};

    /**
     *
     */
    public ContentPaneForRoundWindow() {
    }

    /**
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {        
        if(getAlpha()>0){
            super.paintComponent(g); //set alpha for content pane
            
            Graphics2D g2 = (Graphics2D)g.create();
            Point2D roundCorner = getRoundCorner();
            RoundRectangle2D borderShape = OvalBorder.createOuterShape(2,2,
                        getWidth()-4,getHeight()-5,roundCorner.getX(), roundCorner.getX(),null);

            GradientPaint gp2 = new GradientPaint(0.0f, (float) getHeight(),new Color(50,50,50,255),
                                                    0.0f, 0.0f,new Color(90,122,166,255));
            g2.setPaint(gp2);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.fillRoundRect((int)borderShape.getX(), (int)borderShape.getY(),
                             (int)borderShape.getWidth(), (int)borderShape.getHeight(),
                             (int)borderShape.getArcWidth(), (int)borderShape.getArcHeight());

            BorderEfects.paintBorderShadow(g2,3,borderShape,colorBorderEfect);
            g2.dispose();
        }
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintChildren(Graphics g){        
        if(getAlpha()>0){            
            super.paintChildren(g);
        }
    }    

    /**
     *
     * @param g
     */
    @Override
    protected void paintBorder(Graphics g) {
        getRoundBorder().setAlpha(getAlpha());
        super.paintBorder(g);
    }
}
