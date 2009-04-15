/*
 * NaviToolBarPanel.java
 *
 * Created on 2009-04-15, 14:54:40
 */

package app.navigps.gui.ToolBar;

import app.navigps.gui.ToolBar.Layout.NaviToolbarLayout;
import app.navigps.gui.detailspanel.AlphaJPanel;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class NaviToolBarPanel extends AlphaJPanel{

    public NaviToolBarPanel(){
       super(new NaviToolbarLayout(2,2));
    }
}
