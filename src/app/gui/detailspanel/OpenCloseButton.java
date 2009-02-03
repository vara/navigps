/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import app.gui.buttons.RotatedButton;
import app.utils.OutputVerboseStream;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.Direction;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.TimingTarget;

/**
 * Created on 2008-12-15, 21:29:18
 * @author vara
 */

public class OpenCloseButton extends RotatedButton implements ActionListener,TimingTarget{

    private Animator animator;
    private int animationDuration = 2000;

    public OpenCloseButton(String text,boolean clockwise,Dimension size){
        this(text, clockwise, size, 0);
    }
    public OpenCloseButton(String text,boolean clockwise,Dimension size,double round){
        super(text,true,size,round);
        animator = new Animator(animationDuration, Animator.INFINITE,
                RepeatBehavior.REVERSE, this);
        animator.setStartFraction(0.2f);
        animator.setStartDirection(Direction.BACKWARD);
        addActionListener(this);
    }
    @Override
    public void timingEvent(float arg0) {
        setAlpha(arg0);
        repaint();
    }

    @Override
    public void begin() {}

    @Override
    public void end() {}

    @Override
    public void repeat() {}

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (!animator.isRunning()) {
            this.setText("Stop Animation");
            animator.start();
        } else {
            animator.stop();
            this.setText("Start Animation");
            // reset alpha to opaque
            setAlpha(1.0f);
        }
    }
}
