/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.Main;
import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.SVGBridgeListeners;
import app.gui.svgComponents.SVGScrollPane;
import app.utils.JTextPaneForVerboseInfo;
import app.utils.MyFileFilter;
import config.MainConfiguration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.net.URL;
import java.util.ListIterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.WindowBar;
import net.infonode.docking.mouse.DockingWindowActionMouseButtonListener;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;

/**
 *
 * @author vara
 */
public class MainWindowIWD extends JFrame implements ItemListener{

    
    private Main core;
    
    private JPanel panelWithToolBars;
    private StatusPanel statusPanel;
    private Action openSVGFiletAction,zoomOutAction,zoomInAction,zoomAction,fitToPanelAction;
    private Canvas canvas;
    private JCheckBoxMenuItem [] cbmOptionsForToolBars;
    private JToolBar toolBarFile,toolBarZoom;
    private SVGBridgeListeners svgListeners = new SVGBridgeListeners();
    private JTextPaneForVerboseInfo verbosePane = new JTextPaneForVerboseInfo();
    
    private RootWindow rootWindow;
    private ViewMap viewMap = new ViewMap();//key -int i object -View
    private DockingWindowsTheme currentTheme = new ShapedGradientDockingTheme();
    private RootWindowProperties properties = new RootWindowProperties();
    
    private Vector <View> views = new Vector<View>();
    private static int ICON_SIZE = 8;
    
    
    
    private static class DynamicView extends View {
	
	private int id;
	
	DynamicView(String title, Icon icon, Component component, int id) {
	  super(title, icon, component);
	  this.id = id;
	}
	
	public int getId() {
	  return id;
	}
    }
    
