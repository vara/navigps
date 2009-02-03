/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author wara
 */
public class DecoratePanel extends JPanel{
    public static final int CENTER = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private int layout = 0;

    private String title = "Unknown";

    private JButton closeButton = new JButton("X");

    public DecoratePanel(){
        setOpaque(false);
        setAlignmentY(20f);
        setPreferredSize(new Dimension(100,20));
        init();
    }

    public void addActionListenerToCloseButton(ActionListener al){
        closeButton.addActionListener(al);
    }

    private void init(){
        ((FlowLayout)getLayout()).setAlignment(FlowLayout.RIGHT);
        add(closeButton);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        FontRenderContext frc = g2.getFontRenderContext();
        Font font = g2.getFont();
        Point.Double  centerFont = findPositionForString(font,getDecoreteLayout(),getTitle(),frc);
        g2.drawString(getTitle(),(float)centerFont.getX(), (float)centerFont.getY());
        //g2.setColor(Color.RED);
        //g2.drawRect(0, 0, getWidth()-1, getHeight()-1);

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
}
