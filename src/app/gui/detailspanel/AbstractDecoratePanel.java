/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.detailspanel;

import javax.swing.JLabel;

/**
 *
 * @author wara
 */
public abstract class AbstractDecoratePanel extends AlphaJPanel{

    abstract public void setTitle(String text);
    abstract public String getTitle();
    abstract public JLabel getContent();
    abstract public void setContent(JLabel lab);
}
