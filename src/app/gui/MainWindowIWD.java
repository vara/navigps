/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.gui.verboseTextPane.PanelForVerboseWindow;
import app.gui.buttons.ToolBarButton;
import app.ArgumentsStartUp;
import app.Main;
import app.gui.buttons.ToolBarToggleButton;
import app.gui.displayItemsMap.DetailsPanel;
import app.gui.displayItemsMap.MainDetailsPanel;
import app.gui.displayItemsMap.PanelWithBatikJTree;
import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.SVGBridgeListeners;
import app.gui.svgComponents.SVGScrollPane;
import app.gui.svgComponents.UpdateComponentsAdapter;
import app.utils.BridgeForVerboseMode;
import app.utils.MyFileFilter;
import app.utils.MyLogger;
import app.utils.OutputVerboseStream;
import config.GUIConfiguration;
import config.MainConfiguration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.WindowBar;
import net.infonode.docking.mouse.DockingWindowActionMouseButtonListener;
import net.infonode.docking.properties.DockingWindowProperties;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;
import odb.gui.Manager;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

/**
 *
 * @author vara
 */
public class MainWindowIWD extends JFrame implements WindowFocusListener,ItemListener{
    
    private Main core;
    private JPanel panelWithToolBars;
    private StatusPanel statusPanel;
    private Action openSVGFileAction,zoomOutAction,zoomInAction,zoomAction,fitToPanelAction,searchServicesAction;
    private Canvas canvas;
    private JCheckBoxMenuItem [] cbmOptionsForToolBars;
    private JToolBar toolBarFile,toolBarZoom,toolBarSerch,toolBarMemMonitor;
    private SVGBridgeListeners svgListeners = new SVGBridgeListeners();
    
    private OutputVerboseStream verboseStream = new BridgeForVerboseMode();
    
    private RootWindow rootWindow;
    private ViewMap viewMap = new ViewMap();//key -int i object -View
    private DockingWindowsTheme currentTheme = new ShapedGradientDockingTheme();
    private RootWindowProperties properties = new RootWindowProperties();
    
    private Vector <View> views = new Vector<View>();
    private static int ICON_SIZE = 8;

    private GraphicsDevice device = GUIConfiguration.getGraphicDevice();

    //private MySplitPane paneForProperties = new MySplitPane();    

