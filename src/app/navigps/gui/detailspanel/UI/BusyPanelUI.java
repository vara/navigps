/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BussyPanelUI.java
 *
 * Created on 2009-04-10, 13:22:47
 */

package app.navigps.gui.detailspanel.UI;

import app.navigps.gui.detailspanel.SimpleBusyPanel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.PanelUI;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class BusyPanelUI extends PanelUI{

    private Color textColor = new Color(255,255,255);
    private Color shadowColor = new Color(190,190,190);

    private GlyphVector gText;

    private String sText = "";

    private Point textPosition = new Point(0, 0);

    private boolean drawShadow = true;

    private GlyphVector convertText(Graphics2D g2,JComponent comp,String text){

        FontRenderContext frc = g2.getFontRenderContext();
        g2.setFont(g2.getFont().deriveFont(AffineTransform.getScaleInstance(2.0, 2.0)));

        GlyphVector gv = g2.getFont().createGlyphVector(frc, text);
        gv.setGlyphTransform(0, AffineTransform.getScaleInstance(2,2));        

        return gv;
    }

    private void updateText(Graphics2D g2,JComponent comp){
        String text = ((SimpleBusyPanel)comp).getText();
        System.out.println("Text '"+text+"' internal text '"+sText+"'");
        if(!text.equals(sText)){            
            gText = convertText(g2, comp, text);
            sText = text;
        }
    }


    private void updateTextPosition(JComponent comp){

        System.out.println("Update text !!");
        if(gText != null){
            int width = comp.getWidth();
            int height = comp.getHeight();
            Rectangle textBounds = gText.getOutline().getBounds();
            int x = (width-(int)textBounds.getWidth())>>1;
            int y = ((height-(int)textBounds.getHeight())>>1);

            textPosition.setLocation(x, y);
        }
    }

    @Override
    public void installUI(JComponent c) {
    }

    @Override
    public void uninstallUI(JComponent c) {        
        textPosition=null;
        gText = null;
        sText = null;
    }     

    @Override
    public void update(Graphics g, JComponent c) {

        updateText((Graphics2D)g, c);
        updateTextPosition(c);
        paint(g, c);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        System.out.println("Paint "+getClass().getCanonicalName());
        Graphics2D g2 = (Graphics2D)g.create();
        
        g2.setColor(c.getBackground());

        RoundRectangle2D round = ((SimpleBusyPanel)c).computeVisibleChildrenArea();       
        

        g2.fillRoundRect((int)round.getX(),(int)round.getY(),
                (int)round.getWidth(),(int)round.getHeight(),
                (int)round.getArcWidth(),(int)round.getArcHeight());

        if(!sText.equals("")){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(textColor);
            g2.drawGlyphVector(gText, textPosition.x,textPosition.y);

            if(isDrawShadow()){
                g2.setColor(shadowColor);
                g2.drawGlyphVector(gText, textPosition.x+1, textPosition.y+1);
            }
        }

        g2.dispose();
    }

    private boolean checkComponent(JComponent comp){
        return (comp instanceof SimpleBusyPanel);
    }

    /**
     * @return the drawShadow
     */
    public boolean isDrawShadow() {
        return drawShadow;
    }

    /**
     * @param drawShadow the drawShadow to set
     */
    public void setDrawShadow(boolean drawShadow) {
        this.drawShadow = drawShadow;
    }   
}
