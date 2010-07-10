/*
 * NewToolbarToggleButton.java
 *
 * Created on 2009-04-15, 19:49:55
 */

package app.navigps.gui.buttons;

import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class NewToolbarToggleButton  extends OpacityToggleButton{
    public NewToolbarToggleButton(){
        super("");
    }

    public NewToolbarToggleButton(Action a,ImageIcon i){
        super(a);
        setIcon(i);
        setText("");
        setRound(10,10);
        setDefaultEmptyBorder();
    }

    public NewToolbarToggleButton(String label){
        super(label);
        setRound(10,10);
        setDefaultEmptyBorder();
    }
}
