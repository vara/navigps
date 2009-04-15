/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OpacityToggleButton.java
 *
 * Created on 2009-04-15, 19:54:18
 */

package app.navigps.gui.buttons;

import java.awt.Graphics;
import javax.swing.Action;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class OpacityToggleButton extends RoundToggleButton {

    public OpacityToggleButton(String label) {
        super(label);
        setOpaque(false);
    }

    public OpacityToggleButton(Action a) {
        super(a);
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
    }
}
