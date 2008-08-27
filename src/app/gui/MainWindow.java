/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.Main;
import config.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author vara
 */
public class MainWindow extends JFrame{

    private Main core;
    
    public MainWindow(Main c)
    {
	core = c;
	setSize(Configuration.getScreenSize());	
	
	initComponents();
	
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);
	
    }
    
    private void initComponents()
    {
	JButton reload = new JButton("reload");
	reload.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
				
		reloadMainWindow();
	    }
	});
	
	getContentPane().add(reload);
    }
    
    private void reloadMainWindow()
    {
	setVisible(false);
	core.reload();
	core=null;
	dispose();
    }
}
