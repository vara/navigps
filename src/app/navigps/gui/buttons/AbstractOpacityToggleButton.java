/*
 * AbstractOpacityToggleButton.java
 *
 * Created on 2009-04-15, 19:47:16
 */

package app.navigps.gui.buttons;

import app.navigps.gui.detailspanel.AlphaInterface;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public abstract class AbstractOpacityToggleButton extends JToggleButton implements AlphaInterface{

    private float upperThresholdAlpha = 1f;
    private float alpha = 1f;

    /**
     *
     */
    public AbstractOpacityToggleButton(){
        super(null,null);
    }

    /**
     *
     * @param icon
     */
    public AbstractOpacityToggleButton(Icon icon) {
        this(null, icon);
    }

    /**
     * Creates a opacity toggle button with text.
     *
     * @param text  the text of the button
     */
    public AbstractOpacityToggleButton(String text) {
        super(text);
    }

    /**
     * Creates a Opacity toggle button where properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     *
     */
    public AbstractOpacityToggleButton(Action a) {
        super(a);
    }

    /**
     * Creates a opacity button with initial text and an icon.
     *
     * @param text  the text of the button
     * @param icon  the Icon image to display on the button
     */
    public AbstractOpacityToggleButton(String text, Icon icon) {
       super(text, icon);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));
        super.paintComponent(g2);
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));
        super.paintChildren(g2);
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
