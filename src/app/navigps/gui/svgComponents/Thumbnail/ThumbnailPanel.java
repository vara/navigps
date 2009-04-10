package app.navigps.gui.svgComponents.Thumbnail;

import app.navigps.gui.borders.OvalBorder;
import app.navigps.gui.detailspanel.RoundJPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
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

    private PositionerListener mouselistener = new PositionerListener();
    /**
     *
     * @param tn
     */
    public ThumbnailPanel(Thumbnail tn){
        super(10,10);
        setLayout(new BorderLayout());
        setOpaque(false);
        thumbnail = tn;
        OvalBorder ob = new OvalBorder(5, 5, 5, 5,10,10);
        ob.setBorderColor(new Color(0,0,0));
        ob.setAlpha(0.8f);
        setInnerCorners(1,1);
        setBorder(ob);
        animator = new Animator(animationDuration, 1,
                RepeatBehavior.LOOP,new AnimatorBehaviour());
        add(thumbnail,BorderLayout.CENTER);

        installMouseListeners();
    }

    private void installMouseListeners(){
        addMouseListener(mouselistener);
        addMouseMotionListener(mouselistener);
    }
    private void uninstallMouseListeners(){
        removeMouseListener(mouselistener);
        removeMouseMotionListener(mouselistener);
    }
    /**
     *
     * @param g
     */
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

    protected void paintPositioner(Graphics2D g2){
        
    }

    /**
     *
     * @param g
     */
    @Override
    protected void paintBorder(Graphics g) {
        if(getAlpha()>0)
            super.paintBorder(g);
    }

    /**
     *
     * @param g
     */
    @Override
    protected void paintChildren(Graphics g) {
        if(getAlpha()>0)
            super.paintChildren(g);
    }

    /**
     *
     * @param fraction
     * @param direct
     */
    protected void initAnimator(float fraction,Direction direct){
        System.err.println("Init animator <Thumbnail panel>");
        if(fraction == -1){
            fraction = getAlpha();
        }
        animator.setStartFraction(fraction);
        animator.setStartDirection(direct);
        animator.start();
    }
    /**
     *
     * @param disp
     */
    public void displayThumbnail(boolean disp){
        if(disp){
            if(!thumbnail.isDisplay())
                initAnimator(0,Direction.FORWARD);
        }else{
            if(getThumbnail().isDisplay())
                initAnimator(-1,Direction.BACKWARD);
        }
    }

    /**
     * @return the thumbnail
     */
    public Thumbnail getThumbnail() {
        return thumbnail;
    }
    /**
     *
     */
    protected class AnimatorBehaviour implements TimingTarget{

        /**
         *
         * @param arg0
         */
        @Override
        public void timingEvent(float arg0) {
            if(setAlpha(arg0)){
                //System.out.println("Thumbnail panel alpha "+getAlpha());

            }

            if(thumbnail.setAlpha(arg0)){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        repaint();
                    }
                });
                
            }else{
                animator.stop();
            }
        }
        /**
         *
         */
        @Override
        public void begin() {
            //System.out.println("Thumbnail alpha "+getAlpha()+" upper alpha "+getUpperThresholdAlpha());
            getThumbnail().setInteractionEnabled(!thumbnail.isDisplay());
        }
        /**
         *
         */
        @Override
        public void end() {
            //System.out.println("Thumbnail end -> alpha "+getAlpha()+" upper alpha "+getUpperThresholdAlpha());
            
        }
        /**
         *
         */
        @Override
        public void repeat() {}
    }

    class PositionerListener extends MouseInputAdapter{

        private Point startPoint = new Point(0,0);
        private boolean drag = false;

        @Override
        public void mousePressed(MouseEvent e) {
            startPoint = e.getPoint();
            drag = true;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(drag){
                Point currentPoint = e.getPoint();
                Point currLocation = getLocation();

                int dx = currentPoint.x - startPoint.x;
                int dy = currentPoint.y - startPoint.y;
                //System.out.println("DX: "+dx+" DY: "+dy);
                currLocation.translate(dx, dy);
                Rectangle newBopunds = 
                        new Rectangle(currLocation.x, currLocation.y, 
                                          getWidth(), getHeight());
                if(checkAccess(newBopunds)){
                    setLocation(currLocation);                    
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            drag =  false;
        }

        private boolean checkAccess(Rectangle rec){
            Container parent = getParent();
            if(parent != null){
                return parent.getBounds().contains(rec);
            }
            return false;
        }
    }
}
