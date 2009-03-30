package app.navigps.gui.buttons;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Action;

/**
 * Created on 2008-12-15, 21:39:58
 * @author vara
 */
public class OpacityButton extends AbstractOpacityJButton{

    private BufferedImage buttonImage = null;

    /**
     *
     * @param label
     */
    public OpacityButton(String label) {
        super(label);
        setOpaque(false);        
    }
    /**
     *
     * @param a
     */
    public OpacityButton(Action a) {
        super(a);
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
        //gButton.setClip(g.getClip());
        gButton.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(gButton);

        Graphics2D g2  = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        AlphaComposite newComposite =
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER,getAlpha());
        g2.setComposite(newComposite);

        // Copy the button's image to the destination graphics, translucently
        g2.drawImage(getButtonImage(), 0, 0, null);
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
