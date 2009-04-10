/*
 * SimpleBusyPanel.java
 *
 * Created on 2009-04-06, 02:07:23
 */

package app.navigps.gui.detailspanel;

import app.navigps.gui.detailspanel.UI.BusyPanelUI;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class SimpleBusyPanel extends RoundJPanel implements MouseListener{    

    private Animator animator;

    private volatile String infoText = "Loading ...";

    private boolean enableMouseCursorInteraction = true;

    public SimpleBusyPanel(){
        //setOpaque(false);
        setVisible(false);
        addMouseListener(this);
        setUpperThresholdAlpha(0.5f);     

        setBackground(new Color(10,10,10));

        //animator = new Animator(1000,1, Animator.RepeatBehavior.REVERSE,
                                   // new AnimatorBehaviour());
        setUI(new BusyPanelUI());
    }

    public void setUI(BusyPanelUI newUI) {
        super.setUI(newUI);
    }

    public void setBusy(boolean busy){
        
        if(busy){
            setAlpha(0.0f);
            animator = new Animator(500, 1,Animator.RepeatBehavior.REVERSE,
                    new PropertySetter(this, "alpha", 0f, getUpperThresholdAlpha()));

            animator.setAcceleration(0.1f);
            animator.addTarget(new TimingTargetAdapter() {
                @Override
                public void begin() {
                    setVisible(true);
                    if(isEnableMouseCursorInteraction()){
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    }
                }

            });

        }else{            
            animator = new Animator(500, 1,Animator.RepeatBehavior.LOOP,
                    new PropertySetter(this, "alpha", getAlpha(), 0.0f));
            animator.addTarget(new TimingTargetAdapter() {
                @Override
                public void end() {
                    setCursor(null);
                    setVisible(false);
                }

            });
            animator.setAcceleration(0.1f);
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

    /**
     * @return the enableMouseCursorInteraction
     */
    public boolean isEnableMouseCursorInteraction() {
        return enableMouseCursorInteraction;
    }

    /**
     * @param enableMouseCursorInteraction the enableMouseCursorInteraction to set
     */
    public void setEnableMouseCursorInteraction(boolean enableMouseCursorInteraction) {
        this.enableMouseCursorInteraction = enableMouseCursorInteraction;
    }
}
