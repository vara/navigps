/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.borders;

import app.gui.detailspanel.AlphaInterface;
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

    /**
     * @return the alpha
     */
    @Override
    public float getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     */
    @Override
    public boolean setAlpha(float alpha) {
        if(alpha<=getUpperThresholdAlpha()){
            this.alpha = alpha;
            return true;
        }return false;
    }
}
