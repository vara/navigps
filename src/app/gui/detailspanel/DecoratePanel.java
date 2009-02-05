/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author wara
 */
public class DecoratePanel extends AlphaJPanel{
    public static final int CENTER = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private int layout = 0;

    private String title = "Unknown";

    private JButton closeButton = new JButton("X");

    public DecoratePanel(){
        setOpaque(false);
        setPreferredSize(new Dimension(10,30));
        init();
        setAlpha(0.6f);
    }

    public void addActionListenerToCloseButton(ActionListener al){
        closeButton.addActionListener(al);
    }

    private void init(){
        ((FlowLayout)getLayout()).setAlignment(FlowLayout.RIGHT);
        //add(closeButton);

    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Ariel", Font.PLAIN, 16);
        FontRenderContext frc = g2.getFontRenderContext();
        Point.Double  centerFont = findPositionForString(font,getDecoreteLayout(),getTitle(),frc);
        GlyphVector gv = font.createGlyphVector(frc, getTitle());        
        g2.setColor(new Color(60,60,60));
        g2.drawGlyphVector(gv,(int)centerFont.getX()+2, (int)centerFont.getY()+2);

        g2.setColor(Color.WHITE);
        g2.drawGlyphVector(gv, (int)centerFont.getX(), (int)centerFont.getY());
    }

    private Point.Double findPositionForString(Font f,int mode,String text,FontRenderContext frc){
        if(mode==DecoratePanel.CENTER) {
            TextLayout textTl = new TextLayout(text,f, frc);
            Rectangle rec = textTl.getPixelBounds(frc,0,0);

            FontMetrics fm = getFontMetrics(f);
            //rozmiary stringu w pixelach
            double strWidthInPix = (int)rec.getWidth();
            double strHeightInPix = (int)rec.getHeight();
            //size panel in pixels
            int width = getSize().width;
            int height = getSize().height;

            double centPointX = (width-strWidthInPix-2)/2;
            double centPointY = (height+strHeightInPix-2)/2;
            //System.out.println("rozmiar Panelu "+width+","+height+
            //				   "\nrozmiar stringu "+strWidthInPix+","+strHeightInPix+
            //				   "\nSrodek "+centPointX+","+centPointY);
            return new Point.Double(centPointX,centPointY);
        }
        return null;
    }

    /**
     * @return the layout
     */
    public int getDecoreteLayout() {
        return layout;
    }
    /**
     * @param layout the layout to set
     */
    public void setDecorateLayout(int layout) {
        this.layout = layout;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    class Title extends JLabel{

        public Title(String txt) {
            super(txt);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }

    }
}
