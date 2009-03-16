/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.borders;

import app.gui.detailspanel.AlphaInterface;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author wara
 */
public abstract class AlphaBorder extends AbstractBorder implements AlphaInterface{
    private float upperThresholdAlpha = 1f;
    private float alpha = 1f;
    
    /**
     * @return the upperThresholdAlpha
     */
    @Override
    public float getUpperThresholdAlpha() {
        return upperThresholdAlpha;
    }

    /**
     * @param upperThresholdAlpha the upperThresholdAlpha to set
     */
    @Override
    public void setUpperThresholdAlpha(float upperThresholdAlpha) {
        this.upperThresholdAlpha = upperThresholdAlpha;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,getAlpha()));
        super.paintBorder(c, g2, x, y, width, height);
    }

    /**
     * @return the alpha
     */
    @Override
    public float getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     * @return
     */
    @Override
    public boolean setAlpha(float alpha) {
        if(alpha<=getUpperThresholdAlpha()){
            this.alpha = alpha;
            return true;
        }return false;
    }
}
