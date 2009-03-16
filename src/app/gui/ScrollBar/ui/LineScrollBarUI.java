/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.ScrollBar.ui;

import app.utils.Utils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.metal.MetalScrollBarUI;

/**
 *
 * @author wara
 */
public class LineScrollBarUI extends MetalScrollBarUI {

    private Color darkShadowColor = new Color(100,100,100,200);
    private Color shadowColor = new Color(200,200,200,200);
    private Color highlightColor = new Color(200,255,200,200);
    /**
     *
     */
    @Override
    protected void installDefaults() {
        super.installDefaults();
        scrollBarWidth = 13;
    }

    /**
     *
     * @param g
     * @param c
     * @param trackBounds
     */
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        //super.paintTrack(g, c, trackBounds);
        Graphics2D g2 = (Graphics2D)g;
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate( trackBounds.x, trackBounds.y );
        boolean leftToRight = c.getComponentOrientation().isLeftToRight();
        if ( scrollbar.getOrientation() == JScrollBar.VERTICAL ){
            if ( !isFreeStanding ) {
                    trackBounds.width += 2;
                    if ( !leftToRight ) {
                        g2.translate( -1, 0 );
                    }
            }
            if ( c.isEnabled() ) {// VERTICAL
                g2.setColor(darkShadowColor);
                int halfArea = trackBounds.width/2;
                g2.drawLine( halfArea, 3,halfArea, trackBounds.height );
                g2.setColor(shadowColor);
                for(int i=1;i<3;i++){
                    int delta = i*50;
                    g2.setColor(Utils.checkColor(darkShadowColor.getRed()+delta,
                            darkShadowColor.getGreen()+delta,
                            darkShadowColor.getBlue()+delta));
                    g2.drawLine( halfArea + i, 3-i, halfArea + i, trackBounds.height - i );
                }
            } else {
                //MetalUtils.drawDisabledBorder(g, 0, 0, trackBounds.width, trackBounds.height );
            }

            if ( !isFreeStanding ) {
                trackBounds.width -= 2;
                if ( !leftToRight ) {
                    g2.translate( 1, 0 );
                }
            }
        }
        else{  // HORIZONTAL

            if ( !isFreeStanding ) {
                trackBounds.height += 2;
            }
            if ( c.isEnabled() ) {
                g2.setColor( darkShadowColor );
                int halfArea = trackBounds.height/2;

                g2.drawLine( 0,halfArea, trackBounds.width - 3,halfArea);
                g2.setColor( shadowColor );
                for (int i = 1; i < 3; i++){
                    int delta = i*50;
                    g2.setColor(Utils.checkColor(darkShadowColor.getRed()+delta,
                            darkShadowColor.getGreen()+delta,
                            darkShadowColor.getBlue()+delta));
                    g2.drawLine( i,halfArea+i, trackBounds.width-2,halfArea+i);
                }

            } else {
                //MetalUtils.drawDisabledBorder(g, 0, 0, trackBounds.width, trackBounds.height );
            }
            if ( !isFreeStanding ) {
                trackBounds.height -= 2;
            }
        }
        g2.translate( -trackBounds.x, -trackBounds.y );
    }
}