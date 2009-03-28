package app.navigps.gui;

import app.navigps.gui.detailspanel.AlphaJPanel;
import app.navigps.gui.label.ui.TitleLabelUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.Direction;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.TimingTarget;

/**
 *
 * @author wara
 */
public class DefaultAlphaLabelPanel extends AlphaJPanel{

    private Animator animator;
    private int animationDuration = 2000;

    private Timer closeTimer;
    private int closeDelay = 4000;

    private AlphaJPanel content = new AlphaJPanel();
    private JLabel contentText = new JLabel();

    private boolean animatorEnabled = false;

    private BumpArea bumpArea = new BumpArea();

    /**
     *
     */
    public DefaultAlphaLabelPanel(){

        setOpaque(false);
        //setLayout(new GridLayout(0,1,0,0));
        setLayout(new BorderLayout());

        add(content,BorderLayout.CENTER);
        add(bumpArea,BorderLayout.WEST);

        bumpArea.setMaximumSize(new Dimension(4,20));
        bumpArea.setMinimumSize(new Dimension(4,20));

        content.setOpaque(false);
        content.setLayout(new GridLayout(0,1));

        content.add(contentText);
        contentText.setText("");

        TitleLabelUI tlui = new TitleLabelUI(TitleLabelUI.LEFT|TitleLabelUI.CENTER_VERTICAL);
        tlui.setHorizontalCalibrated(3);

        contentText.setUI(tlui);

        animator = new Animator(getAnimationDuration(), 1,
            RepeatBehavior.LOOP, new AnimatorBehaviour());

        closeTimer = new Timer(getCloseDelay(), new CloseActionTimer());
        closeTimer.setRepeats(false);
    }

    public AlphaJPanel getContent() {
        return content;
    }

    /**
     *
     * @return
     */
    public BumpArea getBumpArea(){
        return bumpArea;
    }

    /**
     *
     * @param txt
     */
    public void setText(final String txt){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                content.setAlpha(content.getUpperThresholdAlpha());
                boolean needrepaint = txt.equals(getContentText().getText());
                if(needrepaint) repaint();
                else getContentText().setText(txt);

                if(isEnabled() && isAnimatorEnabled()){
                    startTimer();
                }
            }
        });
    }
    /**
     *
     */
    protected void startTimer(){

        if(closeTimer.isRunning()){
            closeTimer.restart();
        }else{
            closeTimer.start();
        }
        if(animator.isRunning()){
            animator.cancel();
        }
    }
    /**
     *
     * @param fraction
     */
    protected void initAnimator(float fraction){
        if(fraction == -1){
            fraction = getContent().getAlpha();
        }
        animator.setStartFraction(fraction);
        animator.setStartDirection(Direction.BACKWARD);
        animator.start();
    }

    /**
     * @return the closeDelay
     */
    public int getCloseDelay() {
        return closeDelay;
    }

    /**
     * @param closeDelay the closeDelay to set
     */
    public void setCloseDelay(int closeDelay) {
        this.closeDelay = closeDelay;
        closeTimer.setDelay(getCloseDelay());
    }

    /**
     * @return the animationDuration
     */
    public int getAnimationDuration() {
        return animationDuration;
    }

    /**
     * @param animationDuration the animationDuration to set
     */
    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
        if(animator.isRunning()){
            animator.stop();
            animator.setDuration(getAnimationDuration());
            animator.start();
        }else
            animator.setDuration(getAnimationDuration());
    }

    /**
     * @return the animatorEnabled
     */
    public boolean isAnimatorEnabled() {
        return animatorEnabled;
    }

    /**
     * @param animatorEnabled the animatorEnabled to set
     */
    public void setAnimatorEnabled(boolean animatorEnabled) {
        this.animatorEnabled = animatorEnabled;
        if(!contentText.getText().equals(""))
            startTimer();
    }

    /**
     * @return the contentText
     */
    public JLabel getContentText() {
        return contentText;
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
            if(!getContent().setAlpha(arg0)){
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
        /**
         *
         */
        @Override
        public void begin() {}
        /**
         *
         */
        @Override
        public void end() {}
        /**
         *
         */
        @Override
        public void repeat() {}
    }

    /**
     *
     */
    protected class CloseActionTimer implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            content.setAlpha(content.getAlpha());
            initAnimator(-1);
        }
    }
}