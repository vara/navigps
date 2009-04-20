/*
 * NaviToolBarUI.java
 *
 * Created on 2009-04-16, 00:13:17
 */

package app.navigps.gui.ToolBar.UI;

import app.navigps.utils.MetalUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class NaviToolBarUI extends BasicToolBarUI{


    Image intermediateImage;
    static boolean AA = true;

    private boolean paintedText = true;

    private String text = "NaviGPS";

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        //setRolloverBorders(false);        
    }
    
    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        
        //g2d.setColor(Color.RED);
        //g2d.fillRect(0,0,c.getWidth(), c.getHeight());

    }

    @Override
    public void update(Graphics g, JComponent c) {
        if (g == null) {
            throw new NullPointerException("graphics must be non-null");
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
}