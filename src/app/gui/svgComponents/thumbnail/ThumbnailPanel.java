package app.gui.svgComponents.thumbnail;

import app.gui.borders.OvalBorder;
import app.gui.detailspanel.RoundJPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.Direction;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.TimingTarget;

/**
 *
 * @author wara
 */
public class ThumbnailPanel extends RoundJPanel{

    private Animator animator;
    private int animationDuration = 10000;
    private Thumbnail thumbnail;

    public ThumbnailPanel(Thumbnail tn){
        super(10,10);
        setLayout(new BorderLayout());
        setOpaque(false);
        thumbnail = tn;
        OvalBorder ob = new OvalBorder(1, 4, 1, 4,10,10);
        ob.setAlpha(0.4f);
        setBorder(ob);
        animator = new Animator(animationDuration, 1,
                RepeatBehavior.LOOP,new AnimatorBehaviour());
        add(thumbnail,BorderLayout.CENTER);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(getAlpha()>0){
            Graphics2D g2 = (Graphics2D)g.create();
            GradientPaint gp = new GradientPaint(0.0f, (float) (getHeight()>>1),new Color(1,51,90,255),
                                            (float)(getWidth()>>1), 80.0f,new Color(43,105,152,255));

            g2.setPaint(gp);
            int recw = (int)getRoundBorder().getRecW();
            g2.fillRoundRect(0, 0, getWidth(), getHeight(),recw,recw);
            g2.dispose();
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        if(getAlpha()>0)
            super.paintBorder(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        if(getAlpha()>0)
            super.paintChildren(g);
    }

    protected void initAnimator(float fraction,Direction direct){
        System.err.println("Init animator <Thumbnail panel>");
        if(fraction == -1){
            fraction = getAlpha();
        }
        animator.setStartFraction(fraction);
        animator.setStartDirection(direct);
        animator.start();
    }
    public void displayThumbnail(boolean disp){
        if(disp){
            if(!thumbnail.isDisplay())
                initAnimator(0,Direction.FORWARD);
        }else{
            if(thumbnail.isDisplay())
                initAnimator(-1,Direction.BACKWARD);
        }
    }
    protected class AnimatorBehaviour implements TimingTarget{

        @Override
        public void timingEvent(float arg0) {
            if(setAlpha(arg0)){
                //System.out.println("Thumbnail panel alpha "+getAlpha());
            }

            if(!thumbnail.setAlpha(arg0)){
                animator.stop();
            }else{
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        repaint();
                    }
                });
            }
        }
        @Override
        public void begin() {
            //System.out.println("Thumbnail alpha "+getAlpha()+" upper alpha "+getUpperThresholdAlpha());
            thumbnail.setInteractionEnabled(!thumbnail.isDisplay());
        }
        @Override
        public void end() {
            //System.out.println("Thumbnail end -> alpha "+getAlpha()+" upper alpha "+getUpperThresholdAlpha());
            
        }
        @Override
        public void repeat() {}
    }
}
