package app.navigps.gui.detailspanel;

import app.navigps.gui.detailspanel.LoacationManager.LocationManager;
import app.navigps.gui.repaintmanager.AlphaRepaintManager;
import app.navigps.gui.borders.OvalBorder;
import app.navigps.gui.borders.RoundBorder;
import app.navigps.gui.detailspanel.LoacationManager.RightLocation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.Direction;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 * Created on 2008-12-08, 21:25:25
 * @author Gzregorz (vara) Warywoda
 */
public class RoundWindow extends RoundJPanel
                         implements FocusListener{

    public static final int CLOSE_WINDOW_ACTION = 0;
    public static final int OPEN_WINDOW_ACTION = 1;

    private static int currentAction = OPEN_WINDOW_ACTION;

    private boolean dynamicRevalidate = false;

    private Dimension defaultSize = new Dimension(330,400);

    private Color[] colorBorderGlow = {new Color(230,230,230,168),new Color(70,102,146,180),
                                       new Color(190,190,190, 250),new Color(255,255,255, 255)};

    private boolean decoratedWindow = true;

    private Animator animator;
    private int animationDuration = 1300;

    private AbstractDecoratePanel decorate = new DecoratePanel();
    private RoundWindowRootPane rootPane;

    private Insets innerGap = new Insets(7,7,7,7);
    private int decorateAndContentGap = 5;

    private LocationManager location;

    private WindowDisplayBehavior winBehavior = new WindowDisplayBehavior();

    /*
     * Default options are:
     *   Location: RightLocation
     *   Border: OvalBorder with instets (3,3,3,3)
     */

    public RoundWindow(){
        super(20,20);
        setSize(defaultSize);

        OvalBorder mainBorder = (OvalBorder)getRoundBorder();
        mainBorder.setInsets(new Insets(3,3,3,3));

        setFocusable(true);
        addFocusListener(this);
        super.setLayout(new BorderLayout(0,getDecorateAndContentGap()));
        
        animator = new Animator(getAnimationDuration(), 1,
                RepeatBehavior.REVERSE,getWinBehavior());

        //((DecoratePanel)decorate).addActionListenerToCloseButton(new CloseAction());       

        //contentPane.setBorder(new OvalBorder(3,5,3,5, mainBorder.getRecW(),
          //      mainBorder.getRecH(), mainBorder.getBorderColor()));

        add(decorate,BorderLayout.NORTH);
        setRootPane(createRootPane());

        getContentPane().setAlpha(0.0f);
        getDecoratePanel().setAlpha(0.0f);

        super.setEnabled(false);
        setLocationManager(new RightLocation(this));

        winBehavior.addStartAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getID() == OPEN_WINDOW_ACTION){
                    installRepaintManager();
                }
            }
        });
    }

    /**
     * Resize container
     */
    public void pack(){
        
        //getContentPane().revalidate();
        ContentPaneForRoundWindow container = (ContentPaneForRoundWindow)getContentPane();
        //System.out.println("coount "+container.getComponentCount());
        Dimension contentPaneSize = container.getSize();
        Insets contentInsets = container.getInsets();

        Insets rootPaneInsets = getRoundWindowRootPane().getInsets();
        Insets winInsets = getInsets();
        int width = contentPaneSize.width + contentInsets.left+ contentInsets.right+
                    rootPaneInsets.left + rootPaneInsets.right +
                    winInsets.left + winInsets.right;
        int height  = contentPaneSize.height + contentInsets.top+ contentInsets.bottom+
                      rootPaneInsets.top + rootPaneInsets.bottom +
                      winInsets.top + winInsets.bottom;

        //setSize(width, height);
        revalidate();

        //System.err.println("Content size: "+contentPaneSize+
        //        " Insets rootPane: "+rootPaneInsets+
        //        " Insets Window "+winInsets+" window size "+getSize());
        updatePosition();
    }

    /**
     *
     * @return
     */
    public boolean isEmpty(){
        return !(getRoundWindowRootPane().getContentPane().getComponentCount()>0);
    }
    
    /**
     *
     * @return Rectangle represent window shape
     */
    public RoundRectangle2D getWindowShape(){
        Point2D corners = getOuterCorners();
        return new RoundRectangle2D.Double(
                0,0, getWidth(), getHeight(),corners.getX(),corners.getY());
    }

    protected RoundWindowRootPane createRootPane(){
        rootPane = new RoundWindowRootPane();
        return rootPane;
    }

    public RoundWindowRootPane getRoundWindowRootPane() {
        return rootPane;
    }

    protected void setRootPane(RoundWindowRootPane rp){
        add(rp, BorderLayout.CENTER);
    }

    public AlphaJPanel getContentPane(){        
        return rootPane.getContentPane();
    }

    /**
     *
     */
    public void clearWindow(){
        setIcon(null);
        setTitle("");
        getDecoratePanel().getContent().setIcon(null);
        getContentPane().removeAll();
    }

    /**
     *
     * @param icon
     */
    public void setIcon(Icon icon){
        getDecoratePanel().getContent().setIcon(icon);
    }

    /**
     *
     * @param str
     */
    public void setTitle(String str){
        getDecoratePanel().setTitle(str);
    }

    /**
     *
     * @param aFlag
     */
    @Override
    public void setEnabled(final boolean aFlag) {
        super.setEnabled(aFlag);
        if(animator.isRunning()){
            animator.cancel();
        }

        if(aFlag){
            Float [] val = new Float[]{getAlpha(),getUpperThresholdAlpha()};
            animator = PropertySetter.createAnimator(1500,this,"alphaToAllRootWindow",val);
            
        }else{
            Float [] val = new Float[]{getAlpha(),0f};
            animator = PropertySetter.createAnimator(1500,this,"alphaToAllRootWindow",val);
        }
        animator.addTarget(getWinBehavior());
        animator.start();
        /*
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayPanel(aFlag);
            }
        });
         */
    }

    /**
     *
     * @param val
     */
    public void displayPanel(boolean val){
        
        float frac = .0f;
        if(animator.isRunning()){
            animator.cancel();
            frac = getAlpha();
        }
        if(val){
           animator.setStartFraction(frac);
           animator.setStartDirection(Direction.FORWARD);
           currentAction = OPEN_WINDOW_ACTION;
        }else{
           animator.setStartFraction(Math.max(getContentPane().getAlpha(),getAlpha()));
           animator.setStartDirection(Direction.BACKWARD);
           currentAction = CLOSE_WINDOW_ACTION;
        }
        animator.start();
    }
    /**
     *
     */
    public void updatePosition(){
        Container parent = getParent();
        if(parent == null)
            return;
        Rectangle rec = location.updateLocation(getParent());
        updatePosition(rec);
        //int x = (getParent().getWidth()-getWidth());
        //int y = (getParent().getHeight()-getHeight())>>1;
        //updateMyUI(x, y, getWidth(), getHeight());
        
        //getVerboseStream().outputVerboseStream(getClass().getSimpleName()+" UpdateMyUI\n Parent size ["+width+","+height+"]" +
        //        "\tLocation on parent component ["+getLocation().getX()+","+getLocation().y+"]");
    }

    protected void updatePosition(int x,int y) {
        updatePosition(x,y, getWidth(), getHeight());
    }

    protected void updatePosition(final Rectangle rec){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setBounds(rec);
                if(isDynamicRevalidate())
                    revalidate();
            }
        });
    }

    protected void updatePosition(int x,int y,int width,int height){
        updatePosition(new Rectangle(x, y, width, height));
    }

    private void installRepaintManager() {
        RepaintManager rpm = RepaintManager.currentManager(this);
        if(!(rpm instanceof AlphaRepaintManager)){
            System.err.println("Install "+AlphaRepaintManager.class.getName());
            AlphaRepaintManager manager = new AlphaRepaintManager();
            RepaintManager.setCurrentManager(manager);
        }
    }
    

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g){
        if(isVisible() && getAlpha()>0){

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g.create();
            GradientPaint gp = new GradientPaint(0.0f, (float) (getHeight()>>1),new Color(1,51,90,255),
                                                (float)(getWidth()>>1), 80.0f,new Color(43,105,152,255));
            //GradientPaint gp = new GradientPaint(0.0f, (float) getHeight(),Utils.colorAlpha(0,0,0,getAlpha()),
            //                                    0.0f, 0.0f,Utils.colorAlpha(90,122,166,getAlpha()));
            RoundBorder mainBorder = getRoundBorder();
            Insets outerIns = super.getInsets();
            int x = outerIns.left;
            int y = outerIns.top;
            int w = getWidth()-outerIns.left-outerIns.right;
            int h = getHeight()-outerIns.top-outerIns.bottom;

            RoundRectangle2D outerBorderShape = OvalBorder.createOuterShape(x,y,w,h,
                    mainBorder.getRecW(), mainBorder.getRecH(),outerIns);
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(gp);
            g2.fillRoundRect(x+1, y+1, w-1, h-1,(int)mainBorder.getRecW(),(int)mainBorder.getRecH());
            
            BorderEfects.paintBorderGlow(g2,4,outerBorderShape,colorBorderGlow);
            g2.dispose();
        }        
    }

    /**
     *
     * @return
     */
    @Override
    public Insets getInsets() {
        Insets in = super.getInsets();
        Insets inner = getInnerGap();
        return new Insets(in.top+inner.top, in.left+inner.left,
                in.bottom+inner.bottom, in.right+inner.right);
    }  

    /**
     * @return the decoratePanel
     */
    public boolean isDecoratedWindow() {
        return decoratedWindow;
    }

    /**
     * @param decorated
     */
    public void setDecorateWindow(boolean decorated) {
        if(decorated){
            if(!this.isAncestorOf(this.decorate)){
                add(this.decorate,BorderLayout.NORTH);
            }
        }else{
            this.remove(this.decorate);
        }
         this.decoratedWindow = decorated;
        revalidate();
    }

    /**
     * @return the dynamicRevalidate
     */
    public boolean isDynamicRevalidate() {
        return dynamicRevalidate;
    }

    /**
     *
     * @param alphaBorder
     * @return
     */
    public boolean setAlphaBorder(float alphaBorder){
        RoundBorder border = getRoundBorder();
        return border.setAlpha(alphaBorder);
    }

    /**
     *
     * @param alpha
     */
    public void setAlphaToAllRootWindow(final float alpha){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setAlphaBorder(alpha);
                setAlpha(alpha);
                getRoundWindowRootPane().setAlpha(alpha);
                getContentPane().setAlpha(alpha);
                getDecoratePanel().setAlpha(alpha);
                repaint(0,0,getWidth(),getHeight());
            }
        });
    }
    /**
     * @param dynamicRevalidate the dynamicRevalidate to set
     */
    public void setDynamicRevalidate(boolean dynamicRevalidate) {
        this.dynamicRevalidate = dynamicRevalidate;
    }   

    /**
     * @return the decorate
     */
    public AbstractDecoratePanel getDecoratePanel() {
        return decorate;
    }

    /**
     * @param decorate the decorate to set
     */
    public void setDecoratePanel(AbstractDecoratePanel decorate) {
        this.decorate = decorate;
    }

    /**
     * @return the innerGap
     */
    public Insets getInnerGap() {
        return innerGap;
    }

    /**
     * @param innerGap the innerGap to set
     */
    public void setInnerGap(Insets innerGap) {
        this.innerGap = innerGap;
    }    

    @Override
    public void focusGained(FocusEvent e) {
        System.out.println("Focus gained");
    }

    @Override
    public void focusLost(FocusEvent e) {
        System.out.println("Focus lost");
    }

    /**
     * @return the decorateAndContentGap
     */
    public int getDecorateAndContentGap() {
        return decorateAndContentGap;
    }

    /**
     * @param decorateAndContentGap the decorateAndContentGap to set
     */
    public void setDecorateAndContentGap(int decorateAndContentGap) {
        LayoutManager lm = getLayout();
        if(lm instanceof BorderLayout){
            ((BorderLayout)lm).setVgap(decorateAndContentGap);
        }
        this.decorateAndContentGap = decorateAndContentGap;
        lm.layoutContainer(this);
        if(isVisible())
            getRoundWindowRootPane().revalidate();
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;
        if(!(obj instanceof RoundWindow))
            return retVal;
        RoundWindow tmp = (RoundWindow)obj;
        retVal = getDecoratePanel().getTitle().equals(tmp.getDecoratePanel().getTitle());
        
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.rootPane != null ? this.rootPane.hashCode() : 0);
        hash = 89 * hash + (this.innerGap != null ? this.innerGap.hashCode() : 0);
        hash = 89 * hash + this.decorateAndContentGap;
        return hash;
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
        animator.setDuration(getAnimationDuration());
    }

    /**
     * @param location the location to set
     */
    public void setLocationManager(LocationManager location) {

        if(this.location != null){
            removeMouseListener(this.location);
            removeMouseMotionListener(this.location);
        }

        this.location = location;

        addMouseListener(location);
        addMouseMotionListener(location);
    }

    /**
     * @return the winBehavior
     */
    public WindowDisplayBehavior getWinBehavior() {
        return winBehavior;
    }

    /**
     * @param winBehavior the winBehavior to set
     */
    public void setWinBehavior(WindowDisplayBehavior winBehavior) {
        this.winBehavior = winBehavior;
    }

    public class WindowDisplayBehavior implements TimingTarget{

        private Vector <ActionListener> startAction = new Vector<ActionListener>();
        private Vector <ActionListener> endAction = new Vector<ActionListener>();

        public void addStartAction(ActionListener al){
            if(!startAction.contains(al)){
                startAction.add(al);
            }
        }

        public void removeStartAction(ActionListener al){
            startAction.remove(al);
        }

        public void addEndAction(ActionListener al){
            if(!endAction.contains(al)){
                endAction.add(al);
            }
        }

        public void removeEndAction(ActionListener al){
            endAction.remove(al);
        }

        @Override
        public void timingEvent(float arg0) {
            //System.out.println(""+arg0);
            /*
            if(arg0>0 && arg0<Math.max(getContentPane().getUpperThresholdAlpha(),
                                       getUpperThresholdAlpha())){
                Color outerColor = ((OvalBorder)getRoundBorder()).getBorderColor();

                if(arg0<getRoundBorder().getUpperThresholdAlpha()){
                    ((OvalBorder)getRoundBorder()).setBorderColor(Utils.colorAlpha(outerColor, arg0));
                }
                setAlphaToAllRootWindow(arg0);
                getContentPane().setAlpha(arg0);
                getDecoratePanel().setAlpha(arg0);
                repaint(0,0,getWidth(),getHeight());
            }else{}
                //animator.stop();
             */
        }
        @Override
        public void begin() {
            if(isEnabled())
                setVisible(isEnabled());

            System.out.println("******** RW 'start' alpha: "+getAlpha());
            final ActionEvent ae = new ActionEvent(this, currentAction, "start");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (ActionListener al : startAction) {
                        al.actionPerformed(ae);
                    }
                }
            }).start();
        }
        @Override
        public void end() {
            if(!isEnabled()){
                setVisible(isEnabled());                
            }
            System.out.println("******** RW 'end' alpha: "+getAlpha());
            final ActionEvent ae = new ActionEvent(this, currentAction, "end");            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (ActionListener al : endAction) {
                        al.actionPerformed(ae);
                    }
                }
            }).start();

        }
        @Override
        public void repeat() {
        }
    }
/*
    class CloseAction implements ActionListener,TimingTarget{
        private Animator animator;

        public CloseAction(){
            animator = new Animator(animationDuration, 1,
            RepeatBehavior.LOOP, this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
           animator.setStartFraction(getContentPane().getAlpha());
           animator.setStartDirection(Direction.BACKWARD);
           animator.start();
        }

        @Override
        public void timingEvent(float arg0) {
            if(getContentPane().setAlpha(arg0)){
            setSize(getWidth()-1, getHeight());
            updateMyUI();
            //repaint();
            }else{
                end();
            }
        }
        @Override
        public void begin() {}
        @Override
        public void end() {}
        @Override
        public void repeat() {}
    }
 */
}