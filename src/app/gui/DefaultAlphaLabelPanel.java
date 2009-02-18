package app.gui;

import app.gui.detailspanel.AlphaJPanel;
import app.gui.label.ui.TitleLabelUI;
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

    public DefaultAlphaLabelPanel(){

        setOpaque(false);
        setLayout(new GridLayout(0,1,0,0));
        add(content);

        content.setOpaque(false);
        content.setLayout(new GridLayout(0,1,0,2));

        content.add(contentText);
        contentText.setText("");

        TitleLabelUI tlui = new TitleLabelUI(TitleLabelUI.LEFT|TitleLabelUI.CENTER_VERTICAL);
        tlui.setShadow(false);
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

    public void setText(String txt){
        if(isEnabled() && isAnimatorEnabled()){
            startTimer();
        }
        content.setAlpha(content.getUpperThresholdAlpha());
        getContentText().setText(txt);
    }
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

    protected class AnimatorBehaviour implements TimingTarget{

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
        @Override
        public void begin() {}
        @Override
        public void end() {}
        @Override
        public void repeat() {}
    }

    protected class CloseActionTimer implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            content.setAlpha(content.getAlpha());
            initAnimator(-1);
        }
    }
}