    public MainWindowIWD(Main c){
	
	core = c;
	setSize(MainConfiguration.getScreenSize());	
	setLayout(new BorderLayout());
	
	initComponents();
	
	setTitle("NaviGPS ver. 0.3");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);
    }
    
    private void initComponents()
    {
	openSVGFiletAction = new OpenSVGFileAction("Open SVG document ...", 
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
	fitToPanelAction = new FitToPanelAction("Fit to Panel",
				    createNavigationIcon("fitToPanel16"),
				    "Center your svg map",
				    KeyEvent.VK_F);
	
	
	//init docking RootWindow
	createRootWindow();
	setDefaultLayout();
	
	 
	getContentPane().add(rootWindow, BorderLayout.CENTER);    
    
	
	creatToolBars();	//must be before creatMenuBar()
	creatMenuBar();
	createStatusPanel();	//must be before createCanvasPanel() 
	//createCanvas();
    }
    
    private void setDefaultLayout() {
	
	View[] viewsTabDockingWindow = new View[views.size()];
	for (int i = 0; i < viewsTabDockingWindow.length; i++) {
	    viewsTabDockingWindow[i] = views.get(i);	    
	}
	
	TabWindow tabWindow = new TabWindow(viewsTabDockingWindow);
	int minus=0;
	if(MainConfiguration.getMode())
	    minus =2;//first and last window (CanvasWindow and DebugWindow)
	else
	    minus=1; //only first window
	
	View [] splitTab = new View[views.size()-minus];
	
	//only windows on left side
	for (int i = 0; i < viewsTabDockingWindow.length-minus; i++) {
	    splitTab[i]= viewsTabDockingWindow[i+1];	    
	}
	
	SplitWindow splitWindow = new SplitWindow(true,0.2f,new TabWindow(splitTab),views.lastElement());
	rootWindow.setWindow(new SplitWindow(true,0.2f,splitWindow,tabWindow));
	rootWindow.setPopupMenuFactory(null);
	if(MainConfiguration.getMode()){
	    WindowBar windowBar = rootWindow.getWindowBar(Direction.DOWN);
	    windowBar.addTab(views.lastElement());
	}
	
    }
    private void createRootWindow() {
    
	//main window should always be first
	views.add(new View("SVG Document", VIEW_ICON, createCanvasWithScrollPane()) );
	views.add(new View("Properties " + 1, VIEW_ICON, new JTextArea()));
	//views.add(new View("Properties " + 2, VIEW_ICON, new JTextArea()));

	if(MainConfiguration.getMode()){
	    View vv = new View("Result return by functions",VIEW_ICON,verbosePane);
	    JButton button = new JButton(BUTTON_ICON);
	    button.setOpaque(false);
	    button.setBorder(null);
	    button.setFocusable(false);
	    button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	    
		JOptionPane.showMessageDialog(getOwner(),
					  "You clicked the custom view button.",
					  "Custom View Button",
					  JOptionPane.INFORMATION_MESSAGE);
		}
	    });
	    
	    vv.getCustomTabComponents().add(button);
	    views.add(vv);
	}
	for(int i=0;i<views.size();i++){
	    viewMap.addView(i, views.get(i));
	}	
	
	// The mixed view map makes it easy to mix static and dynamic views inside the same root window
	//MixedViewHandler handler = new MixedViewHandler(viewMap, new ViewSerializer() {
	//  public void writeView(View view, ObjectOutputStream out) throws IOException {
	//    out.writeInt(((DynamicView) view).getId());
	//  }

	//  public View readView(ObjectInputStream in) throws IOException {
	//    return getDynamicView(in.readInt());
	//  }
	//});
	//rootWindow = DockingUtil.createRootWindow(viewMap,handler,true);
	
	rootWindow = DockingUtil.createRootWindow(viewMap,true);
	//rootWindow.add(createCanvas(),BorderLayout.CENTER);
	// Set gradient theme. The theme properties object is the super object of our properties object, which
	// means our property value settings will override the theme values
	properties.addSuperObject(currentTheme.getRootWindowProperties());	
	
	// Our properties object is the super object of the root window properties object, so all property values of the
	// theme and in our property object will be used by the root window
	rootWindow.getRootWindowProperties().addSuperObject(properties);
	
	//Enable the bottom window bar
	rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);

	rootWindow.addTabMouseButtonListener(DockingWindowActionMouseButtonListener.MIDDLE_BUTTON_CLOSE_LISTENER);
	freezeLayout(true);
    }
    
    private void freezeLayout(boolean freeze) {
	// Freeze window operations
	properties.getDockingWindowProperties().setDragEnabled(!freeze);
	properties.getDockingWindowProperties().setCloseEnabled(!freeze);
	properties.getDockingWindowProperties().setMinimizeEnabled(!freeze);
	properties.getDockingWindowProperties().setRestoreEnabled(!freeze);
	properties.getDockingWindowProperties().setMaximizeEnabled(!freeze);
	properties.getDockingWindowProperties().setUndockEnabled(!freeze);
	properties.getDockingWindowProperties().setDockEnabled(!freeze);

    }
    
    protected static ImageIcon createNavigationIcon(String imageName) {
        String imgLocation = "resources/graphics/icons/"
                             + imageName
                             + ".png";
        URL imageURL = MainWindowIWD.class.getResource(imgLocation);

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
	    @Override
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
	toolBarZoom.add(new ToolBarButton(zoomAction, 
					  createNavigationIcon("fitToPanel32")));
		
	panelWithToolBars.add(toolBarZoom,FlowLayout.LEFT);
	panelWithToolBars.add(toolBarFile,FlowLayout.LEFT);
	
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
	JMenuItem itemfitToPanel = new JMenuItem(fitToPanelAction);
	
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
	menuView.add(itemfitToPanel);
	menuView.addSeparator();
	menuView.add(subMenuForToolBarOptions);
	
	
	jmb.add(menuFile);
	jmb.add(menuView);
	setJMenuBar(jmb);	
    }
    public Canvas createCanvas(){
	
	canvas = new Canvas(svgListeners);
	canvas.setBackground(Color.white);
	
	//getContentPane().add(canvas,BorderLayout.CENTER);
	//line only for accelerate tests
	//canvas.setURI("file:./MapWorld.svg");
	return canvas;
    }
    
    public SVGScrollPane createCanvasWithScrollPane(){
	if(canvas == null)
	    return new SVGScrollPane(createCanvas());
	return new SVGScrollPane(canvas);
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
    
    private static final Icon BUTTON_ICON = new Icon() {
	public int getIconHeight() {
	  return ICON_SIZE;
	}

	public int getIconWidth() {
	  return ICON_SIZE;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
	  Color oldColor = g.getColor();

	  g.setColor(Color.BLACK);
	  g.fillOval(x, y, ICON_SIZE, ICON_SIZE);

	  g.setColor(oldColor);
	}
    };
    
    private static final Icon VIEW_ICON = new Icon() {
	public int getIconHeight() {
	  return ICON_SIZE;
	}

	public int getIconWidth() {
	  return ICON_SIZE;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
	  Color oldColor = g.getColor();

	  g.setColor(new Color(70, 70, 70));
	  g.fillRect(x, y, ICON_SIZE, ICON_SIZE);

	  g.setColor(new Color(100, 230, 100));
	  g.fillRect(x + 1, y + 1, ICON_SIZE - 2, ICON_SIZE - 2);

	  g.setColor(oldColor);
	}
    };
    
    private class OpenSVGFileAction extends AbstractAction {
        public OpenSVGFileAction(String text, ImageIcon icon,
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
    
    private class ZoomInAction extends AbstractAction {
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
    
    private class ZoomOutAction extends AbstractAction {
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
    
    private class ZoomAction extends AbstractAction {
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
    
    private class FitToPanelAction extends AbstractAction {
        public FitToPanelAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
	    putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.ALT_DOWN_MASK));
        }
        public void actionPerformed(ActionEvent e) {
	    	    
        }	
    }
}
