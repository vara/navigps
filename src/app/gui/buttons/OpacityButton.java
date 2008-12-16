/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.buttons;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JButton;

/**
 * Created on 2008-12-15, 21:39:58
 * @author vara
 */
public class OpacityButton extends JButton{

    private float alpha = 1.0f;      // current opacity of button
    private BufferedImage buttonImage = null;

    public OpacityButton(String label) {
        super(label);
        setOpaque(false);        
    }

    @Override
    public void paint(Graphics g) {        
        // Create an image for the button graphics if necessary
        if (getButtonImage() == null || getButtonImage().getWidth() != getWidth() ||
                getButtonImage().getHeight() != getHeight()) {
            setButtonImage(getGraphicsConfiguration().createCompatibleImage(getWidth(), getHeight()));
        }
        Graphics2D gButton = (Graphics2D)getButtonImage().getGraphics();
        gButton.setClip(g.getClip());
        //gButton.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(gButton);

        Graphics2D g2d  = (Graphics2D)g;
        AlphaComposite newComposite =
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER,getAlpha());
        g2d.setComposite(newComposite);

        // Copy the button's image to the destination graphics, translucently
        g2d.drawImage(getButtonImage(), 0, 0, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {        
        super.paintBorder(g);
    }
    
    @Override
    public void setOpaque(boolean isOpaque) {
        super.setOpaque(false);
    }

    /**
     * @return the alpha
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    /**
     * @return the buttonImage
     */
    public // each cycle will take 2 seconds
    BufferedImage getButtonImage() {
        return buttonImage;
    }

    /**
     * @param buttonImage the buttonImage to set
     */
    public void setButtonImage(BufferedImage buttonImage) {
        this.buttonImage = buttonImage;
    }
}
