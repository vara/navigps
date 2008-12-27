/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.verboseTextPane;

import app.gui.MyPopupMenu;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.plaf.ComponentUI;

/**
 * Created on 2008-12-18, 13:53:36
 * @author vara
 */
public class MyTextPane extends JTextPane{

    private LinkedList<Action> actions = new LinkedList<Action>();

    public MyTextPane(){
        setEditable(false);
        actions.add(new ClearAllAction("Clear All",
                        null,
                        "Clear All text from document",KeyEvent.VK_C));

        addMouseListener(new MouseForVerboseTextPane());
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        Component parent = getParent();
        ComponentUI compUi = getUI();

        return parent != null ?
            (compUi.getPreferredSize(this).width <= parent.getSize().width) : true;
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g2);
    }

    /**
     * @return the actions
     */
    public LinkedList<Action> getMyActions() {
        return actions;
    }

    public class ClearAllAction extends AbstractAction{

        public ClearAllAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic){
            super(text);
            putValue(AbstractAction.SMALL_ICON, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
            putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.ALT_DOWN_MASK));
            setEnabled(true);

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            setText("");
            //setEnabled(false);
        }
    }

    protected class MouseForVerboseTextPane extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e){
            if(e.getButton()==MouseEvent.BUTTON3){
                MyPopupMenu popup = new MyPopupMenu();
                for (Action action : actions) {
                    popup.add(new JMenuItem(action));
                }
                for (Action action : getActions()) {
                    popup.add(new JMenuItem(action));
                }
                popup.show(MyTextPane.this,e.getX(),e.getY());
            }
        }
    }
}
