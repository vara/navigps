/*
 * AnimationImageLayer.java
 *
 * Created on 2009-04-03, 02:35:17
 */

package app.navigps.gui.searchServices.swing;

import app.navigps.gui.detailspanel.AlphaJPanel;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */

public class AnimationImageLayer extends AlphaJPanel{
    
    private static final float MAGNIFY_FACTOR = 1.5f;

    private Rectangle bounds;
    private Image image;

    private Animator animator;

    private float zoom = 0.0f;

    public AnimationImageLayer(){
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {        
        if (image != null && bounds != null && animator.isRunning()) {                       
            int width = image.getWidth(this);
            width += (int) (image.getWidth(this) * MAGNIFY_FACTOR * getZoom());

            int height = image.getHeight(this);
            height += (int) (image.getHeight(this) * MAGNIFY_FACTOR * getZoom());

            int x = (bounds.width - width) / 2;
            int y = (bounds.height - height) / 2;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g2.setComposite(AlphaComposite.SrcOver.derive(1.0f - getZoom()));
            g2.drawImage(image, x + bounds.x, y + bounds.y,
                    width, height, null);            
        }
    }

    public void showSpring(Rectangle bounds, Image image) {
        
        this.bounds = bounds;

        if(this.image==null || !this.image.equals(image)){
            this.image = image;
        }        
        if(animator != null && animator.isRunning()){
            animator.stop();            
        }
        animator = PropertySetter.createAnimator(500, this,
                "zoom", 0.0f, 1.0f);
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.4f);
        animator.start();

        repaint();
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        repaint();
    }

    public void dispose(){
        image = null;
        animator = null;
        getParent().remove(this);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.err.println(getClass().getCanonicalName()+" Method Finalize !");
    }
}