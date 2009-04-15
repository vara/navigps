/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RoundToggleButton.java
 *
 * Created on 2009-04-15, 19:51:30
 */

package app.navigps.gui.buttons;

import app.navigps.gui.borders.EmptyOvalBorder;
import app.navigps.gui.borders.OvalBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Action;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class RoundToggleButton extends AbstractOpacityToggleButton{
private RoundRectangle2D visibleRec;
    private float roundX=0;
    private float roundY=0;

    /**
     *
     * @param label
     */
    public RoundToggleButton(String label){

        super(label);
        setFocusPainted(false);
        setBorder(null);

    }

    /**
     *
     * @param a
     */
    public RoundToggleButton(Action a){
        super(a);
        setFocusPainted(false);
        setBorder(null);
    }

    /**
     *
     * @param label
     * @param arcw
     * @param arch
     */
    public RoundToggleButton(String label,float arcw,float arch){
        this(label);
        roundX = arcw;
        roundY = arch;
        setDefaultBorder();
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g){
        if(visibleRec == null || getWidth()!=visibleRec.getWidth() || getHeight() != visibleRec.getHeight())
            visibleRec = new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),getRoundX(), getRoundY());
        Shape orgClip = g.getClip();
        Area newClip = new Area(orgClip);
        Area visbClip = new Area(visibleRec);
        newClip.intersect(visbClip);
        GeneralPath gpClip = new GeneralPath(newClip);
        g.setClip(gpClip);
        super.paint(g);
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g){
		super.paintComponent(g);
    }

    /**
     *
     * @param g
     */
    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
    }

    /**
     * @return the roundX
     */
    public float getRoundX() {
        return roundX;
    }

    /**
     * @param roundX the roundX to set
     */
    public void setRoundX(float roundX) {
        this.roundX = roundX;
    }

    /**
     * @return the roundY
     */
    public float getRoundY() {
        return roundY;
    }

    /**
     * @param roundY the roundY to set
     */
    public void setRoundY(float roundY) {
        this.roundY = roundY;
    }
    /**
     *
     * @param arcw
     * @param arch
     */
    public void setRound(float arcw,float arch){
        setRoundX(arcw);
        setRoundY(arch);
    }

    /**
     *
     */
    public void setDefaultBorder(){
        setBorder(new OvalBorder(3,3,3,3,getRoundX(),getRoundY(),new Color(80,80,100)));
    }
    public void setDefaultEmptyBorder(){
        setBorder(new EmptyOvalBorder(3,3,3,3,getRoundX(),getRoundY()));
    }
}
