/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.buttons;

import app.gui.borders.OvalBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Action;

/**
 *
 * @author vara
 */
public class RoundButton extends OpacityButton{

    private RoundRectangle2D visibleRec;
    private double roundX=0;
    private double roundY=0;

    public RoundButton(String label){
	
        super(label);
        setFocusPainted(false);
        setBorder(null);
        
    }

    public RoundButton(Action a){
        super(a);
        setFocusPainted(false);
        setBorder(null);
    }

    public RoundButton(String label,double arcw,double arch){
        this(label);
        roundX = arcw;
        roundY = arch;
        setDefaultBorder();
    }

    @Override
    public void paint(Graphics g){
        visibleRec = new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),getRoundX(), getRoundY());
        g.setClip(visibleRec);        
        super.paint(g);
    }

    @Override
    public void paintComponent(Graphics g){	
		super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {        
        super.paintBorder(g);
    }

    /**
     * @return the roundX
     */
    public double getRoundX() {
        return roundX;
    }

    /**
     * @param roundX the roundX to set
     */
    public void setRoundX(double roundX) {
        this.roundX = roundX;
    }

    /**
     * @return the roundY
     */
    public double getRoundY() {
        return roundY;
    }

    /**
     * @param roundY the roundY to set
     */
    public void setRoundY(double roundY) {
        this.roundY = roundY;
    }
    public void setRound(double arcw,double arch){
        setRoundX(arcw);
        setRoundY(arch);
    }

    public void setDefaultBorder(){
        setBorder(new OvalBorder(6,8,6,8,getRoundX(),getRoundY(),new Color(80,80,100)));
    }
}
