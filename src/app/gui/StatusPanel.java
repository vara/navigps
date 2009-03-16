/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.gui.detailspanel.AlphaJPanel;
import app.utils.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.border.Border;

/**
 *
 * @author vara
 */
public class StatusPanel extends AlphaJPanel {

    /**
     *
     */
    public static final String LEFT_PANEL = "left.panel";
    /**
     *
     */
    public static final String CENTER_PANEL = "center.panel";
    /**
     *
     */
    public static final String RIGHT_PANEL = "right.panel";

    /**
     * @return the lowColor
     */
    public static Color getLowColor() {
        return lowColor;
    }

    /**
     * @param aLowColor the lowColor to set
     */
    public static void setLowColor(Color aLowColor) {
        lowColor = aLowColor;
    }

    /**
     * @return the highColor
     */
    public static Color getHighColor() {
        return highColor;
    }

    /**
     * @param aHighColor the highColor to set
     */
    public static void setHighColor(Color aHighColor) {
        highColor = aHighColor;
    }

    private AlphaJPanel [] panels = new AlphaJPanel[3];
    private static Color lowColor = new Color(174,201,255);
    private static Color highColor = new Color(255, 255, 255);

    /**
     *
     */
    public StatusPanel(){
        initComponents();
        setLayout(new GridLayout(1,panels.length,1,1));
        setBorder(Utils.createOutsiderBorder(3,1,0,1,lowColor));
        setBackground(Color.WHITE);
    }
   
    /**
     *
     * @return
     */
    public static Border getDefaultBorder(){
        return Utils.createOutsiderBorder(1,1,1,1,new Color(174,201,255));
    }

    private void initComponents(){

        for (int i = 0; i < panels.length; i++) {
            panels[i] = new AlphaJPanel();
            panels[i].setOpaque(false);
            GridLayout gl = new GridLayout();
            gl.setHgap(2);
            panels[i].setLayout(gl);            
            add(panels[i]);
        }        	
    }

    /**
     *
     * @return
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension dim =  super.getPreferredSize();
        if(dim.getHeight()<26){
            dim.setSize(dim.width, 26);
            setPreferredSize(dim);
        }
        return dim;
    }

    public AlphaJPanel getPanel(String position){
        if(position.equals(LEFT_PANEL)){
            return panels[0];
        }else if(position.equals(CENTER_PANEL)){
            return panels[1];
        }else if(position.equals(RIGHT_PANEL)){
            return panels[2];
        }
        return null;
    }

    /**
     *
     * @param comp
     * @param position
     */
    public void addToPanelFromPosition(Component comp,String position){
        AlphaJPanel ap = getPanel(position);
        if(ap!=null){                    
            ap.add(comp);            
        }
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g){
        final Graphics2D g2 = (Graphics2D) g;
        GradientPaint gradient1 = new GradientPaint(0.0f, (float) getHeight(),getLowColor(),
                                0.0f, 8.5f, getHighColor());
        Rectangle rec1 = new Rectangle(0, 0, getWidth(), getHeight());
        g2.setPaint(gradient1);
        g2.fill(rec1);	  
    }    
}
