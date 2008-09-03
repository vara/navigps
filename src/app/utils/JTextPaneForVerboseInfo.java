/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *
 * @author vara
 */
public class JTextPaneForVerboseInfo extends JScrollPane{

    private JTextPane text = new JTextPane();
    public JTextPaneForVerboseInfo(){
	
	add(text);	
	text.setEditable(false);	
	setViewportView(text);
    }
    public JTextPane getTextEditor(){return text;}
}
