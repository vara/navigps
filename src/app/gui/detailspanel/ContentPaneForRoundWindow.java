/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import app.gui.borders.OvalBorder;
import app.utils.Utils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.SwingUtilities;

/**
 *
 * @author wara
 */
public class ContentPaneForRoundWindow extends AlphaJPanel implements AlphaInterface{


    private Color [] colorBorderEfect = {new Color(200,200,255,200),
                                         new Color(255,255,255,100)};

    public ContentPaneForRoundWindow() {
        //setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {        
        if(getAlpha()>0){
            super.paintComponent(g); //set alpha for content pane
            
            Graphics2D g2 = (Graphics2D)g.create();
            RoundRectangle2D borderShape = OvalBorder.createOuterShape(2,2,
                        getWidth()-4,getHeight()-5,20, 20,null);

            GradientPaint gp2 = new GradientPaint(0.0f, (float) getHeight(),Utils.colorAlpha(50,50,50,getAlpha()),
                                                    0.0f, 0.0f,Utils.colorAlpha(90,122,166,getAlpha()));
            g2.setPaint(gp2);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.fillRoundRect((int)borderShape.getX(), (int)borderShape.getY(),
                             (int)borderShape.getWidth(), (int)borderShape.getHeight(), 20, 20);

            AlphaComposite newComposite =
                 AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,getAlpha());
            g2.setComposite(newComposite);

            BorderEfects.paintBorderShadow(g2,3,borderShape,colorBorderEfect);
            g2.dispose();
        }
    }

    @Override
    public void paintChildren(Graphics g){        
        if(getAlpha()>0){
            Graphics2D g2 = (Graphics2D)g;
            Rectangle oldClip = g2.getClipBounds();
            Rectangle newClip = (Rectangle)oldClip.clone();
            RoundRectangle2D vis = computeVisibleChildrenArea();

            SwingUtilities.computeIntersection((int)vis.getX(),(int)vis.getY(),
                    (int)vis.getWidth(),(int)vis.getHeight(), newClip);
            g.setClip(newClip);
            super.paintChildren(g2);
            g.setClip(oldClip);
        }
    }
    public RoundRectangle2D.Double computeVisibleChildrenArea(){
        Rectangle bounds = getBounds();
        Insets ins = super.getInsets();
        int canX = ins.left;
        int canY = ins.top;
        int canWidth = bounds.width-ins.left-ins.right;
        int canHeight = bounds.height-ins.top-ins.bottom;

        double arcx = 0;
        double arcy = 0;
        if(getBorder() instanceof OvalBorder){
            arcx = ((OvalBorder)getBorder()).getRecW();
            arcy = ((OvalBorder)getBorder()).getRecH();
        }
        return new RoundRectangle2D.Double(canX, canY, canWidth, canHeight, arcx,arcy);
    }

    @Override
    protected void paintBorder(Graphics g) {
        if(getBorder()instanceof AlphaInterface){
            ((AlphaInterface)getBorder()).setAlpha(getAlpha());
        }
        super.paintBorder(g);
    }
}
