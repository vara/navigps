/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.buttons;

import app.gui.detailspanel.AlphaInterface;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author wara
 */
public abstract class AbstractOpacityJButton extends JButton implements AlphaInterface{
    
    private float upperThresholdAlpha = 1f;
    private float alpha = 1f;

    /**
     *
     */
    public AbstractOpacityJButton(){
        super(null,null);
    }

    /**
     *
     * @param icon
     */
    public AbstractOpacityJButton(Icon icon) {
        this(null, icon);
    }

    /**
     * Creates a opacity button with text.
     *
     * @param text  the text of the button
     */
    public AbstractOpacityJButton(String text) {
        super(text);
    }

    /**
     * Creates a Opacity button where properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     *
     */
    public AbstractOpacityJButton(Action a) {
        super(a);
    }

    /**
     * Creates a opacity button with initial text and an icon.
     *
     * @param text  the text of the button
     * @param icon  the Icon image to display on the button
     */
    public AbstractOpacityJButton(String text, Icon icon) {
       super(text, icon);
    }

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
