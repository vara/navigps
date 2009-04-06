/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SimpleBusyPanel.java
 *
 * Created on 2009-04-06, 02:07:23
 */

package app.navigps.gui.detailspanel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class SimpleBusyPanel extends RoundJPanel implements MouseListener{


    private Color backgroundColor = Color.BLACK;

    private Animator animator;

    private volatile String infoText = "Loading ...";

    public SimpleBusyPanel(){
        setOpaque(false);
        setVisible(false);
        addMouseListener(this);
        setUpperThresholdAlpha(0.5f);
     

        //animator = new Animator(1000,1, Animator.RepeatBehavior.REVERSE,
                                   // new AnimatorBehaviour());
    }

    public void setBusy(boolean busy){        
        if(busy){
            setAlpha(0.0f);
            animator = new Animator(500, 1,Animator.RepeatBehavior.REVERSE,
                    new PropertySetter(this, "alpha", 0f, getUpperThresholdAlpha()));
            animator.addTarget(new TimingTargetAdapter() {
                @Override
                public void begin() {
                    setVisible(true);
                }

            });

        }else{            
            animator = new Animator(500, 1,Animator.RepeatBehavior.LOOP,
                    new PropertySetter(this, "alpha", getAlpha(), 0.0f));
            animator.addTarget(new TimingTargetAdapter() {

                @Override
                public void end() {
                    setVisible(false);
                }

            });
        }
        animator.start();
    }

    @Override
    public boolean setAlpha(float alpha) {
        boolean val = super.setAlpha(alpha);
        repaint();
        return val;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        super.paintComponent(g2);
        Color tmpColor = g.getColor();
        g2.setColor(backgroundColor);
        RoundRectangle2D round = computeVisibleChildrenArea();
        g2.fillRoundRect((int)round.getX(),(int)round.getY(),
                (int)round.getWidth(),(int)round.getHeight(),
                (int)round.getArcWidth(),(int)round.getArcHeight());

        g2.setFont(g2.getFont().deriveFont(AffineTransform.getScaleInstance(2.0, 2.0)));

        FontRenderContext frc = g2.getFontRenderContext();
        String text = getText();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, text);
        gv.setGlyphTransform(0, AffineTransform.getScaleInstance(2,2));

        Rectangle2D textBounds = g2.getFont().getStringBounds(text, frc);
        //System.out.println(""+textBounds);
        int width = getWidth();
        int height = getHeight();

        int x = (width-(int)textBounds.getWidth())>>1;
        int y = ((height-(int)textBounds.getHeight())>>1);


        Composite composite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.drawGlyphVector(gv, x, y);
        g2.setColor(new Color(190,190,190));
        g2.drawGlyphVector(gv, x+1, y+1);

        g2.setColor(tmpColor);

        g2.setComposite(composite);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * @return the text
     */
    public String getText() {
        return infoText;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.infoText = text;
    }

    class AnimatorBehaviour implements TimingTarget{

        @Override
        public void timingEvent(float arg0) {
            if(!setAlpha(arg0)){
                animator.stop();
            }
        }

        @Override
        public void begin() {
        }

        @Override
        public void end() {
        }

        @Override
        public void repeat() {
        }

    }
}
