package app.navigps.gui;

import app.navigps.gui.VerboseTextPane.PanelForVerboseWindow;
import app.navigps.NaviGPSCore;
import app.navigps.gui.displayItemsMap.DetailsPanel;
import app.navigps.gui.displayItemsMap.MainDetailsPanel;
import app.navigps.gui.displayItemsMap.PanelWithBatikJTree;
import app.navigps.gui.label.ui.TitleLabelUI;
import app.navigps.gui.searchServices.SearchServices;
import app.navigps.gui.svgComponents.Canvas;
import app.navigps.gui.svgComponents.SVGBridgeComponents;
import app.navigps.gui.svgComponents.SVGBridgeListeners;
import app.navigps.gui.svgComponents.SVGCanvasLayers;
import app.navigps.gui.svgComponents.SVGLayerScrollPane;
import app.navigps.gui.svgComponents.UpdateComponentsAdapter;
import app.navigps.utils.BridgeForVerboseMode;
import app.navigps.utils.MyFileFilter;
import app.navigps.utils.NaviLogger;
import app.navigps.utils.OutputVerboseStream;
import app.navigps.utils.Utils;
import app.config.GUIConfiguration;
import app.config.MainConfiguration;
import app.database.odb.gui.DatabaseManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
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
import app.database.odb.utils.ODBConnection;
import app.navigps.WindowInitialEvent;
import app.navigps.WindowInitialListener;
import app.navigps.gui.ToolBar.NaviToolBarPanel;
import app.navigps.gui.ToolBar.NaviToolBar;
import app.navigps.gui.buttons.NewToolbarButton;
import app.navigps.gui.buttons.NewToolbarToggleButton;
import app.navigps.gui.repaintmanager.AlphaRepaintManager;
import java.awt.FlowLayout;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.swing.RepaintManager;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.swing.svg.AbstractJSVGComponent;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

/**
 *
 * @author vara
 */
public class NaviRootWindow extends JFrame implements WindowFocusListener, ItemListener, WindowStateListener {

    private NaviGPSCore core;
    private NaviToolBarPanel panelWithToolBars;
    private StatusPanel statusPanel;
    private StatusInfoPanel statusInfoPanel = new StatusInfoPanel();
    private Action openSVGFileAction,  zoomOutAction,  zoomInAction,  zoomAction,  fitToPanelAction,  searchServicesAction;    
    private JCheckBoxMenuItem[] cbmOptionsForToolBars;
    private JToolBar toolBarFile,  toolBarZoom,  toolBarSerch,  toolBarMemMonitor;        
    private RootWindow rootWindow;
    private ViewMap viewMap = new ViewMap();//key -int i object -View
    private DockingWindowsTheme currentTheme = new ShapedGradientDockingTheme();
    private RootWindowProperties properties = new RootWindowProperties();
    private Vector<View> views = new Vector<View>();

    private static SVGCanvasLayers canvaslayers;
    private static SVGBridgeListeners svgListeners = new SVGBridgeListeners();

    private static LinkedList <WindowInitialListener> windowInitialListeners =
                                            new LinkedList<WindowInitialListener>();

    private static int ICON_SIZE = 8;
    public static final ImageIcon LOGO_APPICATION_IMAGE = NaviRootWindow.createNavigationIcon("logo/LogoNaviGPS64x64");

    //private MySplitPane paneForProperties = new MySplitPane();    
    /**
     *
     * @param c
     */
    public NaviRootWindow(NaviGPSCore c) {
        super(GUIConfiguration.getGraphicDevice().getDefaultConfiguration());

        final WindowInitialEvent we = new WindowInitialEvent(this, System.nanoTime());
        for (WindowInitialListener wi : windowInitialListeners) {
            wi.initialPrepare(we);
        }

        NaviLogger.logger.log(Level.FINE, "Initialize components ...");

        addWindowFocusListener(this);
        
        core = c;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        if (LOGO_APPICATION_IMAGE != null) {
            setIconImage(LOGO_APPICATION_IMAGE.getImage());
        }
        NaviLogger.logger.log(Level.FINE, "Initialize gui window completed");
    //setDisplayMode();

        final WindowInitialEvent wec = new WindowInitialEvent(this, System.nanoTime());
        for (WindowInitialListener wi : windowInitialListeners) {
            wi.initialCompleted(wec);
        }


        RepaintManager rpm = RepaintManager.currentManager(this);
        if(!(rpm instanceof AlphaRepaintManager)){
            System.err.println("Install "+AlphaRepaintManager.class.getName());
            AlphaRepaintManager manager = new AlphaRepaintManager();
            RepaintManager.setCurrentManager(manager);
        }
        //setLocationRelativeTo(null);
    }

