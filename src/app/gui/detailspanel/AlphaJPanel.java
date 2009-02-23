/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import java.awt.AlphaComposite;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author wara
 */
public class AlphaJPanel extends JPanel implements AlphaInterface{

    public static final String ALPHA_CHANGE = "AlphaJPanel.alpha.change";
    public static final String UPPER_ALPHA_CHANGE = "AlphaJPanel.upper.alpha.change";

    private volatile float upperThresholdAlpha = 1f;
    private volatile float alpha = 1f;

    public AlphaJPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public AlphaJPanel(LayoutManager layout) {
        this(layout, true);
    }

    public AlphaJPanel(boolean isDoubleBuffered) {
        this(new FlowLayout(), isDoubleBuffered);
    }

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public AlphaJPanel() {
        this(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    protected void paintComponent(Graphics g) {        
        Graphics2D g2 = (Graphics2D)g;        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));
        super.paintComponent(g2);
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
