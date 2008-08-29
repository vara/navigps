/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import java.awt.Graphics;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author vara
 */
public class ToolBarButton extends JButton{
    
    public ToolBarButton(Action a,ImageIcon i){
	
	super(a);	
	setIcon(i);
	setText("");
	setFocusPainted(false);
    }

    @Override
    public void paintComponent(Graphics g){
	super.paintComponent(g);
    }
}