    public static void addWindowinitialListener(WindowInitialListener wil){
        windowInitialListeners.add(wil);
    }
    
    public static void removeWindowinitialListener(WindowInitialListener wil){
        windowInitialListeners.remove(wil);
    }

    public static final SVGBridgeComponents getBridgeInformationPipe() {
        return svgListeners;
    }    

    public void initComponents() {

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
                createNavigationIcon("searchServices16"),
                "Search services with a certain area",
                KeyEvent.VK_S);

        svgListeners.addUpdateComponentslisteners(new UpdateMenuAndToolBars());

        //init docking RootWindow and create canvas with scrollBars
        createRootWindow();
        setDefaultLayout();
        getContentPane().add(rootWindow, BorderLayout.CENTER);        
        creatToolBars();	//must be before creatMenuBar()

        creatMenuBar();
        createStatusPanel();	//must be before createCanvasPanel()

        getSVGCanvas().addSVGDocumentLoaderListener(svgListeners);
        getSVGCanvas().addGVTTreeBuilderListener(svgListeners);
        getSVGCanvas().addGVTTreeRendererListener(svgListeners);

        svgListeners.addStatusChangedlistener(statusInfoPanel);
    }

    public OutputVerboseStream getVerboseStream() {
        return BridgeForVerboseMode.getInstance();
    }

    private void setDefaultLayout() {
        if (views.size() > 1) {
            View[] viewsTabDockingWindow = new View[views.size()];
            for (int i = 0; i < viewsTabDockingWindow.length; i++) {
                viewsTabDockingWindow[i] = views.get(i);
            }
            TabWindow tabWindow = new TabWindow(viewsTabDockingWindow);
            int minus = 0;
            if (MainConfiguration.isModeVerboseGui()) {
                minus = 2;//first and last window (CanvasWindow and DebugWindow)

            } else {
                minus = 1; //only first window

            }
            View[] splitTab = new View[views.size() - minus];
            //only windows on left side
            for (int i = 0; i < viewsTabDockingWindow.length - minus; i++) {
                splitTab[i] = viewsTabDockingWindow[i + 1];
            }
            SplitWindow splitWindow = new SplitWindow(true, 0.2f, new TabWindow(splitTab), views.lastElement());
            rootWindow.setWindow(new SplitWindow(true, 0.2f, splitWindow, tabWindow));
            if (MainConfiguration.isModeVerboseGui()) {
                WindowBar windowBar = rootWindow.getWindowBar(Direction.DOWN);
                windowBar.addTab(views.lastElement());
            }
        }

    }

    private void createRootWindow() {

        //main window should always be first
        View vRoot = new View("SVG Document", VIEW_ICON, createCanvasWithScrollPane());
        freezeLayout(true, vRoot.getWindowProperties());
        views.add(vRoot);
        if (MainConfiguration.isShowDocumentProperties()) {

            MainDetailsPanel detailsPanel = new MainDetailsPanel();
            DetailsPanel displayDetails = new DetailsPanel();
            PanelWithBatikJTree panelJTree = new PanelWithBatikJTree(displayDetails);
            getSVGCanvas().addSVGDocumentLoaderListener(panelJTree.getGVTTreeListener());
            getSVGCanvas().addGVTTreeBuilderListener(panelJTree.getGVTTreeListener());
            detailsPanel.addToUpperContent(panelJTree);
            detailsPanel.addToLowerContent(displayDetails);
            views.add(new View("Properties", VIEW_ICON, detailsPanel));
        }
        if (MainConfiguration.isModeVerboseGui()) {
            PanelForVerboseWindow panel =
                    new PanelForVerboseWindow((BridgeForVerboseMode) getVerboseStream());

            View vv = new View("Result return by functions", VIEW_ICON, panel);
            vv.getWindowProperties().setRestoreEnabled(false);
            JButtonActionForDebugWindow button = new JButtonActionForDebugWindow(BUTTON_ICON);
            vv.getCustomTabComponents().add(button);
            views.add(vv);
        }
        for (int i = 0; i < views.size(); i++) {
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
        rootWindow = DockingUtil.createRootWindow(viewMap, true);
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
        if (MainConfiguration.isModeVerboseGui()) {
            rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
            rootWindow.addTabMouseButtonListener(DockingWindowActionMouseButtonListener.MIDDLE_BUTTON_CLOSE_LISTENER);
        }
    }

    private void freezeLayout(boolean freeze, DockingWindowProperties prop) {
        // Freeze window operations
        prop.setDragEnabled(!freeze);
        prop.setCloseEnabled(!freeze);
        prop.setMinimizeEnabled(!freeze);
        prop.setRestoreEnabled(!freeze);
        prop.setMaximizeEnabled(!freeze);
        prop.setUndockEnabled(!freeze);
        prop.setDockEnabled(!freeze);
    }

    /**
     *
     * @param imageName
     * @return
     */
    public static ImageIcon createNavigationIcon(String imageName) {
        return createNavigationIcon(imageName, "png");
    }

    /**
     *
     * @param imageName
     * @param ext
     * @return
     */
    public static ImageIcon createNavigationIcon(String imageName, String ext) {
        return createNavigationIcon(imageName, ext, true);
    }

    public static ImageIcon createNavigationIcon(String imageName, String ext, boolean errOut) {
        URL imageURL = createNavigationIconPath(imageName, ext, errOut);
        if (imageURL != null) {
            return new ImageIcon(imageURL);
        }
        return null;
    }

    /**
     *
     * @param imageName
     * @param ext
     * @return
     */
    public static URL createNavigationIconPath(String imageName, String ext, boolean errOut) {
        String imgLocation = "resources/graphics/icons/" + imageName + "." + ext;
        URL imageURL = NaviRootWindow.class.getResource(imgLocation);
        if (imageURL == null && errOut) {
            System.err.println("Resource not found: " + imgLocation);            
            return null;
        }
        return imageURL;
    }

    public static URL createNavigationIconPath(String imageName, String ext) {
        return createNavigationIconPath(imageName, ext, true);
    }

    /**
     *
     */
    public void createStatusPanel() {

        statusPanel = new StatusPanel();

        statusInfoPanel.getBumpArea().setVisible(false);
        //statusInfoPanel.setQueueEnabled(true);
        //statusInfoPanel.setMaxEtemInQueue(3);

        statusPanel.addToPanelFromPosition(statusInfoPanel, StatusPanel.LEFT_PANEL);

        CoordinateInfoPanel cip = new CoordinateInfoPanel();
        cip.setBorder(StatusPanel.getDefaultBorder());
        //TitleLabelUI titleUi = (TitleLabelUI) cip.getContentText().getUI();
        //titleUi.setTextLayout(TitleLabelUI.CENTER_VERTICAL | TitleLabelUI.CENTER_HORIZONTAL);
        //titleUi.setHorizontalCalibrated(0);

        getSVGCanvas().addMouseMotionListener(cip);
        statusPanel.addToPanelFromPosition(Box.createHorizontalGlue(), StatusPanel.RIGHT_PANEL);
        statusPanel.addToPanelFromPosition(cip, StatusPanel.RIGHT_PANEL);

        getContentPane().add(getStatusPanel(), BorderLayout.PAGE_END);
    }

    /**
     *
     */
    public void creatToolBars() {

        panelWithToolBars = new NaviToolBarPanel();

        toolBarFile = new NaviToolBar("ToolBar File");
        toolBarZoom = new NaviToolBar("ToolBar Zoom");
        toolBarSerch = new NaviToolBar("ToolBar Serch");
        toolBarMemMonitor = new NaviToolBar("Memory Monitor") {

            @Override
            public void setVisible(boolean b) {
                super.setVisible(b);
                Component[] comp = getComponents();
                for (Component component : comp) {
                    component.setVisible(b);
                }
            }
        };

        toolBarFile.add(new NewToolbarButton(openSVGFileAction,
                createNavigationIcon("open32")));
        toolBarZoom.add(new NewToolbarButton(zoomInAction,
                createNavigationIcon("zoomIn32")));
        toolBarZoom.add(new NewToolbarButton(zoomOutAction,
                createNavigationIcon("zoomOut32")));
        toolBarZoom.add(new NewToolbarToggleButton(zoomAction,
                createNavigationIcon("zoom32")));
        toolBarZoom.add(new NewToolbarButton(fitToPanelAction,
                createNavigationIcon("fitToPanel32")));

        toolBarMemMonitor.setLayout(new FlowLayout(FlowLayout.LEFT,1,1));
        toolBarMemMonitor.add(new MemoryGui());
        toolBarMemMonitor.setMargin(new Insets(0, 0, 0, 0));


        NewToolbarToggleButton tb = new NewToolbarToggleButton(searchServicesAction,
                createNavigationIcon("searchServices32"));
        toolBarSerch.add(tb);

        ((SearchServicesAction) searchServicesAction).addToSelectableGroup(tb);

        panelWithToolBars.add(toolBarMemMonitor);
        panelWithToolBars.add(toolBarSerch);
        panelWithToolBars.add(toolBarZoom);
        panelWithToolBars.add(toolBarFile);

        getContentPane().add(panelWithToolBars, BorderLayout.NORTH);

    }

    /**
     *
     */
    public void creatMenuBar() {

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
                // BUG in GUI DataBaseMnager !!!!
                //remove this check block when praise or (somebody) mobilize to repair
                // null pointer exceptionSSS !
                //
                if(ODBConnection.isConnected()){                    
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            final DatabaseManager odb = new
                                  DatabaseManager(NaviRootWindow.this, false);
                            odb.setVisible(true);
                            /*
                            final JDialog ddd = new JDialog(NaviRootWindow.this,true){

                                @Override
                                protected void dialogInit() {
                                    super.dialogInit();
                                    final JDialog dialog = this;
                                    getRootPane().getActionMap().put("close", new AbstractAction("close") {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            setVisible(false);
                                            getToolkit().getSystemEventQueue().postEvent(
                                                    new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                                        }
                                    });

                                    getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
                                }

                                @Override
                                public void dispose() {
                                    super.dispose();
                                    System.err.println("Dispose Dialog !!!!!!");
                                }

                                @Override
                                protected void finalize() throws Throwable {
                                    super.finalize();
                                    System.err.println("Fianlize Dialog !!!");
                                }


                            };
                            ddd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            ddd.setVisible(true);
                            */
                        }
                    });
                }else{
                    System.err.println("Can't Display the window (shit), no connected to data base !");
                }
            }
        });

        JMenuItem odbDisc = new JMenuItem("Disconnect");
        odbDisc.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = ODBConnection.disconnect();
                System.out.println(msg);
                NaviRootWindow.getBridgeInformationPipe().
                        currentStatusChanged(msg);
            }
        });

        JMenuItem itemZoomIn = new JMenuItem(zoomInAction);
        JMenuItem itemZoomOut = new JMenuItem(zoomOutAction);
        JMenuItem itemfitToPanel = new JMenuItem(fitToPanelAction);
        JCheckBoxMenuItem itemSearchServices = new JCheckBoxMenuItem(searchServicesAction);

        ((SearchServicesAction) searchServicesAction).addToSelectableGroup(itemSearchServices);

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
        menuODB.add(odbDisc);

        jmb.add(menuFile);
        jmb.add(menuView);
        jmb.add(menuODB);
        setJMenuBar(jmb);
    }

    protected SVGCanvasLayers createCanvas() {

        canvaslayers = new SVGCanvasLayers();
        getSVGCanvas().setBackground(Color.white);
        return canvaslayers;
    }

    protected SVGLayerScrollPane createCanvasWithScrollPane() {
        if (canvaslayers == null) {
            return new SVGLayerScrollPane(createCanvas());
        }
        return new SVGLayerScrollPane(canvaslayers);
    }

    private void openFileChoserWindow() {
        JFileChooser chooser = new JFileChooser();
        String fileFilter = "svg";
        chooser.addChoosableFileFilter(new MyFileFilter(new String[]{"svg"}, fileFilter));
        chooser.setAcceptAllFileFilterUsed(false);
        if (getSVGCanvas().isDocumentSet()) {

            try {
                String lastPath = getSVGCanvas().getSVGDocument().getDocumentURI();
                URI uri = new URI(lastPath);
                chooser.setCurrentDirectory(new File(uri));
            } catch (URISyntaxException ex) {
                System.err.println("" + ex);
                NaviLogger.logger.log(Level.FINE, "Open svg file [method openFileChoserWindow()]",ex);
            }
        }
        int retour = chooser.showOpenDialog(this);
        if (retour == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        openSVGDocument(file);
                    } catch (Exception ex) {
                        getVerboseStream().outputVerboseStream(ex.getMessage());
                        NaviLogger.logger.log(Level.FINE, "Open svg file [method openFileChoserWindow()]",ex);
                    }
                }
            });
        }
    }

    /**
     *
     * @param path
     */
    public void openSVGDocument(String path) {

        File file = new File(path);
        if (file.exists()) {
            openSVGDocument(file);
        } else {
            String msg = "File " + path + " doesn't exist !!!";
            System.err.println(msg);
            NaviLogger.logger.log(Level.WARNING, msg);
        }
    }

    /**
     *
     * @param file
     */
    public void openSVGDocument(File file) {

        NaviLogger.logger.log(Level.FINE, "Try opened SVG document "+file.getAbsolutePath());

        if (getSVGCanvas().getSVGDocument() != null) {

            /*
             * Notify all object of closing documnet
             */

            svgListeners.documentClosed();
        }
        getSVGCanvas().setURI(file.toURI().toString());
    }

    protected StatusPanel getStatusPanel() {
        return statusPanel;
    }

    /**
     *
     */
    public void exitApp() {
        //do something before exit app !!!

        System.exit(0);
    }

    //unused function !!!
    private void reloadMainWindow() {
        setVisible(false);
        core.reload();
        core = null;
        dispose();
    }

    /**
     *
     * @param b
     */
    public void setComponetsEnableWhenDocumentLoaded(boolean b) {
        zoomAction.setEnabled(b);
        zoomInAction.setEnabled(b);
        zoomOutAction.setEnabled(b);
        fitToPanelAction.setEnabled(b);
        searchServicesAction.setEnabled(b);
    }

    /*
     * To retrieve the object instances do not use this method.
     * This method is subject to a NullPointerException.
     */
    @Deprecated
    public static SVGCanvasLayers getSVGCanvasLayers() {
        return canvaslayers;
    }

    /*
     * To retrieve the object instances do not use this method.
     * This method is subject to a NullPointerException.
     */
    @Deprecated
    public static Canvas getSVGCanvas() {
        return getSVGCanvasLayers().getSvgCanvas();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBoxMenuItem mi = (JCheckBoxMenuItem) (e.getSource());

        boolean selected = (e.getStateChange() == ItemEvent.SELECTED);        
        
        //Set the enabled state of the appropriate Action.
        if (mi == cbmOptionsForToolBars[0]) {
            toolBarFile.setVisible(selected);
        } else if (mi == cbmOptionsForToolBars[1]) {
            toolBarZoom.setVisible(selected);
        } else if (mi == cbmOptionsForToolBars[2]) {
            toolBarMemMonitor.setVisible(selected);
        } else if (mi == cbmOptionsForToolBars[3]) {
            toolBarSerch.setVisible(selected);
        } else if (mi == cbmOptionsForToolBars[4]) {
            panelWithToolBars.setVisible(selected);
            //stop working memory monitor when 'selected' == false
            Component [] comps = toolBarMemMonitor.getComponents();
            for (Component c : comps) {
                if(c instanceof MemoryGui){
                    ((MemoryGui)c).setWork(selected);
                }
            }
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

    @Override
    public void windowStateChanged(WindowEvent e) {
        getVerboseStream().outputVerboseStream("windowStateChanged");
    }

    private class OpenSVGFileAction extends AbstractAction {

        public OpenSVGFileAction(String text, ImageIcon icon,
                String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
            putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(mnemonic, InputEvent.ALT_DOWN_MASK));
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
                    KeyStroke.getKeyStroke(mnemonic, InputEvent.ALT_DOWN_MASK));
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getSVGCanvas().zoomFromCenterDocumnet(true);
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
                    KeyStroke.getKeyStroke(mnemonic, InputEvent.ALT_DOWN_MASK));
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getSVGCanvas().zoomFromCenterDocumnet(false);
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
                    KeyStroke.getKeyStroke(mnemonic, InputEvent.ALT_DOWN_MASK));
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AbstractButton button = (AbstractButton) e.getSource();
            boolean selected = button.isSelected();
            getSVGCanvas().zoomFromMouseCoordinationEnable(selected);
            getVerboseStream().outputVerboseStream("Zoom from mouse position " + (selected ? "Disabled" : "Enabled"));
        }
    }

    private class FitToPanelAction extends AbstractAction {

        public FitToPanelAction(String text, ImageIcon icon,
                String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
            putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(mnemonic, InputEvent.ALT_DOWN_MASK));
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getSVGCanvas().resetRenderingTransform();
            getVerboseStream().outputVerboseStream("Fit to panel");
        }
    }

    private class JButtonActionForDebugWindow extends JButton implements ActionListener {

        public JButtonActionForDebugWindow(Icon ico) {
            super(ico);
            addActionListener(this);
            setOpaque(false);
            setBorder(null);
            setFocusable(false);
            setToolTipText("get document transforms");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            SVGDocument doc = getSVGCanvas().getSVGDocument();
            if (doc != null) {
                SVGSVGElement el = doc.getRootElement();
                String viewBoxStr = el.getAttributeNS(null, SVGConstants.SVG_VIEW_BOX_ATTRIBUTE);
                float[] rect = ViewBox.parseViewBoxAttribute(el, viewBoxStr, null);
                Rectangle2D rect2d = new Rectangle2D.Float(rect[0], rect[1], rect[2], rect[3]);
                GraphicsNode gn = getSVGCanvas().getGraphicsNode();
                Rectangle2D bounds = gn.getBounds();

                CanvasGraphicsNode cgn = getSVGCanvas().getCanvasGraphicsNode();
                String msg = "-------- Transforms SVG Document --------\n" +
                        "\t" + getClass().getName() + "\n" +
                        "ViewingTransform\t" + getSVGCanvas().getViewingTransform() + "\n" +
                        "ViewBoxTransform\t" + getSVGCanvas().getViewBoxTransform() + "\n" +
                        "RenderingTransform\t" + getSVGCanvas().getRenderingTransform() + "\n" +
                        "PaintingTransform\t" + getSVGCanvas().getPaintingTransform() + "\n" +
                        "InitialTransform\t" + getSVGCanvas().getInitialTransform() + "\n" +
                        "Visible Rect\t" + getSVGCanvas().getVisibleRect() + "\n" +
                        "ViewBox Rect\t" + rect2d + "\t" + bounds + "\n" +
                        "-------- Transform Graphics Nood -------" + "\n" +
                        "PositionTransform\t" + cgn.getPositionTransform() + "\n" +
                        "ViewingTransform\t" + cgn.getViewingTransform() + "\n" +
                        "GlobalTransform\t" + cgn.getGlobalTransform() + "\n" +
                        "InverseTransform\t" + cgn.getInverseTransform() + "\n" +
                        "Transform\t" + cgn.getTransform() + "\n" +
                        "----------------------------------------";
                getVerboseStream().outputVerboseStream(msg);
            }
        }
    }

    private class UpdateMenuAndToolBars extends UpdateComponentsAdapter {

        @Override
        public void documentPrepareToModification() {

            Thread w = new Thread(new Runnable() {
                @Override
                public void run() {
                    setComponetsEnableWhenDocumentLoaded(true);
                    views.firstElement().getViewProperties().setTitle(svgListeners.getAbsoluteFilePath());                    
                }
            });
            w.start();
        }

        @Override
        public void documentClosed() {
            setComponetsEnableWhenDocumentLoaded(false);
            String msg = "Document closed.\n"+ODBConnection.disconnect();
            System.out.println(msg);
            NaviRootWindow.getBridgeInformationPipe().currentStatusChanged(msg);
            NaviLogger.logger.log(Level.FINE,msg);
        }

        @Override
        public void documentLoadingCompleted() {
            SVGCanvasLayers svgLay = getSVGCanvasLayers();
            if(svgLay != null){
                svgLay.updateSynchronizedLayers();
            }
        }
    }

    /**
     *
     */
    public abstract class ActionNotifyALLCompGroup extends AbstractAction {

        private Vector<AbstractButton> vecToggle = new Vector<AbstractButton>();

        /**
         *
         * @param text
         * @param icon
         */
        public ActionNotifyALLCompGroup(String text, ImageIcon icon) {
            super(text, icon);
            addPropertyChangeListener(new Listener());
        }

        /**
         *
         * @param ab
         */
        public void addToSelectableGroup(AbstractButton ab) {
            vecToggle.add(ab);
        }

        private class Listener implements PropertyChangeListener {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(AbstractAction.SELECTED_KEY)) {
                    AbstractAction tmp = (AbstractAction) evt.getSource();
                    for (AbstractButton abstractButton : vecToggle) {
                        if (!abstractButton.getAction().equals(tmp)) {
                            //System.out.println(""+abstractButton);
                            abstractButton.setSelected((Boolean) evt.getNewValue());
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
                    KeyStroke.getKeyStroke(mnemonic, InputEvent.ALT_DOWN_MASK));
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            AbstractButton button = (AbstractButton) e.getSource();
            boolean en = button.getModel().isSelected();
            putValue(AbstractAction.SELECTED_KEY, new Boolean(en));
            SearchServices ss = getSVGCanvas().getSearchServices();
            ss.setEnabledSearchServices(en);
            if (en) {
                int count = canvaslayers.getComponentCountInLayer(SVGCanvasLayers.SEARCH_SERVICES_LAYER);
                System.err.println("component count on SVGCanvasLayers.SEARCH_SERVICES_LAYER " + count);
                
            }
            String info = "Search services " + (en ? "enabled" : "disabled");
            svgListeners.currentStatusChanged(info);
        }
    }

    public void setDisplayMode() {
        final GraphicsDevice device = GUIConfiguration.getGraphicDevice();
        synchronized (NaviRootWindow.this) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            System.err.println(new Date() + "\n\t Change Display mode !!! ");
                            if (GUIConfiguration.getModeScreen() == GUIConfiguration.FULL_SCREEN) {
                                getVerboseStream().outputVerboseStream("full screen mode Supported " + device.isFullScreenSupported());
                                setVisible(false);
                                dispose();
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException ex) {
                                }
                                setUndecorated(true);
                                device.setFullScreenWindow(NaviRootWindow.this);
                            } else {
                                if (isVisible()) {
                                    System.out.println("Window invisible");
                                    setVisible(false);
                                }
                                device.setFullScreenWindow(null);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException ex) {
                                }
                                if (isUndecorated()) {
                                    System.out.println("Window must dispose for decorate Frame");
                                    dispose();
                                    System.out.println("Window decorate");
                                    setUndecorated(false);
                                }
                                if (!isVisible()) {
                                    System.out.println("Show Window");
                                    setVisible(true);
                                }
                            }
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    /**
     *
     */
    public void switchDisplayMode() {
        GUIConfiguration.setModeScreen((byte) (0x01 & ~GUIConfiguration.getModeScreen()));
        setDisplayMode();
    }

    private class DisplayModeAction extends AbstractAction {

        public DisplayModeAction(String text, ImageIcon icon,
                String desc, Integer mnemonic) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
            putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(mnemonic, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switchDisplayMode();
            String info = "User set " +
                    (GUIConfiguration.getModeScreen() == GUIConfiguration.FULL_SCREEN ? "full screen mode" : "frame mode");
            svgListeners.currentStatusChanged(info);
            System.out.println(info);
        }
    }

    private class StatusInfoPanel extends DefaultAlphaLabelPanel
            implements StatusChangedListener,Runnable{

        private final LinkedList<String> stringQueue = new LinkedList<String>();
        private int maxEtemInQueue = 2;
        private boolean queueEnabled = false;

        public StatusInfoPanel() {
            setAnimatorEnabled(true);
        }

        @Override
        public void currentStatusChanged(String str) {
            if( (!closeTimerIsRunning() && !animatorIsRunning() ) || !isQueueEnabled()){
                setText(str);
            }else{                
                //synchronized(stringQueue){
                    if(getCloseDelay() == DEFAULT_CLOSE_DELAY)
                        setCloseDelay(100);
                    if(stringQueue.size()>getMaxEtemInQueue())
                        stringQueue.removeFirst();                    
                    stringQueue.add(str);
                //}                
            }
        }

        @Override
        public void animationFinished() {
            //dont removed this check block ! NoSuchElementException !
            if(!stringQueue.isEmpty()){
                new Thread(this).start();
            }
        }

        @Override
        public void run() {
            try{
                //Display last element with default deleay option
                if(stringQueue.size()==1){
                    setCloseDelay(DEFAULT_CLOSE_DELAY);
                }
                String str = stringQueue.getFirst();
                stringQueue.removeFirst();                
                setText(str);                
            }catch(NoSuchElementException ex){}
        }

        /**
         * @return the queueEnabled
         */
        public boolean isQueueEnabled() {
            return queueEnabled;
        }

        /**
         * @param queueEnabled the queueEnabled to set
         */
        public void setQueueEnabled(boolean queueEnabled) {
            this.queueEnabled = queueEnabled;
        }

        /**
         * @return the maxEtemInQueue
         */
        public int getMaxEtemInQueue() {
            return maxEtemInQueue;
        }

        /**
         * @param maxEtemInQueue the maxEtemInQueue to set
         */
        public void setMaxEtemInQueue(int maxEtemInQueue) {
            this.maxEtemInQueue = maxEtemInQueue;
        }
    }

    private class CoordinateInfoPanel extends DefaultAlphaLabelPanel
            implements MouseMotionListener {

        public static final byte COMPONENT_POSITON = 0;
        public static final byte SCREEN_POSITION = 1;
        public static final byte ROOT_SVGELEMENT_POSITION = 2;
        private static final String SUFFIX = "position";
        private static final String S_COMPONENT_POSITON = "Component";
        private static final String S_SCREEN_POSITION = "Screen";
        private static final String S_ROOT_SVGELEMENT_POSITION = "SVG ROOT";
        private byte displayPosition;
        private JLabel xLabel = new JLabel();
        private JLabel yLabel = new JLabel();

        public CoordinateInfoPanel() {
            setAnimatorEnabled(false);
            Container content = getContent();
            content.removeAll();
            setDisplayPosition(ROOT_SVGELEMENT_POSITION);
            content.setLayout(new GridLayout(1, 2, 1, 1));
            content.add(xLabel);
            content.add(yLabel);

            xLabel.setToolTipText("x coordinate");
            yLabel.setToolTipText("y coordinate");

            xLabel.setUI(new TitleLabelUI(TitleLabelUI.LEFT));
            yLabel.setUI(new TitleLabelUI(TitleLabelUI.LEFT));

            getBumpArea().setBorder(new EmptyBorder(0, 0, 0, 4));
            getBumpArea().addMouseListener(new BumpAreaListener());
        }

        public byte[] getTabPosition() {
            return new byte[]{COMPONENT_POSITON, SCREEN_POSITION, ROOT_SVGELEMENT_POSITION};
        }

        public String getStringNamePosition(byte position) {
            switch (position) {
                case COMPONENT_POSITON:
                    return S_COMPONENT_POSITON + " " + SUFFIX;
                case SCREEN_POSITION:
                    return S_SCREEN_POSITION + " " + SUFFIX;
                case ROOT_SVGELEMENT_POSITION:
                    return S_ROOT_SVGELEMENT_POSITION + " " + SUFFIX;
            }
            return "";
        }
        // this function ;}}}} (i dont have time !)

        public byte getNumPosition(String str) {
            byte[] tabPos = getTabPosition();
            for (int i = 0; i < tabPos.length; i++) {
                if (str.equals(getStringNamePosition(tabPos[i]))) {
                    return tabPos[i];
                }
            }
            return COMPONENT_POSITON; //default ;}

        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof AbstractJSVGComponent) {
                SVGDocument doc = ((AbstractJSVGComponent) source).getSVGDocument();
                if (doc != null && !svgListeners.isRendering()) {
                    switch (displayPosition) {
                        case COMPONENT_POSITON:
                            setTextCoordinate(e.getX() + "", "" + e.getY());
                            break;
                        case SCREEN_POSITION:
                            setTextCoordinate(e.getXOnScreen() + "", "" + e.getYOnScreen());
                            break;
                        case ROOT_SVGELEMENT_POSITION:
                            SVGOMPoint svgp =
                                    Utils.getLocalPointFromDomElement(
                                    doc.getRootElement(), e.getX(), e.getY());
                            setTextCoordinate(svgp.getX() + "", "" + svgp.getY());
                            break;
                    }
                }
            }
        }

        private void setTextCoordinate(String x, String y) {
            xLabel.setText(x);
            yLabel.setText(y);
        }

        /**
         * @return the displayPosition
         */
        public byte getDisplayPosition() {
            return displayPosition;
        }

        /**
         * @param displayPosition the displayPosition to set
         */
        public void setDisplayPosition(byte displayPosition) {
            this.displayPosition = displayPosition;
            String strTooltip = getStringNamePosition(displayPosition);
            setToolTipText(strTooltip);
            getBumpArea().setToolTipText(strTooltip);
        }

        private class BumpAreaListener extends MouseInputAdapter implements ActionListener {

            @Override
            public void mousePressed(MouseEvent e) {
                byte[] tabPos = getTabPosition();
                MyPopupMenu popup = new MyPopupMenu();
                for (int i = 0; i < tabPos.length; i++) {
                    JMenuItem mi = new JMenuItem(getStringNamePosition(tabPos[i]));
                    mi.addActionListener(this);
                    popup.add(mi);
                }
                popup.show(e.getComponent(), e.getX(), e.getY());
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem mi = (JMenuItem) e.getSource();
                byte pos = getNumPosition(mi.getText());
                setDisplayPosition(pos);
            }
        }
    }
}
