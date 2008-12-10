/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.gui.searchServices.*;
import app.utils.OutputVerboseStream;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 * Created on 2008-12-08, 21:25:25
 * @author vara
 */
public class DetailsPanelForSearchServices extends JPanel implements MouseListener,MouseMotionListener{

    private String title = "UNKNOWN";

    private static int sensitiveMouseReaction = 8;
    private static int alpha = 210;
    private Color colorBorder = new Color(0,0,0,alpha);
    private DoubleOvalBorder mainBorder = new DoubleOvalBorder(20,20,colorBorder,45,45,colorBorder);

    private OutputVerboseStream verboseStream = null;

    private boolean resizeWidthPanel = false;
    private boolean resizeHeghtPanel = false;
    private boolean cursorChanged = false;

    private boolean needRevalidate = false;
    private boolean dynamicRevalidate = false;

    private RotatedButton toggleButton;

    private Dimension defaultSize = new Dimension(330,400);

    private Shape childClip = null;

    private static final Color clrGlowInnerHi = new Color(253, 239, 175, 148);
    private static final Color clrGlowInnerLo = new Color(255, 209, 0);
    private static final Color clrGlowOuterHi = new Color(253, 239, 175, 124);
    private static final Color clrGlowOuterLo = new Color(255, 179, 0);

    public DetailsPanelForSearchServices(OutputVerboseStream l){
        verboseStream = l;
        setOpaque(false);
        setSize(defaultSize);
        addMouseListener(this);
        addMouseMotionListener(this);
        mainBorder.setInsetsOuter(new Insets(2,2,2,2));
        mainBorder.setInsetsInner(new Insets(55,11,11,11));
        setBorder(mainBorder);

        //toggleButton = new RotatedButton("^", false,getSize(),l);
        //add(toggleButton);
        setLayout(new GridBagLayout());
        
    }
    public OutputVerboseStream getVerboseStream(){
	    return verboseStream;
	}
    public void updateMyUI(){
        int width = getParent().getWidth();
        int height = getParent().getHeight();
        this.setLocation(width-getWidth(), (height-getHeight())/2 );        
        if(dynamicRevalidate)
            revalidate();
        //getVerboseStream().outputVerboseStream(getClass().getSimpleName()+" UpdateMyUI\n Parent size ["+width+","+height+"]" +
          //      "\tLocation on parent component ["+getLocation().getX()+","+getLocation().y+"]");        
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g.create();

        GradientPaint gp = new GradientPaint(0.0f, (float) getHeight()/2,new Color(1,51,90,alpha),
                                            (float)getWidth()/2, 80.0f, new Color(43,105,152,alpha));

        GradientPaint gp2 = new GradientPaint(0.0f, (float) getHeight(), new Color(0,0,0,alpha),
                                            0.0f, 0.0f,new Color(90,122,166,alpha));

        Insets outerIns = mainBorder.getInsetsOuter();
        Insets innerIns = mainBorder.getInsetsInner();

        RoundRectangle2D outerBorder = DoubleOvalBorder.createOuterShape(0,0,getWidth(),getHeight(),
                mainBorder.roundOuterX,mainBorder.roundOuterY,outerIns);
        RoundRectangle2D innerBorder = DoubleOvalBorder.createInnerShape(outerBorder.getX(),outerBorder.getY(),
                outerBorder.getWidth()-1,outerBorder.getHeight()-1,mainBorder.roundInnerX,mainBorder.roundInnerY,
                innerIns);
        
        float tmpAlpha = (float)alpha/255;
        AlphaComposite newComposite =
              AlphaComposite.getInstance(AlphaComposite.SRC_OVER,tmpAlpha);

        Paint oldPaint = g2.getPaint();
        g2.setComposite(newComposite);

        g2.setPaint(gp2);
        g2.setClip(outerBorder);
        g2.fill(outerBorder);        
        
        g2.setPaint(gp);
        g2.fill(innerBorder);

        //g2.setClip(0,0,getWidth(),getHeight());
        g2.setPaint(oldPaint);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        paintBorderGlow(g2,5,outerBorder);
        paintBorderShadow(g2,2,innerBorder);
        

        float widthStroke = ((BasicStroke)g2.getStroke()).getLineWidth();
        childClip = new RoundRectangle2D.Double(innerBorder.getX()+widthStroke+1,innerBorder.getY()+widthStroke+1,
                innerBorder.getWidth()-widthStroke*2,innerBorder.getHeight()-widthStroke*2,
                innerBorder.getArcWidth(), innerBorder.getArcHeight());

        //System.out.println("childClip "+childClip.getBounds2D()+" innerBorder "+innerBorder.getBounds2D());
        
        g2.dispose();        
    }

    private void paintBorderShadow(Graphics2D g2, int shadowWidth,Shape clipShape) {        
        int sw = shadowWidth*2;
        for (int i=sw; i >= 2; i-=1) {
            float pct = (float)(sw - i) / (sw - 1);
            g2.setColor(getMixedColor(Color.LIGHT_GRAY, pct,
                                      Color.WHITE, 1.0f-pct));
            
            g2.setStroke(new BasicStroke(i));
            g2.draw(clipShape);
        }
    }    

    private void paintBorderGlow(Graphics2D g2, int glowWidth,Shape clipShape) {

        int gw = glowWidth*2;
        for (int i=gw; i >= 2; i-=2) {
            float pct = (float)(gw - i) / (gw - 1);
/*
            Color mixHi = getMixedColor(clrGlowInnerHi, pct,
                                       clrGlowOuterHi, 1.0f - pct);
            Color mixLo = getMixedColor(clrGlowInnerLo, pct,
                                      clrGlowOuterLo, 1.0f - pct);

            g2.setPaint(new GradientPaint(0.0f, clipShape.getBounds().height*0.25f,  mixHi,
                                          0.0f, clipShape.getBounds().height, mixLo));
 */
            g2.setColor(new Color(240,240,255,alpha));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, pct));
            g2.setStroke(new BasicStroke(i));
            g2.draw(clipShape);
        }
    }

    private static Color getMixedColor(Color c1, float pct1, Color c2, float pct2) {
        float[] clr1 = c1.getComponents(null);
        float[] clr2 = c2.getComponents(null);
        for (int i = 0; i < clr1.length-1; i++) {
            clr1[i] = (clr1[i] * pct1) + (clr2[i] * pct2);            
        }        
        return new Color(clr1[0], clr1[1], clr1[2], (float)alpha/255);
    }

    @Override
    public void paintChildren(Graphics g){
        if(childClip!=null)
            g.setClip(childClip);
        super.paintChildren(g);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        
        if(!toggleButton.isVisible())
            toggleButton.showToggleButton();
        else
            toggleButton.hideToggleButton();
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

        if(needRevalidate){
            getVerboseStream().outputVerboseStream("Revalidate!");            
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
            if(resizeWidthPanel)
                setSize(getWidth()-e.getX()+2, getHeight());
            else
                setSize(getWidth(), getHeight()-e.getY());            
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
}