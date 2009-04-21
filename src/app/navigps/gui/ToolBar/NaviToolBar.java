/*
 * NaviToolbar.java
 *
 * Created on 2009-04-15, 20:45:36
 */

package app.navigps.gui.ToolBar;

import app.navigps.gui.ToolBar.UI.NaviToolBarUI;
import app.navigps.gui.borders.OvalBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JToolBar;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.Interpolator;
import org.jdesktop.animation.timing.interpolation.KeyFrames;
import org.jdesktop.animation.timing.interpolation.KeyTimes;
import org.jdesktop.animation.timing.interpolation.KeyValues;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.interpolation.SplineInterpolator;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class NaviToolBar extends JToolBar{

    private  Animator animator;
    private int [] duration = {1000,500};

    public static final int INITIAL_TOOLBAR_TIME = 0;
    public static final int NORMAL_TOOLBAR_TIME = 0;

    private int typeTime=1;
    
    public NaviToolBar(String name){
        super(name);
        init();
    }
    public NaviToolBar( int orientation ){
        super(orientation);
        init();
    }

    public NaviToolBar( String name , int orientation) {
        super(name, orientation);
        init();
    }

    private void init() {
        setBorder(new OvalBorder(3,3,3,3,10,10,new Color(166,166,166)));
        addSeparator();
        setUI(new NaviToolBarUI());
    }

    @Override
    protected void paintComponent(Graphics g) {        
        super.paintComponent(g);
    }

    @Override
    public void setVisible(boolean aFlag) {
        if(isVisible() != aFlag){
            super.setVisible(aFlag);
        }
    }

    @Override
    public void setLocation(Point p) {
        super.setLocation(p);
    }

    @Override
    public void setBounds(Rectangle r) {
        animationBounds(r,duration[getTypeTime()]);
    }

    private void animationBounds(Rectangle newrec,int duration){
        Rectangle oldrec = getBounds();//current position
        if(newrec.equals(oldrec)){
            //System.out.println("the same rect, return");
            return;
        }
        setLocation(newrec.x, newrec.y);
        Dimension oldDim = new Dimension(oldrec.width, oldrec.height);
        Dimension newDim = new Dimension(newrec.width, newrec.height);

        if(animator != null && animator.isRunning()){
            animator.stop();
        }
        Interpolator splines = new SplineInterpolator(0.40f, 0.00f, 0.00f, 1.00f);
        KeyTimes times = new KeyTimes(.0f, 1.0f);
        KeyValues values = KeyValues.create(oldDim,newDim);
        KeyFrames frames = new KeyFrames(values,times, splines);

        animator = PropertySetter.createAnimator(duration,this,"size",frames);
        //animator.setResolution(10);
        animator.setDeceleration(.5f);
        animator.setAcceleration(0.3f);
        animator.addTarget(new TimingTargetAdapter(){

            @Override
            public void timingEvent(float fraction) {
                //System.out.println("timing event "+fraction);
                //repaint();
                validate();
            }

            @Override
            public void end() {
                //repaint();
            }

            @Override
            public void begin() {
            }

        });
        animator.start();
    }

    /**
     * @return the typeTime
     */
    public int getTypeTime() {
        return typeTime;
    }

    /**
     * @param typeTime the typeTime to set
     */
    public void setTypeTime(int typeTime) throws IllegalArgumentException{
        if(typeTime <0 || typeTime > duration.length-1){
            throw new IllegalArgumentException("argument must be >-1 and <"+(duration.length-1));
        }
        this.typeTime = typeTime;
    }
}
