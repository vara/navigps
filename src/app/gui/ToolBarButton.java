/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.gui.svgComponents.UpdateComponentsWhenChangedDoc;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author vara
 */
public class ToolBarButton extends JButton implements MouseListener{
        
    private boolean mouseOnButton = false;
    private boolean selectedButton = false;
    private boolean clickableButton = false;
    
    private UpdateComponentsWhenChangedDoc letOfChange = null;
    
    public ToolBarButton(Action a,ImageIcon i,UpdateComponentsWhenChangedDoc l){
	
	super(a);
	setLetOfChanged(l);
	setIcon(i);
	setText("");
	setFocusPainted(false);
	setBorderPainted(false);
	if(a.isEnabled())
	    addMouseListener(this);	
    }
    public ToolBarButton(Action a,ImageIcon i,UpdateComponentsWhenChangedDoc l,boolean clickable){
	this(a,i,l);
	setClickableButton(clickable);
    }
    
    public void setLetOfChanged(UpdateComponentsWhenChangedDoc l){
	letOfChange = l;
    }
    
    @Override
    public void paintComponent(Graphics g){
	
	if(isEnabled()){
	    final Graphics2D g2 = (Graphics2D) g;	
	    int w = getWidth();
	    int h = getHeight();
	    //System.out.println(""+w+","+h);
	    if(isMouseOnButton() || isSelectedButton()){

		GradientPaint gradient1 = new GradientPaint(0.0f, (float) getHeight()/4,Color.white, 
							    0.0f, 0.0f, new Color(224,234,255));			    
		Rectangle rec1 = new Rectangle(0, 0, getWidth(), getHeight()/3);
		g2.setPaint(gradient1);
		g2.fill(rec1);
		// paint lower gradient
		GradientPaint gradient2 = new GradientPaint(0.0f, (float) getHeight()/3, Color.WHITE,
							    0.0f, (float) getHeight(), new Color(174,201,255));
		Rectangle rec2 = new Rectangle(0, getHeight()/3, getWidth(), getHeight());
		g2.setPaint(gradient2);
		g2.fill(rec2);

	    }else{
		g2.setColor(getBackground());
		g2.fillRect(0, 0, w, h);
	    }
	    //g2.fillRect(0, 0, w, h);

	    getIcon().paintIcon(this, g2,(w-getIcon().getIconWidth())/2, (h-getIcon().getIconHeight())/2);
	    
	}else			
	    super.paintComponent(g);
    }

    public void mouseClicked(MouseEvent e) {
	if(isClickableButton())
	    if(isSelectedButton()){
		setSelectedButton(!selectedButton);
		setBorderPainted(false);
	    }
	    else{
		setSelectedButton(!selectedButton);
		setBorderPainted(true);
		//setBorder(Utils.createSimpleBorder(1, 1,1,1,new Color(174,201,255)));
	    }
    }

    public void mousePressed(MouseEvent e) {
	
	    setBorderPainted(true);
	
    }

    public void mouseReleased(MouseEvent e) {
	if(!isClickableButton()){
	    setBorderPainted(false);
	}
    }

    public void mouseEntered(MouseEvent e) {
	setMouseOnButton(true);	
    }

    public void mouseExited(MouseEvent e) {
	setMouseOnButton(false);
    }

    protected boolean isMouseOnButton() {
	return mouseOnButton;
    }

    protected void setMouseOnButton(boolean mouseOnButton) {
	this.mouseOnButton = mouseOnButton;
    }

    public boolean isSelectedButton() {
	return selectedButton;
    }

    protected void setSelectedButton(boolean selectedButton) {
	this.selectedButton = selectedButton;
    }

    protected boolean isClickableButton() {
	return clickableButton;
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
	if(letOfChange!=null)
	    letOfChange.currentStatusChanged(getAction().getValue(Action.NAME)+" isEnabled "+b);
    }
    
}
