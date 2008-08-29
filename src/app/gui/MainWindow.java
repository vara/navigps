/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.Main;
import app.gui.svgComponents.Canvas;
import app.utils.MyFileFilter;
import config.Configuration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/**
 *
 * @author vara
 */
public class MainWindow extends JFrame implements ItemListener{

    private Main core;
    
    private JPanel panelWithToolBars;
    private Action openSVGFiletAction,zoomOutAction,zoomInAction;
    private Canvas canvas;
    private JCheckBoxMenuItem [] cbmOptionsForToolBars;
    private JToolBar toolBarFile,toolbarZoom;
    
    
    public MainWindow(Main c)
    {
	core = c;
	setSize(Configuration.getScreenSize());	
	setLayout(new BorderLayout());
	
	initComponents();
	
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);
	
    }
    
    private void initComponents()
    {
	openSVGFiletAction = new OpenSVGFiletAction("Open SVG document ...", 
				    createNavigationIcon("open16"), "Open SVG Document",
				    KeyEvent.VK_O);
	zoomInAction = new ZoomInAction("Zoom In", 
				    createNavigationIcon("zoomIn16"), 
				    "Zoom in from center document", 
				    KeyEvent.VK_PLUS);
	zoomOutAction = new ZoomOutAction("Zoom Out", 
				    createNavigationIcon("zoomOut16"), 
				    "Zoom out from center document", 
				    KeyEvent.VK_MINUS);
	creatToolBars();
	creatMenuBar();
	createCanvas();
    }
    protected static ImageIcon createNavigationIcon(String imageName) {
        String imgLocation = "resources/graphics/icons/"
                             + imageName
                             + ".png";
        java.net.URL imageURL = MainWindow.class.getResource(imgLocation);

        if (imageURL == null) {
            System.err.println("Resource not found: "
                               + imgLocation);
            return null;
        } else {
            return new ImageIcon(imageURL);
        }
    }
    
    public void creatToolBars(){
    
	panelWithToolBars = new JPanel(new FlowLayout(0,0,0));
	
	toolBarFile = new JToolBar("ToolBar File");
	toolbarZoom = new JToolBar("ToolBar Zoom");
	
	toolBarFile.add(new ToolBarButton(openSVGFiletAction, 
					  createNavigationIcon("open32")));
	toolbarZoom.add(new ToolBarButton(zoomInAction, 
					  createNavigationIcon("zoomIn32")));
	toolbarZoom.add(new ToolBarButton(zoomOutAction, 
					  createNavigationIcon("zoomOut32")));
	
	panelWithToolBars.add(toolBarFile,FlowLayout.LEFT);
	panelWithToolBars.add(toolbarZoom,FlowLayout.LEFT);
	
	getContentPane().add(panelWithToolBars,BorderLayout.PAGE_START);
    }
    
    public void creatMenuBar(){
	
	JMenuBar jmb = new JMenuBar();
	
	JMenuItem itemFile = new JMenuItem(openSVGFiletAction);
	JMenuItem itemExit = new JMenuItem("Exit");
	itemExit.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		exitApp();
	    }
	});
	JMenuItem itemZoomIn = new JMenuItem(zoomInAction);
	JMenuItem itemZoomOut = new JMenuItem(zoomOutAction);
	
	JMenu menuFile = new JMenu("File");
	JMenu menuView = new JMenu("View");
	JMenu subMenuForToolBarOptions = new JMenu("ToolBars");
	
	cbmOptionsForToolBars = new JCheckBoxMenuItem[3];
	
	cbmOptionsForToolBars[0] = new JCheckBoxMenuItem("File");
	cbmOptionsForToolBars[1] = new JCheckBoxMenuItem("Zoom");
	cbmOptionsForToolBars[2] = new JCheckBoxMenuItem("ToolBar");
	
	for (JCheckBoxMenuItem cb : cbmOptionsForToolBars) {
	    cb.addItemListener(this);
	    cb.setSelected(true);
	    subMenuForToolBarOptions.add(cb);
	}
		
	menuFile.add(itemFile);
	menuFile.addSeparator();
	menuFile.add(itemExit);
	menuView.add(itemZoomIn);
	menuView.add(itemZoomOut);
	menuView.addSeparator();
	menuView.add(subMenuForToolBarOptions);
	
	
	jmb.add(menuFile);
	jmb.add(menuView);
	setJMenuBar(jmb);	
    }
    public void createCanvas(){
	
	canvas = new Canvas();
	canvas.setBackground(Color.RED);
	
	getContentPane().add(canvas,BorderLayout.CENTER);
    }
    
    private void openFileChoserWindow(){
	JFileChooser chooser = new JFileChooser();
	String fileFilter = "svg";
	chooser.addChoosableFileFilter(new MyFileFilter(new String[]{"svg"},fileFilter));
	chooser.setAcceptAllFileFilterUsed(false);
	chooser.setCurrentDirectory(new File(""));
	int retour = chooser.showOpenDialog(this);
	
	if(retour == JFileChooser.APPROVE_OPTION) {
	    try {
		    File f =chooser.getSelectedFile();
	    } catch (Exception e1) {
		    e1.printStackTrace();
	    }
	}
    }
    
    public class OpenSVGFiletAction extends AbstractAction {
        public OpenSVGFiletAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
	    putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.CTRL_MASK));
        }
        public void actionPerformed(ActionEvent e) {
	    openFileChoserWindow();
	}
    }
    
    public class ZoomInAction extends AbstractAction {
        public ZoomInAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
	    putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.CTRL_MASK));
        }
        public void actionPerformed(ActionEvent e) {
	              
        }
    }
    
    public class ZoomOutAction extends AbstractAction {
        public ZoomOutAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
	    putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.CTRL_MASK));
        }
        public void actionPerformed(ActionEvent e) {
	              
        }
    }
    
    public void exitApp(){
	System.exit(0);
    }
    
    private void reloadMainWindow()
    {
	setVisible(false);
	core.reload();
	core=null;
	dispose();
    }

    public void itemStateChanged(ItemEvent e) {
	JCheckBoxMenuItem mi = (JCheckBoxMenuItem)(e.getSource());
	
        boolean selected = (e.getStateChange() == ItemEvent.SELECTED);

        //Set the enabled state of the appropriate Action.
        if (mi == cbmOptionsForToolBars[0]) {
            toolBarFile.setVisible(selected);
        } else if (mi == cbmOptionsForToolBars[1]) {
            toolbarZoom.setVisible(selected);
        } else if (mi == cbmOptionsForToolBars[2]) {
            panelWithToolBars.setVisible(selected);
	    cbmOptionsForToolBars[0].setEnabled(selected);
	    cbmOptionsForToolBars[1].setEnabled(selected);
	
	}
    }
}
