package app.navigps.gui;

import app.navigps.gui.detailspanel.AlphaJPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.Direction;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.TimingTarget;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class DefaultAlphaLabelPanel extends AlphaJPanel{

    public static final int DEFAULT_CLOSE_DELAY = 4000;
    public static final int DEFAULT_ANIM_DURATION = 2000;

    private final Animator animator;
    private int animationDuration = DEFAULT_ANIM_DURATION;

    private Timer closeTimer;
    private int closeDelay = DEFAULT_CLOSE_DELAY;

    private AlphaJPanel content = new AlphaJPanel();
    private JLabel contentText = new JLabel();

    private boolean animatorEnabled = false;

    private BumpArea bumpArea = new BumpArea();

    //for test
    private Map <String,ActionListener> actionText =
            Collections.synchronizedMap(new HashMap<String,ActionListener>());

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

        //there is bug check it
        //TitleLabelUI tlui = new TitleLabelUI(TitleLabelUI.LEFT|TitleLabelUI.CENTER_VERTICAL);
        //tlui.setHorizontalCalibrated(3);
        //contentText.setUI(tlui);

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

    public void addActionText(String str,ActionListener al){
        actionText.put(str,al);
    }

    public void removeActionText(String al){
        actionText.remove(al);
    }

    public void removeAllActionText(){
        actionText.clear();
    }

    /**
     *
     * @param txt
     */
    public synchronized void setText(final String txt){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                content.setAlpha(content.getUpperThresholdAlpha());
                if( txt.equals(getContentText().getText()) )
                    repaint();
                else getContentText().setText(txt);

                if(isEnabled() && isAnimatorEnabled()){
                    startTimers();
                }
            }
        });
    }

    public boolean closeTimerIsRunning(){
        return closeTimer.isRunning();
    }

    public boolean animatorIsRunning(){
        return animator.isRunning();
    }

    protected void stopTimers(){
        if(closeTimerIsRunning()){
            closeTimer.stop();
        }
        if(animatorIsRunning()){
            animator.stop();
        }
    }

    /**
     *
     */
    protected void startTimers(){

        if(closeTimerIsRunning()){
            closeTimer.restart();
        }else{
            closeTimer.start();
        }
        if(animatorIsRunning()){
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
        if(animatorIsRunning())
            animator.stop();
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
        closeTimer.setInitialDelay(getCloseDelay());
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
        synchronized(animator){
        if(animator.isRunning()){
            animator.stop();
            animator.setDuration(getAnimationDuration());
            animator.start();
        }else
            animator.setDuration(getAnimationDuration());
        }
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
        if(!contentText.getText().equals("") && animatorEnabled)
            startTimers();
        else{
            stopTimers();
        }
    }
    /**
     * Override this method to inform when animator
     * work complet
     */
    public void animationFinished(){
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
        public void end() {
            animationFinished();
            if(!actionText.isEmpty()){
                String txt = getContentText().getText();
                ActionListener al = actionText.get(txt);
                if(al!=null){
                    ActionEvent ae = new ActionEvent(DefaultAlphaLabelPanel.this,
                            0, txt);
                    al.actionPerformed(ae);
                }
            }
        }
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