    public MainWindowIWD(Main c){
        System.setOut(getVerboseStream().getOutputStream());
        System.setErr(getVerboseStream().getErrOutputStream());
        addWindowFocusListener(this);
        addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("prop name "+evt.getPropertyName());
                System.out.println("New Value "+evt.getNewValue());
                System.out.println("Old Value "+evt.getOldValue());
            }
        });

        MyLogger.log.log(Level.FINE,"Constructor "+getClass().toString());
        core = c;

        setSize(GUIConfiguration.getWindowSize());
        //always center location
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        initComponents();
        
        if(MainConfiguration.getPathToChartFile()!=null)
            openSVGDocument(MainConfiguration.getPathToChartFile());

        setTitle("NaviGPS ver. "+MainConfiguration.NAVIGPS_VERSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setDisplayMode();
        //if winidow init not FULL_SCREEN mode then must be invoke setVisible
        if(!isVisible())
            setVisible(true);
    }

    public MainWindowIWD(Main c,ArgumentsStartUp arg){
        this(c);
        //openSVGDocument(filePath);
    }
    
    private void initComponents(){

        openSVGFileAction = new OpenSVGFileAction("Open SVG document ...",
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
        searchServicesAction = new SearchServicesAction("Search Services",
                        createNavigationIcon("fitToPanel16"),
                        "Search services with a certain area",
                        KeyEvent.VK_S);

        svgListeners.setVerboseStream(getVerboseStream());
        svgListeners.addUpdateComponents(new UpdateMenuAndToolBars());

        //init docking RootWindow
        createRootWindow();
        setDefaultLayout();
        getContentPane().add(rootWindow, BorderLayout.CENTER);
        creatToolBars();	//must be before creatMenuBar()
        creatMenuBar();
        createStatusPanel();	//must be before createCanvasPanel()
        //createCanvas();
    }
    
    public OutputVerboseStream getVerboseStream(){
        return verboseStream;
    }
    
    private void setDefaultLayout() {
        if(views.size()>1){
            View[] viewsTabDockingWindow = new View[views.size()];
            for (int i = 0; i < viewsTabDockingWindow.length; i++) {
                viewsTabDockingWindow[i] = views.get(i);
            }
            TabWindow tabWindow = new TabWindow(viewsTabDockingWindow);
            int minus=0;
            if(MainConfiguration.isModeVerboseGui())
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
            if(MainConfiguration.isModeVerboseGui()){
                WindowBar windowBar = rootWindow.getWindowBar(Direction.DOWN);
                windowBar.addTab(views.lastElement());
            }
        }
	
    }
    private void createRootWindow() {
    
        //main window should always be first
        View vRoot = new View("SVG Document", VIEW_ICON, createCanvasWithScrollPane());
        freezeLayout(true,vRoot.getWindowProperties());
        views.add(vRoot);
        if(MainConfiguration.isShowDocumentProperties()){

            MainDetailsPanel detailsPanel = new MainDetailsPanel(getVerboseStream());
            detailsPanel.addToUpperContent(new PanelWithBatikJTree(canvas, getVerboseStream()));
            detailsPanel.addToLowerContent(new DetailsPanel());
            views.add(new View("Properties", VIEW_ICON,detailsPanel));
        }
        if(MainConfiguration.isModeVerboseGui()){
            PanelForVerboseWindow panel = 
                    new PanelForVerboseWindow((BridgeForVerboseMode)getVerboseStream());

            View vv = new View("Result return by functions",VIEW_ICON,panel);
            vv.getWindowProperties().setRestoreEnabled(false);
            JButtonActionForDebugWindow button = new JButtonActionForDebugWindow(BUTTON_ICON);
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

        //rootWindow.setPopupMenuFactory(null);
        rootWindow = DockingUtil.createRootWindow(viewMap,true);
        //rootWindow.add(createCanvas(),BorderLayout.CENTER);
        // Set gradient theme. The theme properties object is the super object of our properties object, which
        // means our property value settings will override the theme values
        properties.addSuperObject(currentTheme.getRootWindowProperties());

        // Our properties object is the super object of the root window properties object, so all property values of the
        // theme and in our property object will be used by the root window
        rootWindow.getRootWindowProperties().addSuperObject(properties);
        properties.setRecursiveTabsEnabled(false);
        properties.getDockingWindowProperties().setMaximizeEnabled(false);

        //Enable the bottom window bar
        if(MainConfiguration.isModeVerboseGui()){
            rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
            rootWindow.addTabMouseButtonListener(DockingWindowActionMouseButtonListener.MIDDLE_BUTTON_CLOSE_LISTENER);
        }       
    }
    
    private void freezeLayout(boolean freeze,DockingWindowProperties prop) {
        // Freeze window operations
        prop.setDragEnabled(!freeze);
        prop.setCloseEnabled(!freeze);
        prop.setMinimizeEnabled(!freeze);
        prop.setRestoreEnabled(!freeze);
        prop.setMaximizeEnabled(!freeze);
        prop.setUndockEnabled(!freeze);
        prop.setDockEnabled(!freeze);
    }
    
    public static ImageIcon createNavigationIcon(String imageName) {
        return createNavigationIcon(imageName,"png");
    }
    public static ImageIcon createNavigationIcon(String imageName,String ext) {
        String imgLocation = "resources/graphics/icons/"
                             + imageName
                             + "."+ext;
        URL imageURL = MainWindowIWD.class.getResource(imgLocation);

        if (imageURL == null) {
            System.err.println("Resource not found: "
                               + imgLocation);
            return null;
        } else {
            return new ImageIcon(imageURL);

        }
    }
    public static String createNavigationIconPath(String imageName,String ext){
        String imgLocation = "resources/graphics/icons/"
                             + imageName
                             + "."+ext;
        URL imageURL = MainWindowIWD.class.getResource(imgLocation);
        if (imageURL == null) {
            System.err.println("Resource not found: "
                               + imgLocation);
            return null;
        }
        return imageURL.getPath();
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
        toolBarSerch = new JToolBar("ToolBar Serch");
        toolBarMemMonitor = new JToolBar("Memory Monitor"){
            @Override
            public void setVisible(boolean b){
                super.setVisible(b);
                Component[] comp = getComponents();
                    for (Component component : comp) {
                        component.setVisible(b);
                    }
                }
        };

        toolBarFile.add(new ToolBarButton(openSVGFileAction,
                          createNavigationIcon("open32"),
                          getVerboseStream()));
        toolBarZoom.add(new ToolBarButton(zoomInAction,
                          createNavigationIcon("zoomIn32"),
                          getVerboseStream()));
        toolBarZoom.add(new ToolBarButton(zoomOutAction,
                          createNavigationIcon("zoomOut32"),
                          getVerboseStream()));
        toolBarZoom.add(new ToolBarToggleButton(zoomAction,
                          createNavigationIcon("zoom32"),
                          getVerboseStream()));
        toolBarZoom.add(new ToolBarButton(fitToPanelAction,
                          createNavigationIcon("fitToPanel32"),
                          getVerboseStream()));        
        toolBarMemMonitor.add(new MemoryGui(getVerboseStream()));
        toolBarMemMonitor.setMargin(new Insets(4,1,4,1));
        ToolBarToggleButton tb = new ToolBarToggleButton(searchServicesAction,
                          createNavigationIcon("fitToPanel32"),
                          getVerboseStream());
        toolBarSerch.add(tb);

        ((SearchServicesAction)searchServicesAction).addToSelectableGroup(tb);

        panelWithToolBars.add(toolBarMemMonitor,FlowLayout.LEFT);
        panelWithToolBars.add(toolBarSerch,FlowLayout.LEFT);
        panelWithToolBars.add(toolBarZoom,FlowLayout.LEFT);
        panelWithToolBars.add(toolBarFile,FlowLayout.LEFT);

        getContentPane().add(panelWithToolBars,BorderLayout.PAGE_START);
	
    }
    
    public void creatMenuBar(){
	
        JMenuBar jmb = new JMenuBar();

        JMenuItem itemFile = new JMenuItem(openSVGFileAction);
        JMenuItem itemExit = new JMenuItem("Exit");
        itemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            exitApp();
            }
        });

        JMenuItem odbManager = new JMenuItem("ODBManager");
            odbManager.addActionListener(new ActionListener() {

            @Override
                public void actionPerformed(ActionEvent e) {

                    Manager odb = new Manager(MainWindowIWD.this,true);
                    odb.setVisible(true);
                    //throw new UnsupportedOperationException("Unsupported exception!");
                }
            });

        JMenuItem itemZoomIn = new JMenuItem(zoomInAction);
        JMenuItem itemZoomOut = new JMenuItem(zoomOutAction);
        JMenuItem itemfitToPanel = new JMenuItem(fitToPanelAction);
        JCheckBoxMenuItem itemSearchServices  = new JCheckBoxMenuItem(searchServicesAction);
        itemSearchServices.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                System.out.println(((AbstractButton)e.getSource()).getAction().getValue(Action.NAME)+" is selected "+((AbstractButton)e.getSource()).isSelected());
            }
        });
        ((SearchServicesAction)searchServicesAction).addToSelectableGroup(itemSearchServices);

        JMenu menuFile = new JMenu("File");
        JMenu menuView = new JMenu("View");
        JMenu subMenuForToolBarOptions = new JMenu("ToolBars");
        JMenu menuODB = new JMenu("Database");

        cbmOptionsForToolBars = new JCheckBoxMenuItem[5];

        cbmOptionsForToolBars[0] = new JCheckBoxMenuItem("File Open");
        cbmOptionsForToolBars[1] = new JCheckBoxMenuItem("Zoom Action");
        cbmOptionsForToolBars[2] = new JCheckBoxMenuItem("Memory Monitor");
        cbmOptionsForToolBars[3] = new JCheckBoxMenuItem("Search Services");
        cbmOptionsForToolBars[4] = new JCheckBoxMenuItem("ToolBar");

        for (JCheckBoxMenuItem cb : cbmOptionsForToolBars) {
            cb.addItemListener(this);
            cb.setSelected(true);
            subMenuForToolBarOptions.add(cb);
        }

        JMenuItem dispFull = new JMenuItem(new DisplayModeAction("Full Screen", null,
                    "Display Full Screen", KeyEvent.VK_ENTER));

        menuFile.add(itemFile);
        menuFile.addSeparator();
        menuFile.add(itemExit);
        menuView.add(itemZoomIn);
        menuView.add(itemZoomOut);
        menuView.add(itemfitToPanel);
        menuView.addSeparator();
        menuView.add(itemSearchServices);
        menuView.addSeparator();
        menuView.add(subMenuForToolBarOptions);
        menuView.addSeparator();
        menuView.add(dispFull);
        menuODB.add(odbManager);

        jmb.add(menuFile);
        jmb.add(menuView);
        jmb.add(menuODB);
        setJMenuBar(jmb);
    }
    public Canvas createCanvas(){
	
        canvas = new Canvas(svgListeners);
        canvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            @Override
            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                getVerboseStream().outputErrorVerboseStream("Bounds GVT ROOT"+e.getGVTRoot().getBounds());
            }
        });
        canvas.setBackground(Color.white);
        return canvas;
    }
    
    public JComponent createCanvasWithScrollPane(){
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
                openSVGDocument(chooser.getSelectedFile());

            } catch (Exception e1) {
                getVerboseStream().outputVerboseStream(e1.getMessage());
            }
        }
    }
    
    public void openSVGDocument(String path){
	
        File file = new File(path);
        if(file.exists()){
            openSVGDocument(file);
        }else{
            System.err.println("File "+path+" doesn't exist !!!");
            MyLogger.log.log(Level.WARNING,"File "+path+" doesn't exist !!!");
        }
	
    }
    public void openSVGDocument(File file){	
        canvas.setURI(file.toURI().toString());
    }
    
    protected StatusPanel getStatusPanel() {
        return statusPanel;
    }
    public void exitApp(){
        //do something before exit app !!!

        System.exit(0);
    }
    
    //unused function !!!
    private void reloadMainWindow(){
        setVisible(false);
        core.reload();
        core=null;
        dispose();
    }
    
    public void setComponetsZoomEnable(boolean b){
        zoomAction.setEnabled(b);
        zoomInAction.setEnabled(b);
        zoomOutAction.setEnabled(b);
        fitToPanelAction.setEnabled(b);
        searchServicesAction.setEnabled(b);
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBoxMenuItem mi = (JCheckBoxMenuItem)(e.getSource());
	
        boolean selected = (e.getStateChange() == ItemEvent.SELECTED);

        //Set the enabled state of the appropriate Action.
        if (mi == cbmOptionsForToolBars[0]) {
            toolBarFile.setVisible(selected);
        } else if (mi == cbmOptionsForToolBars[1]) {	    
            toolBarZoom.setVisible(selected);
        }else if (mi == cbmOptionsForToolBars[2]) {
            toolBarMemMonitor.setVisible(selected);
        }else if (mi == cbmOptionsForToolBars[3]){
            toolBarSerch.setVisible(selected);
        }else if (mi == cbmOptionsForToolBars[4]) {
            panelWithToolBars.setVisible(selected);
            //stop working memory monitor when 'selected' == false
            ((MemoryGui)toolBarMemMonitor.getComponentAtIndex(0)).setVisible(selected);
        }
    }
    
    private static final Icon BUTTON_ICON = new Icon() {
        @Override
        public int getIconHeight() {
            return ICON_SIZE;
        }
        @Override
        public int getIconWidth() {
            return ICON_SIZE;
        }
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color oldColor = g.getColor();
            g.setColor(Color.BLACK);
            g.fillOval(x, y, ICON_SIZE, ICON_SIZE);
            g.setColor(oldColor);
        }
    };
    
    private static final Icon VIEW_ICON = new Icon() {
        @Override
        public int getIconHeight() {
            return ICON_SIZE;
        }
        @Override
        public int getIconWidth() {
            return ICON_SIZE;
        }
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color oldColor = g.getColor();
            g.setColor(new Color(70, 70, 70));
            g.fillRect(x, y, ICON_SIZE, ICON_SIZE);
            g.setColor(new Color(100, 230, 100));
            g.fillRect(x + 1, y + 1, ICON_SIZE - 2, ICON_SIZE - 2);
            g.setColor(oldColor);
        }
    };

    @Override
    public void windowGainedFocus(WindowEvent e) {
        getVerboseStream().outputVerboseStream("windowGainedFocus");
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        getVerboseStream().outputVerboseStream("windowLostFocus");
    }

    private class OpenSVGFileAction extends AbstractAction {
        public OpenSVGFileAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
            putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.ALT_DOWN_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            getVerboseStream().outputVerboseStream("Open SVG file ...");
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
            setEnabled(false);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
	    canvas.zoomFromCenterDocumnet(true);
            getVerboseStream().outputVerboseStream("Zoom in");
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
            setEnabled(false);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
	    canvas.zoomFromCenterDocumnet(false);
            getVerboseStream().outputVerboseStream("Zoom out");
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
            setEnabled(false);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            AbstractButton button = (AbstractButton)e.getSource();
            boolean selected = button.isSelected();
            canvas.zoomFromMouseCoordinationEnable(selected);
            getVerboseStream().outputVerboseStream("Zoom from mouse position "+(selected ? "Disabled":"Enabled"));
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
            setEnabled(false);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            canvas.resetRenderingTransform();
            getVerboseStream().outputVerboseStream("Fit to panel");
        }	
    }
    private class JButtonActionForDebugWindow extends JButton implements ActionListener{
        public JButtonActionForDebugWindow(Icon ico){
            super(ico);
            addActionListener(this);
            setOpaque(false);
            setBorder(null);
            setFocusable(false);
            setToolTipText("get document transforms");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            SVGDocument doc = canvas.getSVGDocument();
            SVGSVGElement el = doc.getRootElement();
            String viewBoxStr = el.getAttributeNS(null, SVGConstants.SVG_VIEW_BOX_ATTRIBUTE);
            float[] rect = ViewBox.parseViewBoxAttribute(el, viewBoxStr, null);
            Rectangle2D rect2d =  new Rectangle2D.Float(rect[0], rect[1],rect[2], rect[3]);
            GraphicsNode gn = canvas.getGraphicsNode();
            Rectangle2D bounds = gn.getBounds();

            CanvasGraphicsNode cgn = canvas.getCanvasGraphicsNode();
            getVerboseStream().outputVerboseStream("-------- Transform SVG Document --------");
            getVerboseStream().outputVerboseStream("\t"+getClass().getName()+"");
            getVerboseStream().outputVerboseStream("ViewingTransform\t"+canvas.getViewingTransform());
            getVerboseStream().outputVerboseStream("ViewBoxTransform\t"+canvas.getViewBoxTransform());
            getVerboseStream().outputVerboseStream("RenderingTransform\t"+canvas.getRenderingTransform());
            getVerboseStream().outputVerboseStream("PaintingTransform\t"+canvas.getPaintingTransform());
            getVerboseStream().outputVerboseStream("InitialTransform\t"+canvas.getInitialTransform());
            getVerboseStream().outputVerboseStream("Visible Rect\t"+canvas.getVisibleRect());
            getVerboseStream().outputVerboseStream("ViewBox Rect\t"+rect2d+"\t"+bounds);
            getVerboseStream().outputVerboseStream("-------- Transform Graphics Nood -------");
            getVerboseStream().outputVerboseStream("PositionTransform\t"+cgn.getPositionTransform());
            getVerboseStream().outputVerboseStream("ViewingTransform\t"+cgn.getViewingTransform());
            getVerboseStream().outputVerboseStream("GlobalTransform\t"+cgn.getGlobalTransform());
            getVerboseStream().outputVerboseStream("InverseTransform\t"+cgn.getInverseTransform());
            getVerboseStream().outputVerboseStream("Transform\t"+cgn.getTransform());
            getVerboseStream().outputVerboseStream("----------------------------------------");

        }
    }
    
    private class UpdateMenuAndToolBars extends UpdateComponentsAdapter{
	
        @Override
        public void documentPrepareToModification(){

            Thread w = new Thread(new Runnable() {
                @Override
                public void run() {
                    setComponetsZoomEnable(true);
                    views.firstElement().getViewProperties().setTitle(svgListeners.getAbsoluteFilePath());                    
                }
            });
            w.start();
        }

        @Override
        public void documentClosed(){

            setComponetsZoomEnable(false);
        }
    }

    public abstract class ActionNotifyALLCompGroup extends AbstractAction {
        private Vector<AbstractButton> vecToggle = new Vector<AbstractButton>();

        public ActionNotifyALLCompGroup(String text, ImageIcon icon){
            super(text, icon);
            addPropertyChangeListener(new Listener());
        }

        public void addToSelectableGroup(AbstractButton ab){
            vecToggle.add(ab);
        }

        private class Listener implements PropertyChangeListener{
            @Override
            public void propertyChange(PropertyChangeEvent evt) {                
                if(evt.getPropertyName().equals(AbstractAction.SELECTED_KEY)){
                    AbstractAction tmp = (AbstractAction)evt.getSource();
                    for (AbstractButton abstractButton : vecToggle) {
                        if(!abstractButton.getAction().equals(tmp)){
                            //System.out.println(""+abstractButton);
                            abstractButton.setSelected((Boolean)evt.getNewValue());
                        }
                    }
                }
            }
        }
    }

    private class SearchServicesAction extends ActionNotifyALLCompGroup {

        public SearchServicesAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
            putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.ALT_DOWN_MASK));
            setEnabled(false);
        }
        @Override
        public void actionPerformed(ActionEvent e) {

            AbstractButton button = (AbstractButton)e.getSource();            
            boolean en = button.getModel().isSelected();
            putValue(AbstractAction.SELECTED_KEY,new Boolean(en));          
            canvas.getSearchServices().setEnabled(en);
            getVerboseStream().outputVerboseStream("Search services enabled "+en);
        }
    }

    private void setDisplayMode(){
        if(GUIConfiguration.getModeScreen()==GUIConfiguration.FULL_SCREEN){
            getVerboseStream().outputVerboseStream("full screen mode "+device.isFullScreenSupported());
            this.setUndecorated(true);
            device.setFullScreenWindow(this);
        }else{
            device.setFullScreenWindow(null);
            this.setUndecorated(false);
        }
    }
    public void switchDisplayMode(){
        GUIConfiguration.setModeScreen((byte) (0x01 & ~GUIConfiguration.getModeScreen()));
        setDisplayMode();
    }

    private class DisplayModeAction extends AbstractAction{
        public DisplayModeAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
            putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(mnemonic,InputEvent.SHIFT_DOWN_MASK|InputEvent.CTRL_DOWN_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            switchDisplayMode();
        }
    }
}
