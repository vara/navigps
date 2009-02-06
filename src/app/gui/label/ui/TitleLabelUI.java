/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.label.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.View;
import sun.swing.SwingUtilities2;

/**
 *
 * @version 1.0 06/02/09
 * @author wara
 * @comments  dispplay html text not work correct !!!
 */
public class TitleLabelUI extends BasicLabelUI{

    public static final int CENTER_HORIZONTAL = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    public static final int TOP = 256;
    public static final int CENTER_VERTICAL = 512;
    public static final int BOTTOM = 1024;

    private int textPosition = 0;

    private boolean shadow = true;
    private Color colorShadow = Color.BLACK;

    private static Rectangle paintIconR = new Rectangle();
    private static Rectangle paintTextR = new Rectangle();
    private static Rectangle paintViewR = new Rectangle();
    private static Insets paintViewInsets = new Insets(1, 1, 1, 1);

    protected static BasicLabelUI titleLabelUI = new BasicLabelUI();

    public TitleLabelUI(int position){
        setTextLayout(position);
    }
    
    /* Default Layout is CENTER_VERTICAL and CENTER_HORIZONTAL
     * 
     */
    public TitleLabelUI(){
        setTextLayout(CENTER_VERTICAL|CENTER_HORIZONTAL);
    }

    @Override
    protected void installDefaults(JLabel c) {
        super.installDefaults(c);
    }
    @Override
    public void propertyChange(PropertyChangeEvent e){
        super.propertyChange(e);
    }
    public static ComponentUI createUI(JComponent c) {
        return titleLabelUI;
    }

    @Override
    protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
        super.paintEnabledText(l, g, s,textX,textY);
    }

    @Override
    protected String layoutCL(JLabel label, FontMetrics fontMetrics, String text, Icon icon, Rectangle viewR, Rectangle iconR, Rectangle textR) {

        iconR.x = viewR.x;
        iconR.y = viewR.y;

        int gap;

        if(icon!=null){
            iconR.setSize(icon.getIconWidth(),viewR.height);
            gap = label.getIconTextGap();
        }
        else{
            iconR.setSize(0,0);
            gap = 0;
        }

        textR.x = iconR.x+iconR.width+gap;
        textR.y = iconR.y;
        textR.width = (viewR.width-iconR.width-gap);
        textR.height = viewR.height;

        //showRecs(label);
        
        return text;

    }

    @Override
    public void paint(Graphics g, JComponent c) {        
        Graphics2D g2 = (Graphics2D)g;
        JLabel label = (JLabel)c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

        if ((icon == null) && (text == null)) {
            return;
        }
        
        FontMetrics fm = SwingUtilities2.getFontMetrics(label, g);
        layout(label, fm, c.getWidth(), c.getHeight());
        
        if (icon != null) {
            icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
        }

        if (text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, paintTextR);

            }else if(!text.equals("")){

                Point pos = findPositionForText(label.getFont(),getTextLayout(),
                        text,g2.getFontRenderContext());

                if(isShadow()){
                    drawShadow(label, g2, text, pos.x+2,pos.y+2);
                }

                if (label.isEnabled()) {
                    paintEnabledText(label,g2,text, pos.x,pos.y);
                }
                else {
                    paintDisabledText(label,g2,text, pos.x,pos.y);
                }
             }
        }
    }

    public void drawShadow(JLabel l, Graphics g, String s, int textX, int textY){
        Color tmpCol = g.getColor();
        g.setColor(getColorShadow());
        SwingUtilities2.drawStringUnderlineCharAt(l, g, s, -1,
                                                         textX,textY);
        g.setColor(tmpCol);
    }

    public void showRecs(JLabel l){
        System.out.println("paintViewR "+paintViewR);
        System.out.println("paintIconR "+paintIconR);
        System.out.println("paintTextR "+paintTextR);
        System.out.println("label "+l.getBounds());
    }

    protected String layout(JLabel label, FontMetrics fm,
                          int width, int height) {
        Insets insets = label.getInsets(paintViewInsets);
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() :
                                          label.getDisabledIcon();
        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = width - (insets.left + insets.right);
        paintViewR.height = height - (insets.top + insets.bottom);

        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
        return layoutCL(label, fm, text, icon, paintViewR, paintIconR,
                        paintTextR);
    }

    /**
     * @return the shadow
     */
    public boolean isShadow() {
        return shadow;
    }

    /**
     * @param shadow the shadow to set
     */
    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public Point findPositionForText(Font f,int mode,String text,FontRenderContext frc){

        TextLayout textTl = new TextLayout(text,f, frc);
        //textTl.getPixelBounds(frc,0,0) -> this func. is too slow for large string
        Rectangle rec = textTl.getPixelBounds(frc,0,0);
        //string size in pixels
        int strWidthInPix = (int)rec.getWidth();
        int strHeightInPix = (int)rec.getHeight();
        
        int charH = (int)(textTl.getAscent()+textTl.getLeading());
        Point p = new Point(paintTextR.x,paintTextR.y+charH);

        switch(mode&0xFF){
            case TitleLabelUI.CENTER_HORIZONTAL:
                if(strWidthInPix<paintTextR.width){
                    p.x += (paintTextR.width-strWidthInPix)>>1;
                }
                break;

            case TitleLabelUI.LEFT:
            break;

            case TitleLabelUI.RIGHT:
                if(strWidthInPix<paintTextR.width){
                    p.x += (paintTextR.width-strWidthInPix);
                }
                break;

            default:
        }
        switch(mode&0xFF00){
            case TitleLabelUI.CENTER_VERTICAL:
                p.y += (paintTextR.height-strHeightInPix)>>1;
                break;

            case TitleLabelUI.TOP:
                break;

            case TitleLabelUI.BOTTOM:
                p.y += (paintTextR.height-(int)(textTl.getDescent()+textTl.getAscent()));
                break;

            default:
        }
        return p;
    }

    /**
     * @return the textPosition
     */
    public int getTextLayout() {
        return textPosition;
    }

    /**
     * @param textPosition the textPosition to set
     */
    public void setTextLayout(int textPosition) {
        this.textPosition = textPosition;
    }

    /**
     * @return the colorShadow
     */
    public Color getColorShadow() {
        return colorShadow;
    }

    /**
     * @param colorShadow the colorShadow to set
     */
    public void setColorShadow(Color colorShadow) {
        this.colorShadow = colorShadow;
    }
}
