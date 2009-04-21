/*
 * NaviToolBarUI.java
 *
 * Created on 2009-04-16, 00:13:17
 */

package app.navigps.gui.ToolBar.UI;

import app.navigps.gui.borders.OvalBorder;
import app.navigps.utils.GraphicsUtilities;
import app.navigps.utils.MetalUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.basic.BasicToolBarUI.DockingListener;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class NaviToolBarUI extends BasicToolBarUI{


    Image intermediateImage;
    static boolean AA = true;

    private boolean paintedText = true;

    private String text = "NaviGPS";

    private BufferedImage bi = null;

    private ConvolveOp blurOp;

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        //setRolloverBorders(false);

         float[] matrix = {
                                0.111f, 0.111f, 0.111f,
                                0.111f, 0.111f, 0.111f,
                                0.111f, 0.111f, 0.111f,
                           };

        blurOp = new ConvolveOp(new Kernel(3, 3, matrix));
    }
    
    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        
        //g2d.setColor(Color.RED);
        //g2d.fillRect(0,0,c.getWidth(), c.getHeight());

    }

    @Override
    protected void floatAt(Point position, Point origin) {
        super.floatAt(position, origin);
        System.out.println("floatAt pos: "+position+" origin: "+origin);
    }

    @Override
    protected void dragTo(Point position, Point origin) {
        super.dragTo(position, origin);
        System.out.println("dragTo pos: "+position+" origin: "+origin);
    }



    @Override
    public void update(Graphics g, JComponent c) {
        if (g == null) {
            throw new NullPointerException("graphics must be non-null");
        }

        Border bord = c.getBorder();
        if(bord instanceof OvalBorder){
            OvalBorder ovb= (OvalBorder)bord;
            RoundRectangle2D clip = new RoundRectangle2D.Double(0,0,
                    c.getWidth(),c.getHeight(), ovb.getRecW(), ovb.getRecH());
            Area newClip = new Area(g.getClip());
            Area visbClip = new Area(clip);
            newClip.intersect(visbClip);
            GeneralPath gpClip = new GeneralPath(newClip);
            g.setClip(gpClip);
        }

        if (c.isOpaque() && (c.getBackground() instanceof UIResource) &&
                            ((JToolBar)c).getOrientation() ==
                      JToolBar.HORIZONTAL && UIManager.get(
                     "MenuBar.gradient") != null) {
            JRootPane rp = SwingUtilities.getRootPane(c);
            
            JMenuBar mb = null;

            if(rp != null)
                mb = rp.getJMenuBar();

            if (mb != null && mb.isOpaque() &&
                              (mb.getBackground() instanceof UIResource)) {
                Point point = new Point(0, 0);
                point = SwingUtilities.convertPoint(c, point, rp);
                int x = point.x;
                int y = point.y;
                point.x = point.y = 0;
                point = SwingUtilities.convertPoint(mb, point, rp);
                if (point.x == x && y == point.y + mb.getHeight() &&
                     mb.getWidth() == c.getWidth() &&
                     MetalUtils.drawGradient(c, g, "MenuBar.gradient",
                     0, -mb.getHeight(), c.getWidth(), c.getHeight() +
                     mb.getHeight(), true)) {
                    paint(g, c);
                    return;
                }
            }
            if (MetalUtils.drawGradient(c, g, "MenuBar.gradient",
                           0, 0, c.getWidth(), c.getHeight(), true)) {
                paint(g, c);
                return;
            }
        }
        super.update(g, c);
    }

    protected void paintText(Graphics2D g2d,JComponent c){
        if(!text.equals("")){

            Dimension compSize = c.getSize();

            //check size for painting text
            int childsWidth = 0;
            Component [] childs = c.getComponents();
            for (Component child : childs) {
                childsWidth +=child.getWidth();
            }

            int textGap = compSize.width - childsWidth;
            System.out.println("Text gap "+textGap);

            if(textGap >50){
                
                //positioning text in center textGap area
                //int xPos = childsWidth+(gapForText - textWidth)>>1;
                //int yPos = (compSize.height-textHight)>>1;
                createIamge(g2d, new Rectangle(0,0, textGap, 50));
            }            
        }
    }

    private void createIamge(Graphics2D g2d,Rectangle paintArea){

        if (intermediateImage == null) {
            // First, measure the size of the text
            FontRenderContext frc = new FontRenderContext(null,AA, false);

            Font f = new Font("Times",Font.BOLD,22);

            TextLayout layout = new TextLayout(text, f, frc);
            
            float sw = (float) layout.getBounds().getWidth();
            AffineTransform transform = new AffineTransform();
            //transform.setToTranslation(paintArea.width/2-sw/2, paintArea.height/4);
            Shape shape = layout.getOutline(transform);
            Rectangle rect = shape.getBounds();
            System.out.println("Rect text "+rect);
            //Rectangle2D rect = layout.getBounds();
            int imageW = (int)(rect.getWidth() - rect.getX() + .5);
            int imageH = (int)(rect.getHeight() - rect.getY() + .5);
            // We must also account for text "descent" in determining where to draw string in image
            int descent = (int)(layout.getDescent() + .5f);
            // Now, create the intermediate image
            GraphicsConfiguration gc =
                    SwingUtilities.getWindowAncestor(toolBar).getGraphicsConfiguration();
            if (!AA) {
                // non-Anti-Aliased text; only need transparent-background image
                intermediateImage = gc.createCompatibleImage(imageW, imageH,
	                                                     Transparency.BITMASK);
            } else {
                // anti-aliased text needs translucent image
                intermediateImage = gc.createCompatibleImage(imageW, imageH,
	                                                     Transparency.TRANSLUCENT);
            }
            // And render the transparent background and the text into the image
            Graphics2D gImg = (Graphics2D)intermediateImage.getGraphics();
            gImg.setComposite(AlphaComposite.Src);
            gImg.setColor(new Color(0, 0, 0, 0));
            gImg.fillRect(0, 0, imageW, imageH);
            if (AA) {
                 // Set up Anti-Aliasing for text rendering
                 gImg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	                               RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }

            gImg.draw(shape);
            gImg.dispose();
        }

        g2d.drawImage(intermediateImage, 0, 0, null);
    }

    /**
     * @return the paintText
     */
    public boolean isPaintedText() {
        return paintedText;
    }

    /**
     * @param paintText the paintText to set
     */
    public void setPaintedText(boolean paintText) {
        this.paintedText = paintText;
    }

    @Override
    protected MouseInputListener createDockingListener(){
        return new NaviDockingListener(toolBar);
    }

    protected void setDragOffset(Point p) {
        if (!GraphicsEnvironment.isHeadless()) {
            if (dragWindow == null) {
                dragWindow = createDragWindow(toolBar);
            }
            dragWindow.setOffset(p);
        }
    }

    @Override
    public boolean canDock(Component c, Point p) {
        boolean val =super.canDock(c, p);
        System.out.println("can dock "+val);
        return val;

    }

    @Override
    public void setFloating(boolean b, Point p) {
        if (toolBar.isFloatable()) {       

            if (b){
                if (dragWindow != null){
                    Point destLoc = toolBar.getLocationOnScreen();
                    Animator animator =
                            PropertySetter.createAnimator(500, dragWindow, "location", destLoc);
                    animator.addTarget(new TimingTargetAdapter(){
                        @Override
                        public void end() {
                            dragWindow.setVisible(false);
                        }

                    });
                    animator.start();
                }
                
            }else {
                if (dragWindow != null){
                    dragWindow.setVisible(false);
                }
                System.out.println("point "+p);
                Container dockingSource = toolBar.getParent();
                if ( propertyListener != null )
                    UIManager.removePropertyChangeListener( propertyListener );
                Component target = dockingSource.getComponentAt(p);
                if(target != null){
                    Rectangle rec = target.getBounds();
                    p.translate(rec.width/2, 0);
                    if(!rec.contains(p)){
                        return;
                    }
                }
                int zLocation = dockingSource.getComponentZOrder(target);
                dockingSource.add(toolBar,zLocation);
                dockingSource.invalidate();
                Container dockingSourceParent = dockingSource.getParent();
                if (dockingSourceParent != null)
                    dockingSourceParent.validate();
                dockingSource.repaint();
            }
        }

    }

    @Override
    protected JFrame createFloatingFrame(JToolBar toolbar) {
        return (JFrame)null;
    }

    @Override
    protected RootPaneContainer createFloatingWindow(JToolBar toolbar) {
        return super.createFloatingWindow(toolbar);
    }


    @Override
    protected void paintDragWindow(Graphics g) {
        if(bi == null){
            bi = GraphicsUtilities.
                    createCompatibleTranslucentImage(toolBar.getWidth(),toolBar.getHeight());
            Graphics gbi = bi.createGraphics();
            toolBar.paint(gbi);
            gbi.dispose();
        }
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2.drawImage(blurOp.filter(bi, null), 0, 0, null);

        System.err.println("Paint dragWindow");
    }

    @Override
    protected DragWindow createDragWindow(JToolBar toolbar) {
        return super.createDragWindow(toolbar);
    }


    
    protected class NaviDockingListener extends DockingListener {
        private boolean pressedInBumps = false;

        public NaviDockingListener(JToolBar t) {
            super(t);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
                if (!toolBar.isEnabled()) {
                    return;
                }
            pressedInBumps = false;
            Rectangle bumpRect = new Rectangle();

            if (toolBar.getOrientation() == JToolBar.HORIZONTAL) {
                int x = MetalUtils.isLeftToRight(toolBar) ? 0 : toolBar.getSize().width-14;
                    bumpRect.setBounds(x, 0, 14, toolBar.getSize().height);

            } else {  // vertical
                bumpRect.setBounds(0, 0, toolBar.getSize().width, 14);
            }
            if (bumpRect.contains(e.getPoint())) {
                pressedInBumps = true;
                Point dragOffset = e.getPoint();
                if (!MetalUtils.isLeftToRight(toolBar)) {
                    dragOffset.x -=
                            (toolBar.getSize().width-toolBar.getPreferredSize().width);
                }
                setDragOffset(dragOffset);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (pressedInBumps) {
                super.mouseDragged(e);
            }
        }
    }


}