/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 *
 * @author wara
 */
public class BorderEfects {

    public static  void paintBorderShadow(Graphics2D g2, int shadowWidth,Shape clipShape,Color [] col) {
        int sw = shadowWidth*2;
        for (int i=sw; i >= 2; i-=1) {
            float pct = (float)(sw - i) / (sw - 1);
            g2.setColor(getMixedColor(col[0], 1.0f-pct,
                                      col[1], pct));

            g2.setStroke(new BasicStroke(i/1.7f));
            g2.draw(clipShape);
        }
    }
    public static Color getMixedColor(Color c1, float pct1, Color c2, float pct2) {
        float[] clr1 = c1.getComponents(null);
        float[] clr2 = c2.getComponents(null);        
        for (int i = 0; i < clr1.length-1; i++) {
            clr1[i] = (clr1[i] * pct1) + (clr2[i] * pct2);
        }
        clr1[3] = (clr1[3]+clr2[3])/2;
        return new Color(clr1[0], clr1[1], clr1[2],clr1[3]);
    }

    /*
     * Color[] array must be 4 items
     *
     *      col[0] clrGlowInnerHi
     *      col[1] clrGlowOuterHi
     *      col[2] clrGlowInnerLo
     *      col[3] clrGlowOuterLo
     */
    public static void paintBorderGlow(Graphics2D g2, int glowWidth,Shape clipShape,Color[] col) {

        float mainAlpha=1;
        if(g2.getComposite()instanceof AlphaComposite)
            mainAlpha = ((AlphaComposite)g2.getComposite()).getAlpha();
        
        int gw = glowWidth*2;
        for (int i=gw; i >= 2; i-=2) {
            float pct = (float)(gw - i) / (gw - 1);

            Color mixHi = getMixedColor(col[0], pct,
                                       col[1], 1.0f - pct);
            Color mixLo = getMixedColor(col[2], pct,
                                      col[3], 1.0f - pct);

            g2.setPaint(new GradientPaint(0.0f, clipShape.getBounds().height*0.15f,  mixHi,
                                          0.0f, clipShape.getBounds().height, mixLo));

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (mainAlpha <= pct) ? mainAlpha : pct));
            g2.setStroke(new BasicStroke(i));
            g2.draw(clipShape);
        }
    }
}
