/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.detailspanel.LoacationManager;

import app.navigps.gui.detailspanel.*;
import java.awt.Container;
import java.awt.Rectangle;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author wara
 */
public abstract class LocationManager extends MouseInputAdapter{

    protected RoundWindow root;

    protected int sensitiveMouseReaction = 8;

    protected boolean resizeWidthPanel = false;
    protected boolean resizeHeghtPanel = false;
    protected boolean cursorChanged = false;    

    public LocationManager(RoundWindow rw){
        root = rw;
    }    
    public abstract Rectangle updateLocation(Container src);
}
