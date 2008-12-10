/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

/**
 *
 * @author vara
 */
public class RoundButton extends JButton{

    private RoundRectangle2D visibleRec;
    
    public RoundButton(String label){
	
        super(label);
        setFocusPainted(false);
        setBorder(new OvalBorder());
        setOpaque(false);
    }
    
    @Override
    public void paintComponent(Graphics g){	
		
        Graphics2D g2 = (Graphics2D)g.create();
        visibleRec = new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),20,20);
        g2.setClip(visibleRec);
        super.paintComponent(g2);
        g2.dispose();
    }
}
