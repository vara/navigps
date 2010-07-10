package app.navigps.gui.buttons;

import java.awt.Graphics;
import javax.swing.Action;

/**
 * Created on 2008-12-15, 21:39:58
 * @author vara
 */
public class OpacityButton extends AbstractOpacityJButton{

    /**
     *
     * @param label
     */
    public OpacityButton(String label) {
        super(label);
        setOpaque(false);
    }
    /**
     *
     * @param a
     */
    public OpacityButton(Action a) {
        super(a);
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {        
        super.paintBorder(g);
    }    
}
