/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.label.ui;

import java.awt.Color;
import java.awt.Dimension;
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
    private Color colorShadow = Color.DARK_GRAY;

    private static Rectangle paintIconR = new Rectangle();
    private static Rectangle paintTextR = new Rectangle();
    private static Rectangle paintViewR = new Rectangle();
    private static Insets paintViewInsets = new Insets(1, 1, 1, 1);

    private int verticalCalibrated = 0;
    private int horizontalCalibrated = 0;

    protected static TitleLabelUI titleLabelUI = new TitleLabelUI();

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
    protected String layoutCL(JLabel label, FontMetrics fontMetrics, String text, Icon icon,
            Rectangle tviewR,
            Rectangle ticonR,
            Rectangle ttextR) {

        ticonR.x = tviewR.x;
        ticonR.y = tviewR.y;

        int gap;

        if(icon!=null){
            ticonR.setSize(icon.getIconWidth(),tviewR.height);
            gap = label.getIconTextGap();
        }
        else{
            ticonR.setSize(0,0);
            gap = 0;
        }

        ttextR.x = ticonR.x+ticonR.width+gap+getHorizontalCalibrated();
        ttextR.y = ticonR.y+getVerticalCalibrated();
        ttextR.width = (tviewR.width-ticonR.width-gap-getHorizontalCalibrated());
        ttextR.height = (ticonR.height!=0 ? ticonR.height : fontMetrics.getHeight())-getVerticalCalibrated();

        //System.err.println("\t----Rect's -------");
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
                    drawShadow(label, g2, text, pos.x+1,pos.y+1);
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
        String retStr ="paintViewR "+paintViewR
        +"\npaintIconR "+paintIconR
        +"\npaintTextR "+paintTextR
        +"\nlabel "+l.getBounds()
        +"\n\nviewR "+viewR
        +"\niconR "+iconR
        +"\nTextR "+textR;
        System.out.println(retStr);
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
        //System.err.println("**layout");
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

    private static Rectangle iconR = new Rectangle();
    private static Rectangle textR = new Rectangle();
    private static Rectangle viewR = new Rectangle();
    private static Insets viewInsets = new Insets(0, 0, 0, 0);


    @Override
    public Dimension getPreferredSize(JComponent c)
    {
        JLabel label = (JLabel)c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() :
                                          label.getDisabledIcon();
        Insets insets = label.getInsets(viewInsets);
        Font font = label.getFont();

        int dx = insets.left + insets.right;
        int dy = insets.top + insets.bottom;

        if ((icon == null) &&
            ((text == null) ||
             ((text != null) && (font == null)))) {
            return new Dimension(dx, dy);
        }
        else if ((text == null) || ((icon != null) && (font == null))) {
            return new Dimension(icon.getIconWidth() + dx,
                                 icon.getIconHeight() + dy);
        }
        else {
            FontMetrics fm = label.getFontMetrics(font);

            iconR.x = iconR.y = iconR.width = iconR.height = 0;
            textR.x = textR.y = textR.width = textR.height = 0;
            viewR.x = dx;
            viewR.y = dy;
            viewR.width = viewR.height = Short.MAX_VALUE;
            //System.err.println("**getPreferredSize");
            layoutCL(label, fm, text, icon, viewR, iconR, textR);
            int x1 = Math.min(iconR.x, textR.x);
            int x2 = Math.max(iconR.x + iconR.width, textR.x + textR.width);
            int y1 = Math.min(iconR.y, textR.y);
            int y2 = Math.max(iconR.y + iconR.height, textR.y + textR.height);
            Dimension rv = new Dimension(x2 - x1, y2 - y1);

            rv.width += dx;
            rv.height += dy;
            //System.out.println("pref size "+rv);
            return rv;
        }
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        Dimension d = getPreferredSize(c);
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            d.width -= v.getPreferredSpan(View.X_AXIS) - v.getMinimumSpan(View.X_AXIS);
        }
        return d;
    }

    @Override
    public Dimension getMaximumSize(JComponent c) {
        Dimension d = getPreferredSize(c);
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            d.width += v.getMaximumSpan(View.X_AXIS) - v.getPreferredSpan(View.X_AXIS);
        }
        return d;
    }

    /**
     * @return the verticalCalibrated
     */
    public int getVerticalCalibrated() {
        return verticalCalibrated;
    }

    /**
     * @param verticalCalibrated the verticalCalibrated to set
     */
    public void setVerticalCalibrated(int verticalCalibrated) {
        this.verticalCalibrated = verticalCalibrated;
    }

    /**
     * @return the horizontalCalibrated
     */
    public int getHorizontalCalibrated() {
        return horizontalCalibrated;
    }

    /**
     * @param horizontalCalibrated the horizontalCalibrated to set
     */
    public void setHorizontalCalibrated(int horizontalCalibrated) {
        this.horizontalCalibrated = horizontalCalibrated;
    }
}
