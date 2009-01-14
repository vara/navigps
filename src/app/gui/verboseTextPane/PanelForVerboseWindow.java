/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.verboseTextPane;

import app.utils.BridgeForVerboseMode;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * Created on 2008-12-18, 14:00:08
 * @author vara
 */
public class PanelForVerboseWindow extends JPanel{

    private JTextPaneForVerboseInfo verbosePane;

    public PanelForVerboseWindow(BridgeForVerboseMode verboseStream){
        setLayout(new BorderLayout());
        verbosePane = new JTextPaneForVerboseInfo();
        verbosePane.setRowHeaderView(new LineNumber(verbosePane.getTextEditor()));
        verboseStream.addComponentsWithOutputStream(verbosePane.getInforamtionPipe());

        add(new PanelMenu(),BorderLayout.NORTH);
        add(verbosePane,BorderLayout.CENTER);
    }

    protected class PanelMenu extends JPanel{
        private JMenuBar bar = new JMenuBar();
        public PanelMenu(){
            setLayout(new GridLayout());
            add(bar);
            JMenu menu = new JMenu("Actions");
            for (Action action : ((MyTextPane)verbosePane.getTextPane()).getMyActions()) {
                menu.add(new JMenuItem(action));
            } 
            
            bar.add(menu);
        }
    }

    
}
