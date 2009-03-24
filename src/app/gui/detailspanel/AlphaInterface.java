/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

/**
 *
 * @author wara
 */
public interface AlphaInterface {
    /**
     *
     * @param aAlpha
     * @return
     */
    boolean setAlpha(float aAlpha);
    /**
     *
     * @return
     */
    float getAlpha();
    /**
     *
     * @param uppAlpha
     */
    void setUpperThresholdAlpha(float uppAlpha);
    /**
     *
     * @return
     */
    float getUpperThresholdAlpha();
}
