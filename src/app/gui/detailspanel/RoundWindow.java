package app.gui.detailspanel;

import app.gui.borders.OvalBorder;
import app.utils.OutputVerboseStream;
import app.utils.Utils;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.Direction;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.TimingTarget;

/**
 * Created on 2008-12-08, 21:25:25
 * @author vara
 */
public class RoundWindow extends AlphaJPanel
        implements MouseListener,MouseMotionListener,TimingTarget,AlphaInterface{


    private int sensitiveMouseReaction = 8;    

    private OvalBorder mainBorder = new OvalBorder(20,20,Utils.colorAlpha(100,100,100,.38f));

    private OutputVerboseStream verboseStream = null;

    private boolean resizeWidthPanel = false;
    private boolean resizeHeghtPanel = false;
    private boolean cursorChanged = false;

    private boolean needRevalidate = false;
    private boolean dynamicRevalidate = false;

    private Dimension defaultSize = new Dimension(330,400);

    private Color[] colorBorderGlow = {new Color(230,230,230,148),new Color(70,102,146,200),
                                       new Color(190,190,190, 250),new Color(255,255,255, 255)};

    private boolean decoratePanel = true;    

    private Animator animator;
    private int animationDuration = 4000;

    private DecoratePanel decorate = new DecoratePanel();
    private RoundWindowRootPane rootPane;

    //uncomment for test root pane
    //ContentPaneForRoundWindow content = new ContentPaneForRoundWindow();

    public RoundWindow(OutputVerboseStream l){
        
        verboseStream = l;
        setOpaque(false);
        setSize(defaultSize);
        addMouseListener(this);
        addMouseMotionListener(this);
        mainBorder.setInsets(new Insets(3,3,3,0));
        setBorder(mainBorder);        
        super.setLayout(new BorderLayout());

        animator = new Animator(animationDuration, 1,
                RepeatBehavior.LOOP, this);
        decorate.addActionListenerToCloseButton(new CloseAction());

        installRepaintManager();

        //contentPane.setOpaque(false);
        //contentPane.setBorder(new OvalBorder(3,5,3,5, mainBorder.getRecW(),
          //      mainBorder.getRecH(), mainBorder.getBorderColor()));

        add(decorate,BorderLayout.NORTH);
        //comment for test root pane
        setRootPane(createRootPane());
        //uncomment for test root pane
        //add(content,BorderLayout.CENTER);
        getContentPane().setBorder(new OvalBorder(3,5,3,5, mainBorder.getRecW(),
                mainBorder.getRecH(), mainBorder.getBorderColor()));
        super.setEnabled(false);
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
        //uncomment for test
        //return content;
        //comment for test root pane
        return rootPane.getContentPane();
    }

    public void setTitle(String str){
        decorate.setTitle(str);
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {        
        super.addImpl(comp, constraints, index);
    }

    @Override
    public void setEnabled(final boolean aFlag) {
        super.setEnabled(aFlag);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayPanel(aFlag);
            }
        });        
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
    }
    public void displayPanel(boolean val){
        
        float frac = .0f;
        if(animator.isRunning()){
            animator.cancel();
            frac = getAlpha();
        }
        if(val){
           animator.setStartFraction(frac);
           animator.setStartDirection(Direction.FORWARD);
        }else{
           animator.setStartFraction(Math.max(getContentPane().getAlpha(),getAlpha()));
           animator.setStartDirection(Direction.BACKWARD);
        }
        animator.start();
    }
    public void updateMyUI(){
        Container parent = getParent();
        if(parent == null)
            return;

        int x = (getParent().getWidth()-getWidth());
        int y = (getParent().getHeight()-getHeight())>>1;
        updateMyUI(x, y, getWidth(), getHeight());
        
        //getVerboseStream().outputVerboseStream(getClass().getSimpleName()+" UpdateMyUI\n Parent size ["+width+","+height+"]" +
        //        "\tLocation on parent component ["+getLocation().getX()+","+getLocation().y+"]");
    }
     public void updateMyUI(final int x,final int y,final int width,final int height){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setBounds(x,y, width, height);
                if(isDynamicRevalidate())
                    revalidate();
            }
        });
     }
    private void installRepaintManager() {
        MyRepaintManager manager = new MyRepaintManager();
        RepaintManager.setCurrentManager(manager);
    }

    public OutputVerboseStream getVerboseStream(){
	    return verboseStream;
	}

    @Override
    public void paintChildren(Graphics g){        
        super.paintChildren(g);
    }

    protected RoundRectangle2D.Double computeVisibleChildrenArea(){
        Rectangle bounds = getBounds();
        Insets ins = super.getInsets();
        int canX = ins.left;
        int canY = ins.top;
        int canWidth = bounds.width-ins.left-ins.right;
        int canHeight = bounds.height-ins.top-ins.bottom;
        double arcx = ((OvalBorder)getBorder()).getRecW();
        double arcy = ((OvalBorder)getBorder()).getRecH();
        return new RoundRectangle2D.Double(canX, canY, canWidth, canHeight, arcx,arcy);
    }

    @Override
    public void paintComponent(Graphics g){
        //super.paintComponent(g);
        Container parent = getParent();
        if(isVisible() && parent!=null && parent.isEnabled()){
            Graphics2D g2 = (Graphics2D)g.create();

            GradientPaint gp = new GradientPaint(0.0f, (float) (getHeight()>>1),Utils.colorAlpha(1,51,90,getAlpha()),
                                                (float)(getWidth()>>1), 80.0f,Utils.colorAlpha(43,105,152,getAlpha()));
            //GradientPaint gp = new GradientPaint(0.0f, (float) getHeight(),Utils.colorAlpha(0,0,0,getAlpha()),
            //                                    0.0f, 0.0f,Utils.colorAlpha(90,122,166,getAlpha()));
            
            Insets outerIns = super.getInsets();
            RoundRectangle2D outerBorderShape = OvalBorder.createOuterShape(outerIns.left,outerIns.top,
                    getWidth()-outerIns.left-outerIns.right,getHeight()-outerIns.top-outerIns.bottom,
                    mainBorder.getRecW(), mainBorder.getRecH(),outerIns);
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.setPaint(gp);
            g2.fill(outerBorderShape);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            AlphaComposite newComposite =
              AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,getAlpha());
            g2.setComposite(newComposite);

            BorderEfects.paintBorderGlow(g2,4,outerBorderShape,colorBorderGlow);
            g2.dispose();
        }        
    }

    @Override
    public Insets getInsets() {
        Insets in = super.getInsets();
        return new Insets(in.top+7, in.left+7+1, in.bottom+7, in.right+7);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
//        if(!toggleButton.isVisible())
  //          toggleButton.showToggleButton();
    //    else
      //      toggleButton.hideToggleButton();
    }

    @Override
    public void mousePressed(MouseEvent e) {
       
        if(e.getButton()==MouseEvent.BUTTON1 && e.getX()<sensitiveMouseReaction){
            getVerboseStream().outputVerboseStream("Resize X side "+getClass().getSimpleName());
            resizeWidthPanel = true;
        }else if(e.getButton()==MouseEvent.BUTTON1 && e.getY()<sensitiveMouseReaction){
            getVerboseStream().outputVerboseStream("Resize Y side "+getClass().getSimpleName());
            resizeHeghtPanel = true;
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {

        if(needRevalidate || !isDynamicRevalidate()){                        
            revalidate();
            needRevalidate=false;
        }
        resizeWidthPanel = false;
        resizeHeghtPanel = false;
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {
        //resizeWidthPanel = false;
        //resizeHeghtPanel = false;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if(resizeWidthPanel || resizeHeghtPanel){
            int width =0,height = 0;
            if(resizeWidthPanel){
                width = getWidth()-e.getX()+2;
                setSize(width, getHeight());
            }
            else{
                height = getHeight()-e.getY();
                setSize(getWidth(), height);
            }
            updateMyUI();
            needRevalidate=true;
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        
        if(e.getX()<sensitiveMouseReaction||e.getY()<sensitiveMouseReaction){
            setCursor(Cursor.getPredefinedCursor( e.getX()<sensitiveMouseReaction ?
                                      Cursor.W_RESIZE_CURSOR:Cursor.N_RESIZE_CURSOR));
            cursorChanged = true;           
        }else if(cursorChanged && !resizeWidthPanel&&!resizeHeghtPanel){
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * @return the decoratePanel
     */
    public boolean isDecoratePanel() {
        return decoratePanel;
    }

    /**
     * @param decoratePanel the decoratePanel to set
     */
    public void setDecoratePanel(boolean decoratePanel) {
        this.decoratePanel = decoratePanel;
    }

    /**
     * @return the dynamicRevalidate
     */
    public boolean isDynamicRevalidate() {
        return dynamicRevalidate;
    }

    /**
     * @param dynamicRevalidate the dynamicRevalidate to set
     */
    public void setDynamicRevalidate(boolean dynamicRevalidate) {
        this.dynamicRevalidate = dynamicRevalidate;
    }

    @Override
    public void timingEvent(float arg0) {
        if(arg0>0 && arg0<Math.max(getContentPane().getUpperThresholdAlpha(),
                                   getUpperThresholdAlpha())){
            Color outerColor = ((OvalBorder)getBorder()).getBorderColor();
            
            if(arg0<mainBorder.getUpperThresholdAlpha()){ 
                ((OvalBorder)getBorder()).setBorderColor(Utils.colorAlpha(outerColor, arg0));
            }
            setAlpha(arg0);
            getContentPane().setAlpha(arg0);
            decorate.setAlpha(arg0);
            repaint();
        }else
            animator.stop();
    }
    @Override
    public void begin() {
        if(isEnabled())
            setVisible(isEnabled());
    }
    @Override
    public void end() {
        if(!isEnabled())
            setVisible(isEnabled());
    }
    @Override
    public void repeat() {
    }

    public class MyRepaintManager extends RepaintManager {
        
        @Override
        public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {

            Rectangle dirtyRegion = getDirtyRegion(c);
            if (dirtyRegion.width == 0 && dirtyRegion.height == 0) {
                int lastDeltaX = c.getX();
                int lastDeltaY = c.getY();
                Container parent = c.getParent();
                while (parent instanceof JComponent) {
                    if (!parent.isVisible() || (parent.getPeer() == null)) {
                        return;
                    }
                    if (parent instanceof ContentPaneForRoundWindow &&
                            (((AlphaJPanel)parent).getAlpha() < 1f ||
                            !parent.isOpaque())) {                        
                        x += lastDeltaX;
                        y += lastDeltaY;
                        lastDeltaX = lastDeltaY = 0;
                        c = (JComponent)parent;             
                    }
                    lastDeltaX += parent.getX();
                    lastDeltaY += parent.getY();
                    parent = parent.getParent();
                }
            }           
            super.addDirtyRegion(c, x, y, w, h);
        }
    }//MyRepaintManager

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
}