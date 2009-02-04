/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import javax.swing.JPanel;

/**
 *
 * @author wara
 */
public abstract class AlphaJPanel extends JPanel implements AlphaInterface{

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
