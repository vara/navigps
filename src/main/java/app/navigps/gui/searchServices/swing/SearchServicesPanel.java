/*
 * SearchServicesPanel.java
 *
 * Created on 2008-12-10, 03:21:56
 */
package app.navigps.gui.searchServices.swing;


import app.navigps.gui.NaviRootWindow;
import app.navigps.gui.MyPopupMenu;
import app.navigps.gui.Scrollbar.ui.LineScrollBarUI;
import app.navigps.gui.svgComponents.DisplayObjects.AbstractDisplayManager;
import app.navigps.utils.InvokeUtils;
import app.navigps.utils.InvokeUtils.ReturnValue;
import app.navigps.utils.NaviPoint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree.DynamicUtilTreeNode;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import app.database.odb.core.Category;
import app.database.odb.core.Search;
import app.database.odb.core.ServiceCore;
import app.database.odb.utils.ConnectionListener;
import app.database.odb.utils.Constants;
import app.database.odb.utils.ODBConnection;
import app.navigps.gui.detailspanel.AlphaJPanel;
import app.navigps.gui.detailspanel.LoacationManager.LeftLocation;
import app.navigps.gui.detailspanel.RoundJPanel;
import app.navigps.gui.detailspanel.RoundWindow;
import app.navigps.gui.svgComponents.SVGCanvasLayers;
import app.navigps.utils.BridgeForVerboseMode;
import app.navigps.utils.NaviLogger;
import app.navigps.utils.NaviUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerListener;
import java.util.logging.Level;
import org.neodatis.odb.ODB;
import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectRepresentation;
import org.neodatis.odb.core.trigger.DeleteTrigger;
import org.neodatis.odb.core.trigger.InsertTrigger;
import org.neodatis.odb.core.trigger.UpdateTrigger;

/**
 *
 * @author vara
 */
public class SearchServicesPanel extends javax.swing.JPanel{

    private JScrollPane jScrollPane1;
    private JCheckBoxTree jCheckBox;
    
    //for test !!!
    private RoundWindow rWindow;
    private ServicesInfoDisplayedList servlistPanel = new ServicesInfoDisplayedList();

    private boolean triggers;

    /** Creates new form SearchServicesPanel */
    public SearchServicesPanel() {        
        initComponents();
        initValue();
        initListenersDB();
        Vector<String> val = new Vector<String>();

        jCheckBox = new JCheckBoxTree();
        jCheckBox.setRootVisible(false);
        jCheckBox.setShowsRootHandles(true);
        setServices(val);

        jScrollPane1 = new JScrollPane(jCheckBox);
        jScrollPane1.setOpaque(false);
        jScrollPane1.setBorder(null);
        jScrollPane1.getViewport().setOpaque(false);
        jScrollPane1.getViewport().setBorder(null);
        jCheckBox.addMouseListener(new MouseEventReloadJTree());

        JScrollBar scbH = jScrollPane1.getHorizontalScrollBar();
        JScrollBar scbV = jScrollPane1.getVerticalScrollBar();
        scbH.setOpaque(false);
        scbV.setOpaque(false);
        scbV.setUI(new LineScrollBarUI());
        scbH.setUI(new LineScrollBarUI());

        scbH.removeAll();
        scbV.removeAll();

        jCheckBox.setOpaque(false);
        jCheckBox.setFocusable(false);

        panelForJTree.add(jScrollPane1);       
    }

    protected void initListenersDB(){
        ODBConnection.getInstance().
                addConnectionListener(new ODBConnectionListener());
    }

    public void installTriggers(){
        System.err.println(getClass().getName()+" triggers was installed: "+triggers);
        if(!triggers){
            addTriggersToDB();
        }
    }

