/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.utils.OutputVerboseStream;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author vara
 */
public class ToolBarButton extends JButton implements MouseListener{
        
    private boolean mouseOnButton = false;
    private boolean mousePressedButton = false;
    private boolean clickableButton = false;
    
    private OutputVerboseStream letOfChange;

    private GradientPaint[] gradientOnButton = new GradientPaint[2];
    private GradientPaint[] gradientPressedButtion = new GradientPaint[2];
    private Rectangle2D [] recForGradient = new Rectangle2D[2];
    private Dimension oldSize = null;
    private RoundRectangle2D borderOnButton;

    private float roundCorner = 12.0f;

    public ToolBarButton(Action a,ImageIcon i,OutputVerboseStream l){
	
        super(a);
        setVerboseStream(l);
        setIcon(i);
        setText("");
        setFocusPainted(false);
        setBorderPainted(false);
        if(a.isEnabled())
            addMouseListener(this);
    }
    public ToolBarButton(Action a,ImageIcon i,OutputVerboseStream l,boolean clickable){
        this(a,i,l);
        setClickableButton(clickable);
    }
    
    public void setVerboseStream(OutputVerboseStream l){
	letOfChange = l;
    }

    private void updateMyUI(){
        oldSize = getSize();
        gradientOnButton[0] = new GradientPaint(0.0f, (float) getHeight()/4,new Color(250,250,255),
                                                0.0f, 0.0f, new Color(224,234,255));
        gradientOnButton[1] = new GradientPaint(0.0f, (float) getHeight()/3, new Color(250,250,255),
                                                0.0f, (float) getHeight(), new Color(174,201,255));

        gradientPressedButtion[0] = new GradientPaint(0.0f, (float) getHeight()/4,new Color(255,255,255),
                                                      0.0f, 0.0f, new Color(124,134,155));
        gradientPressedButtion[1] = new GradientPaint(0.0f, (float) getHeight()/3, new Color(255,255,255),
                                                      0.0f, (float) getHeight(), new Color(74,101,155));

        recForGradient[0] = new Rectangle2D.Double(0, 0, getWidth(), getHeight()/3);
        recForGradient[1] = new Rectangle2D.Double(0, getHeight()/3, getWidth(), getHeight());
        borderOnButton    = new RoundRectangle2D.Double(1,2, getWidth()-2, getHeight()-4, roundCorner,roundCorner);
    }

    @Override
    public void paintComponent(Graphics g){
        //super.paintComponent(g);

        if(isEnabled()){
            final Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());

            if(oldSize==null || oldSize.getHeight()!=getHeight() || oldSize.getWidth()!=getWidth())
                updateMyUI();
            
            if(isMouseOnButton() || isSelected()){
                Paint oldPaint = g2.getPaint();
                g2.setClip(borderOnButton);
                GradientPaint [] gradient = null;
                Color borderColor = null;
                
                if(mousePressedButton){
                    borderColor = new Color(25, 25, 25);
                    gradient = gradientPressedButtion;                    
                }else{                    
                    borderColor = new Color(155, 155, 155);
                    gradient = gradientOnButton;
                }

                for(int i=0;i<gradient.length && i<recForGradient.length;i++){
                    g2.setPaint(gradient[i]);
                    g2.fill(recForGradient[i]);
                }
                g2.setPaint(oldPaint);
                g2.setClip(0, 0,getWidth(),getHeight());
                g2.setColor(borderColor);
                g2.draw(borderOnButton);

            }
            getIcon().paintIcon(this, g2,(getWidth()-getIcon().getIconWidth())/2,
                                         (getHeight()-getIcon().getIconHeight())/2);
            g2.dispose();
        }else
            super.paintComponent(g);	    
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        mousePressedButton = true;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(isClickableButton()){
            setSelected(!isSelected());
        }
        mousePressedButton = false;
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        setMouseOnButton(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setMouseOnButton(false);
    }

    protected boolean isMouseOnButton() {
        return mouseOnButton;
    }

    protected void setMouseOnButton(boolean mouseOnButton) {
        this.mouseOnButton = mouseOnButton;
    }

    protected boolean isClickableButton() {
        return clickableButton;
    }

    protected OutputVerboseStream getVerboseStream() {
        return letOfChange;
    }
    
    protected void setClickableButton(boolean clickableButton) {
        this.clickableButton = clickableButton;
    }

    @Override
    public void setEnabled(boolean b){
        super.setEnabled(b);
        if(b)
            addMouseListener(this);
        else
            removeMouseListener(this);
        
        if(getVerboseStream()!=null)
            getVerboseStream().outputVerboseStream(getAction().getValue(Action.NAME)+" isEnabled "+b);
    }   
}
