/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.gui.svgComponents.LetComponentsOfChangedDoc;
import app.utils.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author vara
 */
public class StatusPanel extends JPanel {

    private JLabel currentStatus;
    private JLabel mousePosOnSvgComponent;
    private JLabel pointInSvgDoc;
    
    public StatusPanel(LetComponentsOfChangedDoc listeners){
	
	currentStatus = listeners.getLabelWithCurrentStatus();
	mousePosOnSvgComponent = listeners.getLabelWithPosOnSvgComponent();
	pointInSvgDoc = listeners.getLabelWithPointInSvgDoc();
	
	setLayout(new GridBagLayout());
	setBorder(Utils.createOutsiderBorder(3,0,0,0,new Color(174,201,255)));
	setBackground(Color.WHITE);
	initComponents();
    }
   
    private void initComponents(){
	
	currentStatus.setAlignmentX(JLabel.LEFT_ALIGNMENT);
	currentStatus.setBorder(Utils.createOutsiderBorder(1,1,1,1,new Color(174,201,255)));
	pointInSvgDoc.setBorder(Utils.createOutsiderBorder(1,1,1,1,new Color(174,201,255)));
	mousePosOnSvgComponent.setBorder(Utils.createOutsiderBorder(1,1,1,1,new Color(174,201,255)));
	
	GridBagConstraints guidelines = new GridBagConstraints();
	
	guidelines.fill = GridBagConstraints.BOTH;
	guidelines.insets = new Insets(0,0,0,500);
	guidelines.ipady = 2;
        guidelines.weightx = 60;     
        guidelines.weighty = 1;	   
	add(currentStatus,guidelines,0,0,1,10);
	
	guidelines.fill = GridBagConstraints.BOTH;
	guidelines.insets = new Insets(0,2,0,2);
	guidelines.ipady = 2;
        guidelines.weightx = 10;     
        guidelines.weighty = 1;	  
	add(mousePosOnSvgComponent,guidelines,11,1,1,4);
	
	guidelines.fill = GridBagConstraints.BOTH;
	guidelines.insets = new Insets(0,2,0,0);
	guidelines.ipady = 2;
        guidelines.weightx = 20;     
        guidelines.weighty = 1;	  
	add(pointInSvgDoc,guidelines,16,1,1,4);
    }
    
    public void add(Component k,GridBagConstraints guidelines,
                    int x, int y, int s,int w)
    {
        //okreslenie pozycji komponentu na arkuszu 
        guidelines.gridx = x;         //wiersz i kolumna lewego gornego naroznika komponentu
        guidelines.gridy = y; 
        guidelines.gridwidth = s;     //ilosc wierszy i kolumn zajmujaca przez komponent 
        guidelines.gridheight = w;
        add(k,guidelines);
    }
    @Override
    public void paintComponent(Graphics g){
	final Graphics2D g2 = (Graphics2D) g;
	    
	GradientPaint gradient1 = new GradientPaint(0.0f, (float) getHeight(),new Color(174,201,255), 
						    0.0f, 8.5f, Color.white);			    
	Rectangle rec1 = new Rectangle(0, 0, getWidth(), getHeight());
	g2.setPaint(gradient1);
	g2.fill(rec1);
	  
    }   
    
}
