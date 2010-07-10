/*
 * NewToolbarButton.java
 *
 * Created on 2009-04-15, 18:48:22
 */

package app.navigps.gui.buttons;

import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class NewToolbarButton extends RoundButton{

    public NewToolbarButton(){
        super("");
    }

    public NewToolbarButton(Action a,ImageIcon i){
        super(a);
        setIcon(i);
        setText("");
        setRound(10,10);
        setDefaultEmptyBorder();
    }

    public NewToolbarButton(String label){
        super(label);
        setRound(10,10);
        setDefaultEmptyBorder();
    }
}