    protected boolean addTriggersToDB(){

        ODB odb = Constants.getDbConnection();
        if(odb != null ){
            odb.addInsertTrigger(Category.class,new MyInsertTrigger());
            
            odb.addUpdateTrigger(Category.class, new MyUpdateTriger());
            odb.addUpdateTrigger(ServiceCore.class,new MyUpdateTriger());
            odb.addDeleteTrigger(Category.class, new MyDeleteTriger());
            return (triggers = true);
        }
        return (triggers = false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        gRadius = new javax.swing.JFormattedTextField();
        gCurrentX = new javax.swing.JFormattedTextField();
        gCurrentY = new javax.swing.JFormattedTextField();
        gCenterX = new javax.swing.JFormattedTextField();
        gCenterY = new javax.swing.JFormattedTextField();
        jButtonSearch = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        panelForJTree = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();

        setDoubleBuffered(false);
        setFocusable(false);
        setOpaque(false);
        setRequestFocusEnabled(false);
        setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setOpaque(false);

        jPanel4.setOpaque(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 219, 255)));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setForeground(new java.awt.Color(238, 239, 239));
        jLabel3.setText("x");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, -1, -1));

        jLabel4.setForeground(new java.awt.Color(238, 239, 239));
        jLabel4.setText("y");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, -1, -1));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 150, -1));

        jLabel5.setForeground(new java.awt.Color(238, 239, 239));
        jLabel5.setText("Current Point");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, -1, -1));

        jLabel2.setForeground(new java.awt.Color(238, 239, 239));
        jLabel2.setText("Center Point");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, -1, -1));

        jLabel1.setForeground(new java.awt.Color(238, 239, 239));
        jLabel1.setText("Radius");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        gRadius.setCaretColor(new java.awt.Color(151, 151, 151));
        gRadius.setOpaque(false);
        jPanel1.add(gRadius, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 170, -1));

        gCurrentX.setCaretColor(new java.awt.Color(151, 151, 151));
        gCurrentX.setOpaque(false);
        jPanel1.add(gCurrentX, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 80, -1));

        gCurrentY.setCaretColor(new java.awt.Color(151, 151, 151));
        gCurrentY.setOpaque(false);
        jPanel1.add(gCurrentY, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 80, -1));

        gCenterX.setCaretColor(new java.awt.Color(151, 151, 151));
        gCenterX.setOpaque(false);
        jPanel1.add(gCenterX, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 80, -1));

        gCenterY.setCaretColor(new java.awt.Color(151, 151, 151));
        gCenterY.setOpaque(false);
        jPanel1.add(gCenterY, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 80, -1));

        jButtonSearch.setText("Search");
        jButtonSearch.setFocusPainted(false);
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jPanel7.setOpaque(false);

        jLabel7.setText("Parameters");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel7)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setOpaque(false);

        panelForJTree.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 219, 255)));
        panelForJTree.setAlignmentX(0.0F);
        panelForJTree.setAlignmentY(0.0F);
        panelForJTree.setFocusable(false);
        panelForJTree.setOpaque(false);
        panelForJTree.setLayout(new java.awt.BorderLayout());

        jPanel6.setOpaque(false);

        jLabel6.setText("Services");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelForJTree, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelForJTree, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setOpaque(false);

        jRadioButton1.setFont(new java.awt.Font("Dialog", 1, 10));
        jRadioButton1.setForeground(new java.awt.Color(238, 239, 239));
        jRadioButton1.setText("Remove Last search");
        jRadioButton1.setContentAreaFilled(false);
        jRadioButton1.setFocusPainted(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jRadioButton1)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jRadioButton1))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel2);
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param al
     */
    public void addActionForSearchButton(ActionListener al) {
        jButtonSearch.addActionListener(al);
    }

    private AbstractDisplayManager getDisplayManager(){
        SVGCanvasLayers svgCanvaasLayers = NaviUtilities.getSVGCanvasLayers(getParent());
        if(svgCanvaasLayers != null){
            return svgCanvaasLayers.getSvgCanvas().getDisplayManager();
        }else{
            String msg = "Can't find display manager ! ";
            System.err.println(msg);
            NaviLogger.logger.log(Level.WARNING, msg);
        }
        return null;
    }

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed

        if(rWindow == null){
            createRoundWindowForServicesList();            
        }
        
        final Vector<String> services = getSelectedServices();
        if (!services.isEmpty()) {
            final double radius = ((Number) gRadius.getValue()).doubleValue();
            final double cx = ((Number) gCenterX.getValue()).doubleValue();
            final double cy = ((Number) gCenterY.getValue()).doubleValue();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    
                    Object obj[] = {services, cx, cy, radius};
                    Class[] pt = {Vector.class, double.class, double.class, double.class};
                    //test reflection
                    ReturnValue rv = InvokeUtils.invokeWithTime(
                            new Search(), "searchCategoryRadius", pt, obj);
                    if(rv.isMessage()){                        
                        rv.getMessage().printStackTrace
                                (BridgeForVerboseMode.getInstance().getErrOutputStream());
                        return;
                    }
                    
                    Vector subResult = (Vector) rv.getRet();
                    
                    AbstractDisplayManager dm = getDisplayManager();

                    if(dm == null) return;

                    if(jRadioButton1.isSelected()){
                        dm.removeLastServices();
                        servlistPanel.getListModel().removeAll();
                    }

                    Vector retVal = dm.createObject(subResult);
                    dm.putObject(retVal);

                    servlistPanel.getListModel().addServices(subResult);
                    
                    if(!retVal.isEmpty()){
                        if(!rWindow.isEnabled()){
                            rWindow.updatePosition();
                            rWindow.setEnabled(true);
                        }
                    }else{
                        if(rWindow.isEnabled() && servlistPanel.getListModel().isEmpty()){
                            rWindow.setEnabled(false);
                            //servlistPanel.getAnimamtionLayer().dispose();
                        }
                    }
                    String summaryMsg = "found "+subResult.size() + " services";
                    System.out.println("Query finished, execution time: " + (rv.getTimeNano()/1000000) + " mili sec " + summaryMsg);
                    NaviRootWindow.getBridgeInformationPipe().currentStatusChanged(summaryMsg);

                }
            }).start();

        } else {
            String msg= "";
            if(Constants.getDbConnection() != null){
                msg = "Please select a service category!";
            }else{
                msg = "No database initialized";
            }
            NaviRootWindow.getBridgeInformationPipe().currentStatusChanged(msg);
        }
}//GEN-LAST:event_jButtonSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField gCenterX;
    private javax.swing.JFormattedTextField gCenterY;
    private javax.swing.JFormattedTextField gCurrentX;
    private javax.swing.JFormattedTextField gCurrentY;
    private javax.swing.JFormattedTextField gRadius;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel panelForJTree;
    // End of variables declaration//GEN-END:variables

    private void initValue() {
        setRadius(0);
        setCenterPoint(new NaviPoint(0, 0));
        setCurrentPos(new NaviPoint(0, 0));
    }

    /**
     *
     */
    public void reloadCategory(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setServices(new Search().getCategories());
                NaviRootWindow.getBridgeInformationPipe().
                        currentStatusChanged("Category Tree reloaded");
            }
        });
    }

    /**
     *
     * @param val
     */
    public void setRadius(double val) {
        gRadius.setValue(new Double(val));
    }

    /**
     *
     * @param val
     */
    public void setCenterPoint(NaviPoint val) {
        gCenterX.setValue(new Float(val.getX()));
        gCenterY.setValue(new Float(val.getY()));
    }

    /**
     *
     * @param val
     */
    public void setCurrentPos(NaviPoint val) {
        gCurrentX.setValue(new Float(val.getX()));
        gCurrentY.setValue(new Float(val.getY()));
    }

    /*
     * This method implies that jtree model contais String objects
     */
    /**
     *
     * @return
     */
    public Vector<String> getSelectedServices() {
        Vector<String> val = new Vector<String>(10, 10);
        boolean checked = false;
        boolean anyChecked = false;

        int selRows = jCheckBox.getRowCount();
        for (int i = 0; i < selRows; i++) {
            if (!(checked = ((JCheckBoxTree) jCheckBox).isChecked(jCheckBox.getPathForRow(i)))) {
                anyChecked = ((JCheckBoxTree) jCheckBox).isAnyChildChecked(jCheckBox.getPathForRow(i));
            }
            if (checked || anyChecked) {
                TreeModel model = jCheckBox.getModel();
                Object child = model.getChild(model.getRoot(), i);
                if (child instanceof DefaultMutableTreeNode) {
                    Object data = ((DefaultMutableTreeNode) child).getUserObject();
                    if (data instanceof String) {
                        val.add(((String) data));
                    } else {
                        System.err.println(getClass().getCanonicalName() +
                                " method : getSelectedServices , Child [" + data + "] is not a String object !!! ");
                    }
                }
            }
        }
        return val;
    }

    private void createRoundWindowForServicesList(){
        rWindow = new RoundWindow();
        SVGCanvasLayers svgcl = NaviUtilities.getSVGCanvasLayers(getParent());
        if(svgcl!=null){

            AlphaJPanel container = svgcl.getModalContainer();
            rWindow.setLocationManager(new LeftLocation(rWindow));
            rWindow.setDynamicRevalidate(true);
            rWindow.setTitle("Services List");
            rWindow.setDecorateWindow(true);
            rWindow.setUpperThresholdAlpha(0.75f);
            rWindow.setAlpha(0.0f);
            rWindow.getContentPane().setUpperThresholdAlpha(0.75f);
            rWindow.getContentPane().setLayout(new BorderLayout(10, 10));
            rWindow.getContentPane().add(servlistPanel,BorderLayout.CENTER);
            rWindow.setEnabled(false);
            ((RoundJPanel)rWindow.getContentPane()).
                    setInsets(new Insets(10,10,10,10));
            ((RoundJPanel)rWindow.getContentPane()).setInnerCorners(0,0);

            //JRootPane root = getRootPane();
            //root.setGlassPane(servlistPanel.getAnimamtionLayer());
            svgcl.add(servlistPanel.getAnimamtionLayer(), SVGCanvasLayers.POPUP_LAYER);
            servlistPanel.getAnimamtionLayer().setVisible(true);
            
            svgcl.getServicesContainer().addContainerListener(new ContainerListener() {

                @Override
                public void componentAdded(ContainerEvent e) {
                    System.out.println("add service "+e.getChild());
                }

                @Override
                public void componentRemoved(ContainerEvent e) {
                    System.out.println("remove service "+e.getChild());
                    if(rWindow.isVisible()){
                        Component comp = e.getChild();
                        servlistPanel.getListModel().removeElement(comp);
                    }
                }
            });
            container.add(rWindow);            
            container.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    rWindow.updatePosition();
                }
            });
        }else{
            String msg = "Can't initial round window for search services list,\n"+
                    "SVGCanvasLayers need -> Not Found  !!!";
            System.err.println(getClass().getCanonicalName()+" "+msg);
        }
    }
    /**
     *
     * @param value
     */
    public void setServices(Vector<String> value) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Services");
        DynamicUtilTreeNode.createChildren(root, value);
        DefaultTreeModel dtm = new DefaultTreeModel(root, false);
        jCheckBox.setModel(dtm);
    }

    private class ReloadJTree extends AbstractAction{

        public ReloadJTree(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            reloadCategory();
        }
    }

    private class MouseEventReloadJTree extends MouseInputAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON3){
                MyPopupMenu myPop = new MyPopupMenu();
                JMenuItem mi = new JMenuItem(new ReloadJTree("Reload Services"));
                myPop.add(mi);
                myPop.show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }

    private class MyInsertTrigger extends InsertTrigger {

        @Override
        public boolean beforeInsert(Object object) {
            //System.out.println("before inserting " + object);
            return true;
        }

        @Override
        public void afterInsert(Object object, OID oid) {
            //System.out.println("after insert object with id "+oid+"("+object.getClass().getName()+")");
            //System.out.println("Insert trigger DB");
            reloadCategory();
        }
    }

    private class MyUpdateTriger extends UpdateTrigger{

        @Override
        public boolean beforeUpdate(ObjectRepresentation oldObjectRepresentation, Object newObject, OID oid) {            
            return true;
        }

        @Override
        public void afterUpdate(ObjectRepresentation oldObjectRepresentation, Object newObject, OID oid) {            
            if(newObject instanceof Category){
                reloadCategory();
            }else if(newObject instanceof ServiceCore){                
                AbstractDisplayManager dm = getDisplayManager();
                if(dm != null){
                    ((ServiceCore)newObject).setOID(oid);
                    dm.updateService(newObject);
                }
            }
        }
    }

    private class MyDeleteTriger extends DeleteTrigger{

        @Override
        public boolean beforeDelete(Object object, OID oid) {
            return true;
        }

        @Override
        public void afterDelete(Object object, OID oid) {
            reloadCategory();
        }
    }

    private class ODBConnectionListener implements ConnectionListener{

        @Override
        public void connectionOpened() {
            reloadCategory();
            addTriggersToDB();
        }

        @Override
        public void connectionClosed() {            
        }
    }
}
