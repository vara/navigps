/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author wara
 */
public class AlphaJPanel extends JPanel implements AlphaInterface{

    public static final String ALPHA_CHANGE = "AlphaJPanel.alpha.change";
    public static final String UPPER_ALPHA_CHANGE = "AlphaJPanel.upper.alpha.change";

    private float upperThresholdAlpha = 1f;
    private float alpha = 1f;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    protected void paintComponent(Graphics g) {        
        Graphics2D g2 = (Graphics2D)g;        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));
        super.paintChildren(g);
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
        float oldVal = this.upperThresholdAlpha;
        this.upperThresholdAlpha = upperThresholdAlpha;
        firePropertyChange(UPPER_ALPHA_CHANGE, oldVal,upperThresholdAlpha);
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
            float oldVal = this.alpha;
            this.alpha = alpha;
            firePropertyChange(ALPHA_CHANGE, oldVal,alpha);
            return true;
        }return false;
    }
}