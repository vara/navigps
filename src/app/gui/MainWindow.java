/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.Main;
import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.SVGBridgeListeners;
import app.utils.MyFileFilter;
import config.MainConfiguration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
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
    private StatusPanel statusPanel;
    private Action openSVGFiletAction,zoomOutAction,zoomInAction,zoomAction;
    private Canvas canvas;
    private JCheckBoxMenuItem [] cbmOptionsForToolBars;
    private JToolBar toolBarFile,toolBarZoom;
    private SVGBridgeListeners svgListeners = new SVGBridgeListeners();
    
    public MainWindow(Main c)
    {
	core = c;
	setSize(MainConfiguration.getScreenSize());	
	setLayout(new BorderLayout());
	
	initComponents();
	
	setTitle("NaviGPS ver. 0.2");
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
	zoomAction = new ZoomAction("Zoom", 
				    createNavigationIcon("zoom16"), 
				    "Zoom from svg coordinate (in/out Left/Right Mouse Key)", 
				    KeyEvent.VK_Z);
	
		
	creatToolBars();	//must be before creatMenuBar()
	creatMenuBar();
	createStatusPanel();	//must be before createCanvasPanel() 
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
    
    public void createStatusPanel(){
		
	statusPanel = new StatusPanel(svgListeners);		
	//statusPanel.setBorder(Utils.createSimpleBorder(0,0,0,0,new Color(174,201,255)));
	
	getContentPane().add(getStatusPanel(),BorderLayout.PAGE_END);
    }
    
    public void creatToolBars(){
    
	panelWithToolBars = new JPanel(new FlowLayout(0,0,0)){
	    public void paintComponent(Graphics g){
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g;	    
		GradientPaint gradient1 = new GradientPaint(0.0f, (float) getHeight(),Color.white, 
							    0.0f, 8.5f, new Color(235,245,255));			    
		Rectangle rec1 = new Rectangle(0, 0, getWidth(), getHeight());
		g2.setPaint(gradient1);
		g2.fill(rec1);
	    }
	};
	
	toolBarFile = new JToolBar("ToolBar File");
	toolBarZoom = new JToolBar("ToolBar Zoom");
	
	toolBarFile.add(new ToolBarButton(openSVGFiletAction, 
					  createNavigationIcon("open32")));
	toolBarZoom.add(new ToolBarButton(zoomInAction, 
					  createNavigationIcon("zoomIn32")));
	toolBarZoom.add(new ToolBarButton(zoomOutAction, 
					  createNavigationIcon("zoomOut32")));
		
	toolBarZoom.add(new ToolBarButton(zoomAction, 
					  createNavigationIcon("zoom32"),true));
	
	panelWithToolBars.add(toolBarFile,FlowLayout.LEFT);
	panelWithToolBars.add(toolBarZoom,FlowLayout.LEFT);
	
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
	
	canvas = new Canvas(svgListeners);
	canvas.setBackground(Color.white);
	
	getContentPane().add(canvas,BorderLayout.CENTER);
	//line only for accelerate tests
	//canvas.setURI("file:./MapWorld.svg");
    }
    
    private void openFileChoserWindow(){
	JFileChooser chooser = new JFileChooser();
	String fileFilter = "svg";
	chooser.addChoosableFileFilter(new MyFileFilter(new String[]{"svg"},fileFilter));
	chooser.setAcceptAllFileFilterUsed(false);	
	if(canvas.isDocumentSet()){
	    
	    try {
		String lastPath = canvas.getSVGDocument().getDocumentURI();
		URI uri = new URI(lastPath);
		chooser.setCurrentDirectory(new File(uri));
	    } catch (URISyntaxException ex) {
		System.err.println(""+ex);
	    }	    
	}	
	int retour = chooser.showOpenDialog(this);	
	if(retour == JFileChooser.APPROVE_OPTION) {
	    try {
		    File f =chooser.getSelectedFile();
		    //we have 2 ways to loading svg documnets
		    //1.
		    //SVGDocument doc = SVGLoader.getSVGDocumentFromFile(f);
		    //if(doc !=null)
		    //canvas.setDocument(doc);
		    //2.
		    canvas.setURI(f.toURI().toString());
		    
	    } catch (Exception e1) {
		    e1.printStackTrace();
	    }
	}
    }

    protected StatusPanel getStatusPanel() {
	return statusPanel;
    }
    
    public class OpenSVGFiletAction extends AbstractAction {
        public OpenSVGFiletAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
	    putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.ALT_DOWN_MASK));
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
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.ALT_DOWN_MASK));
        }
        public void actionPerformed(ActionEvent e) {
	    canvas.zoomFromCenterDocumnet(true);      
        }
    }
    
    public class ZoomOutAction extends AbstractAction {
        public ZoomOutAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
	    putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.ALT_DOWN_MASK));
        }
        public void actionPerformed(ActionEvent e) {
	    canvas.zoomFromCenterDocumnet(false);
        }
    }
    
    public class ZoomAction extends AbstractAction {
        public ZoomAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
	    putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.ALT_DOWN_MASK));
        }
        public void actionPerformed(ActionEvent e) {
	    ToolBarButton button = (ToolBarButton)e.getSource();
	    boolean selected = button.isSelectedButton();	    
	    System.out.println(""+selected);
	    if(selected){
		canvas.zoomFromMouseCoordinationEnable(!selected);
		button.setSelected(!selected);
		if(MainConfiguration.getMode())
		    System.out.println("Zoom Disabled");		    
	    }else{
		canvas.zoomFromMouseCoordinationEnable(!selected);
		button.setSelected(!selected);
		if(MainConfiguration.getMode())
		    System.out.println("Zoom Enabled");
	    }
	    
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
            toolBarZoom.setVisible(selected);
        } else if (mi == cbmOptionsForToolBars[2]) {
            panelWithToolBars.setVisible(selected);
	    cbmOptionsForToolBars[0].setEnabled(selected);
	    cbmOptionsForToolBars[1].setEnabled(selected);
	
	}
    }
}
