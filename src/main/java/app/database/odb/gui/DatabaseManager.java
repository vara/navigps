package app.database.odb.gui;

import app.navigps.gui.NaviRootWindow;
import app.navigps.utils.NaviPoint;
import app.navigps.utils.Utils;
import app.config.DataBaseConfig;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import app.database.odb.core.Category;
import app.database.odb.core.ServiceAttributes;
import app.database.odb.core.ServiceCore;
import app.database.odb.core.ServiceDescription;
import app.database.odb.core.Subcategory;
import app.database.odb.utils.Constants;
import app.navigps.gui.MyPopupMenu;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import org.neodatis.odb.ODB;
import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectRepresentation;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.core.trigger.DeleteTrigger;
import org.neodatis.odb.core.trigger.InsertTrigger;
import org.neodatis.odb.core.trigger.UpdateTrigger;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.w3c.dom.svg.SVGDocument;

/**
 * @author ACME
 */
public class DatabaseManager extends javax.swing.JDialog {
     
    private ODBInsertTrigger insertTrigger = new ODBInsertTrigger();
    private ODBUpdateTrigger updateTrigger = new ODBUpdateTrigger();
    private ODBDeleteTrigger deleteTrigger = new ODBDeleteTrigger();

    private WindowCloseListener winOpenCloseListener = new WindowCloseListener();
    private MouseCoordinateListener mouseCoordListener = new MouseCoordinateListener();

    /** Creates new form Manager
     * @param parent
     * @param modal
     */
    public DatabaseManager(JFrame parent, boolean modal) {
        super(parent, modal);
        setLocationRelativeTo(parent);
        
        addWindowListener(winOpenCloseListener);

        initComponents();
        refreshTreeData();
        initServicesTable();
        initDatabaseTriggers();

        getRootPane().getActionMap().put("closeWindow", new AbstractAction("closeWindow") {

            @Override
            public void actionPerformed(ActionEvent e) {
                //setVisible(false);
                getToolkit().getSystemEventQueue().postEvent(
                        new WindowEvent(DatabaseManager.this, WindowEvent.WINDOW_CLOSING));
            }
        });

        getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "closeWindow");
 
    }

    @Override
    public void dispose() {       
        System.err.println("DatabaseManager dispose !!!");
        insertTrigger = null;
        deleteTrigger = null;
        updateTrigger = null;
        removeAll();
        removeWindowListener(winOpenCloseListener);        
        super.dispose();        
    }

    @Override
    protected void finalize() throws Throwable {
        System.err.println("DatabaseManager Method finalize !!!");
        super.finalize();        
    }

    private void initDatabaseTriggers() {

        ODB odb = null;
        try{
         odb = Constants.getDbConnection();
        }catch(NullPointerException e){
            System.err.println(""+e.getMessage());
            return;
        }
        
        odb.addInsertTrigger(Category.class, insertTrigger);
        odb.addInsertTrigger(Subcategory.class, insertTrigger);
        odb.addInsertTrigger(ServiceCore.class, insertTrigger);

        odb.addUpdateTrigger(Category.class, updateTrigger);
        odb.addUpdateTrigger(Subcategory.class, updateTrigger);
        odb.addUpdateTrigger(ServiceCore.class, updateTrigger);

        odb.addDeleteTrigger(Category.class, deleteTrigger);
        odb.addDeleteTrigger(Subcategory.class, deleteTrigger);
        odb.addDeleteTrigger(ServiceCore.class, deleteTrigger);
    }

    public void initServicesTable() {        

        jTable1.setToolTipText("Double click to edit!");
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //JTABLE selection listener
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                ODB odb = null;
                try{
                    odb = Constants.getDbConnection();
                }catch(NullPointerException ex){
                    System.err.println(""+ex.getMessage());
                    return;
                }

                int selectedRow;
                jTextArea1.setText("");
                
                if (jTable1.getSelectedRow() == -1) {
                    selectedRow = 0;
                } else {
                    selectedRow = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
                }

                System.out.println("Selected row " + selectedRow);
                if (selectedRow != -1) {
                    Float sx = (Float) jTable1.getModel().getValueAt(selectedRow, 3);
                    Float sy = (Float) jTable1.getModel().getValueAt(selectedRow, 4);
                    System.out.println("sx: " + sx + " sy: " + sy);

                    IQuery query = new CriteriaQuery(ServiceAttributes.class, Where.and().add(Where.equal("x", sx)).add(Where.equal("y", sy)));

                    Objects cats = odb.getObjects(query);
                    if (cats != null && !cats.isEmpty()) {
                        ServiceAttributes sa = (ServiceAttributes) cats.getFirst();
                        ServiceDescription sd = sa.getServiceCore().getServiceDescription();

                        String msg = "Attributes:\nService at "+sa+"\n"+sd;
                        jTextArea1.setText(msg);

                    } else {
                        System.out.println("empty selection");
                    }
                } else {
                    System.err.println(this.getClass().getCanonicalName() + " valueChanged -> selected row is -1 !!!");
                }
            }
        });
        refreshTableModel();
    }

    private void invokeServiceEditor(ServiceCore sc) {
        ServiceEditor se = new ServiceEditor(this, false, sc);
        se.setVisible(true);
    }

    public void refreshTableModel() {

        ODB odb = null;
        try{
            odb = Constants.getDbConnection();
        }catch(NullPointerException ex){
            System.err.println(""+ex.getMessage());
            return;
        }

        Vector v = new Vector();
        if (odb != null) {
            Objects services = odb.getObjects(ServiceCore.class);
            Object[] columns = {"Name", "Category", "Subcategory", "X", "Y"};

            DefaultTableModel model = new DefaultTableModel() {

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.setColumnIdentifiers(columns);

            if (!services.isEmpty()) {
                while (services.hasNext()) {
                    v = new Vector();
                    ServiceCore sc = (ServiceCore) services.next();
                    v.add(sc.getServiceDescription().getServiceName());
                    v.add(sc.getServiceDescription().getCategory().getName());
                    if (sc.getServiceDescription().getServiceSubCategory() == null) {
                        v.add("empty");
                    } else {
                        v.add(sc.getServiceDescription().getServiceSubCategory().getName());
                    }
                    v.add(sc.getServiceAttributes().getX());
                    v.add(sc.getServiceAttributes().getY());
                    model.addRow(v);
                }
            } else {
                System.out.println("no services avaliable!");
            }
            jTable1.setModel(model);
        } else {
            System.err.println("DataBase not initialized!");
        }
    }

    private void addNewService() {
        addNewService(null);
    }

    private void addNewService(NaviPoint np) {
        ServiceFactory sf = new ServiceFactory(this, true);
        if (np != null) {
            sf.setCoordinate(np);
        }
        sf.setVisible(true);
    }

    private void removeServiceByDescription(ServiceDescription sd) {

        ODB odb = null;
        try{
            odb = Constants.getDbConnection();
        }catch(NullPointerException ex){
            System.err.println(""+ex.getMessage());
            return;
        }

        ServiceCore sc = sd.getServiceCore();
        ServiceAttributes sa = sc.getServiceAttributes();
        odb.delete(sa);
        odb.delete(sd);
        odb.delete(sc);
    }

    private void removeSubcategory(Category catt, Subcategory sub) {
        ODB odb = null;
        try{
            odb = Constants.getDbConnection();
        }catch(NullPointerException ex){
            System.err.println(""+ex.getMessage());
            return;
        }

        catt.removeSubcategory(sub);
        odb.delete(sub);
        odb.store(catt);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Database manager");
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Avaliable services", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(51, 153, 255))); // NOI18N

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBackground(new java.awt.Color(255, 255, 204));
        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
        );

        jButton3.setFont(new java.awt.Font("Verdana", 0, 11));
        jButton3.setText("New ...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Attributes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(51, 153, 255))); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Services", jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Category tree", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(51, 153, 255))); // NOI18N

        jScrollPane3.setBorder(null);

        jTree1.setFont(new java.awt.Font("Verdana", 0, 11));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setToolTipText("Right-Click for options !");
        jTree1.setRootVisible(false);
        jTree1.setScrollsOnExpand(false);
        jTree1.setShowsRootHandles(true);
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTree1MouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTree1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Categories", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        addNewService();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTree1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseReleased
        showTreePopup(evt);
}//GEN-LAST:event_jTree1MouseReleased

    private void jTree1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MousePressed
        showTreePopup(evt);
}//GEN-LAST:event_jTree1MousePressed

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        showTablePopup(evt);
    }//GEN-LAST:event_jTable1MousePressed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        showTablePopup(evt);
    }//GEN-LAST:event_jTable1MouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables

    private void addCategory(String category) {
        ODB odb = null;
        try{
            odb = Constants.getDbConnection();
        }catch(NullPointerException ex){
            System.err.println(""+ex.getMessage());
            return;
        }
        odb.store(new Category(category));
        odb.commit();
    }

    private void addSubcategory(Category category, String subcategory) {
        ODB odb = null;
        try{
            odb = Constants.getDbConnection();
        }catch(NullPointerException ex){
            System.err.println(""+ex.getMessage());
            return;
        }

        category.addSubcategory(new Subcategory(category, subcategory));
        odb.store(category);
        odb.commit();
    }

    private void showTreePopup(MouseEvent evt) {
        if (evt.isPopupTrigger()) {

            MyPopupMenu treePopup = new MyPopupMenu("Tree popup menu");
            JMenu newMenu = new JMenu("New");
            JMenuItem categoryMenu = new JMenuItem(new TreeCategoryMenuAction("Category"));
            JMenuItem subcategoryMenu = new JMenuItem(new TreeSubcategoryMenuAction("Subcategory"));
            JMenuItem removeMenu = new JMenuItem(new TreeRemoveMenuAction("Remove"));
            JMenuItem editMenu = new JMenuItem(new TreeEditMenuAction("Edit"));

            newMenu.add(categoryMenu);
            newMenu.add(subcategoryMenu);

            treePopup.add(newMenu);
            treePopup.add(new JPopupMenu.Separator());
            treePopup.add(editMenu);
            treePopup.add(removeMenu);
            treePopup.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

    private void showTablePopup(MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            MyPopupMenu tablePopup = new MyPopupMenu();

            JMenuItem tableRemoveMenu = new JMenuItem(new TableRemoveMenuAction("Remove"));
            JMenuItem tableEditMenu = new JMenuItem(new TableEditMenuAction("Edit"));

            tablePopup.add(tableEditMenu);
            tablePopup.add(tableRemoveMenu);
            tablePopup.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

    private void refreshTreeData() {
        ODB odb = null;
        try{
            odb = Constants.getDbConnection();
        }catch(NullPointerException ex){
            System.err.println(""+ex.getMessage());
            return;
        }

        Category category;
        Subcategory subcategory;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

        ImageIcon leafIcon = DatabaseManager.getIcon("22");
        ImageIcon openIcon = DatabaseManager.getIcon("8");
        ImageIcon closedIcon = DatabaseManager.getIcon("5");

        
        Objects categories = odb.getObjects(Category.class);
        if (categories != null) {
            if (categories.isEmpty()) {
                System.out.println("jTree model: no categories present");
                jTree1.setModel(new DefaultTreeModel(root));
                DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
                model.reload();

            } else {
                jTree1.setModel(new DefaultTreeModel(root));
                DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();

                DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
                renderer.setLeafIcon(leafIcon);
                renderer.setClosedIcon(closedIcon);
                renderer.setOpenIcon(openIcon);
                jTree1.setCellRenderer(renderer);

                while (categories.hasNext()) {
                    category = (Category) categories.next();
                    DefaultMutableTreeNode cat = new DefaultMutableTreeNode(category.getName());
                    model.insertNodeInto(cat, root, root.getChildCount());
                    if (category.getSubcategories() != null) {
                        for (int i = 0; i < category.getSubcategories().size(); i++) {
                            subcategory = (Subcategory) category.getSubcategories().get(i);
                            DefaultMutableTreeNode sub = new DefaultMutableTreeNode(subcategory.getName());
                            model.insertNodeInto(sub, cat, cat.getChildCount());
                        }
                    }
                    model.reload();
                }
            }
        } else {
            System.out.println("Categories is null");
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public static ImageIcon getIcon(String name) {
        return getIcon(name, "png");
    }

    /**
     *
     * @param name
     * @param ext
     * @return
     */
    public static ImageIcon getIcon(String name, String ext) {
        String imgLocation = DataBaseConfig.getIconPath() + name + "." + ext;
        URL imageURL = DatabaseManager.class.getResource(imgLocation);
        if (imageURL == null) {
            System.err.println("Resource not found: " + imgLocation);
            return null;
        } else {
            return new ImageIcon(imageURL);
        }
    }

    private class ODBInsertTrigger extends InsertTrigger {

        @Override
        public boolean beforeInsert(Object object) {
            //System.out.println("before inserting " + object);
            return true;
        }

        @Override
        public void afterInsert(Object arg0, OID arg1) {
            System.out.println("after inserting " + arg0 + " OID: " + arg1);
            refreshTreeData();
            refreshTableModel();
        }
    }

    private class ODBDeleteTrigger extends DeleteTrigger {

        @Override
        public boolean beforeDelete(Object arg0, OID arg1) {
            return true;
        }

        @Override
        public void afterDelete(Object arg0, OID arg1) {
            refreshTreeData();
            refreshTableModel();
        }
    }

    private class ODBUpdateTrigger extends UpdateTrigger {
        int counter = 0;
        @Override
        public boolean beforeUpdate(ObjectRepresentation arg0, Object arg1, OID arg2) {
            counter++;
            System.err.println("counter update trigger "+counter);
            return true;
        }

        @Override
        public void afterUpdate(ObjectRepresentation arg0, Object arg1, OID arg2) {
            refreshTreeData();
            refreshTableModel();
        }
    }

    private class MouseCoordinateListener extends MouseInputAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("mouse klicked");
            SVGDocument doc = NaviRootWindow.getSVGCanvas().getSVGDocument();
            if (doc != null) {
                NaviPoint retPoint =
                        Utils.getLocalPointFromDomElement(doc.getRootElement(), e.getX(), e.getY());
                addNewService(retPoint);
            }
        }
    }

    class WindowCloseListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            System.out.println("Window closing");
            NaviRootWindow.getSVGCanvas().removeMouseListener(mouseCoordListener);
            mouseCoordListener = null;
        }

        @Override
        public void windowOpened(WindowEvent e) {
            System.out.println("Window opened");
            NaviRootWindow.getSVGCanvas().addMouseListener(mouseCoordListener);
        }
    }

    /*
     *  Difine all actions
     */

    private class TableRemoveMenuAction extends AbstractAction{
        public TableRemoveMenuAction(String name){
            super(name);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            ODB odb = null;
            try{
                odb = Constants.getDbConnection();
            }catch(NullPointerException ex){
                System.err.println(""+ex.getMessage());
                return;
            }


            if (jTable1.getSelectedRow() != -1) {
                int selection = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
                IQuery query = new CriteriaQuery(ServiceAttributes.class, Where.and().add(Where.equal("x", jTable1.getModel().getValueAt(selection, 3))).add(Where.equal("y", jTable1.getModel().getValueAt(selection, 4))));
                Objects cats = odb.getObjects(query);
                if (cats != null && !cats.isEmpty()) {
                    ServiceAttributes sa = (ServiceAttributes) cats.getFirst();
                    ServiceCore sc = sa.getServiceCore();
                    ServiceDescription sd = sc.getServiceDescription();
                    removeServiceByDescription(sd);
                    odb.commit();
                } else {
                    System.err.println("Service empty!");
                }
            } else {
                JOptionPane.showMessageDialog(DatabaseManager.this, "Nothing selected!", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class TableEditMenuAction extends AbstractAction{
        public TableEditMenuAction(String name){
            super(name);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            ODB odb = null;
            try{
                odb = Constants.getDbConnection();
            }catch(NullPointerException ex){
                System.err.println(""+ex.getMessage());
                return;
            }

            if (jTable1.getSelectedRow() != -1) {
                int selection = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
                IQuery query = new CriteriaQuery(ServiceAttributes.class, Where.and().add(Where.equal("x", jTable1.getModel().getValueAt(selection, 3))).add(Where.equal("y", jTable1.getModel().getValueAt(selection, 4))));
                Objects cats = odb.getObjects(query);
                if (cats != null && !cats.isEmpty()) {
                    ServiceAttributes sa = (ServiceAttributes) cats.getFirst();
                    ServiceCore sc = sa.getServiceCore();
                    invokeServiceEditor(sc);
                } else {
                    System.err.println("Service empty!");
                }
            } else {
                JOptionPane.showMessageDialog(DatabaseManager.this, "Nothing selected!", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class TreeRemoveMenuAction extends AbstractAction{
        public TreeRemoveMenuAction(String name){
            super(name);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            ODB odb = null;
            try{
                odb = Constants.getDbConnection();
            }catch(NullPointerException ex){
                System.err.println(""+ex.getMessage());
                return;
            }

            if (jTree1.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(DatabaseManager.this, "Nothing selected!", "Error!", JOptionPane.ERROR_MESSAGE);
            } else {
                String selectedNode = jTree1.getLastSelectedPathComponent().toString();
                if (jTree1.getSelectionPath().getPath().length == 2) {
                    System.out.println("Removing category: " + jTree1.getLastSelectedPathComponent().toString());
                    Object[] options = {"Yes", "No", "Cancel"};
                    int n = JOptionPane.showOptionDialog(rootPane, "Remove category " +
                            jTree1.getLastSelectedPathComponent().toString() +
                            "? Removing category will cause all of its subcategories and services be removed as well!",
                            "Prompt", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                    if (n == 0) {

                        IQuery query = new CriteriaQuery(ServiceDescription.class,
                                Where.equal("category.name", selectedNode));
                        Objects services = odb.getObjects(query);
                        //remove services inside category
                        if (!services.isEmpty() && services != null) {
                            System.out.println("number of services to be deleted: " + services.size());
                            while (services.hasNext()) {
                                ServiceDescription sd = (ServiceDescription) services.next();
                                removeServiceByDescription(sd);
                            }
                            odb.commit();
                        } else {
                            System.out.println("No services in category!");
                        }

                        //remove subcategories inside category

                        IQuery query1 = new CriteriaQuery(Subcategory.class,
                                Where.equal("category.name", selectedNode));
                        Objects subcategories = odb.getObjects(query1);
                        if (!subcategories.isEmpty() && subcategories != null) {
                            System.out.println("number of subcategories to be deleted: " + subcategories.size());
                            while (subcategories.hasNext()) {
                                Subcategory sub = (Subcategory) subcategories.next();
                                Category catt = sub.getCategory();
                                removeSubcategory(catt, sub);
                            }
                            odb.commit();
                        } else {
                            System.out.println("No subcategories in category!");
                        }
                        //remove selected category
                        IQuery query2 = new CriteriaQuery(Category.class,
                                Where.equal("name", selectedNode));
                        Objects cats = odb.getObjects(query2);
                        if (!cats.isEmpty() && cats != null) {
                            System.out.println("Categories to be deleted: " + cats.size());
                            Category category = (Category) cats.getFirst();
                            odb.delete(category);
                            odb.commit();
                        } else {
                            System.err.println("No category match!");
                        }
                        refreshTreeData();
                        refreshTableModel();
                    }
                } else if (jTree1.getSelectionPath().getPath().length == 3) {
                    System.out.println("Removing subcategory: " + selectedNode);
                    Object[] options = {"Yes", "No", "Cancel"};
                    int n = JOptionPane.showOptionDialog(rootPane, "Remove subcategory " + selectedNode + "? Removing subcategory will cause all of its services be removed as well!", "Prompt", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                    if (n == 0) {
                        IQuery query3 = new CriteriaQuery(ServiceDescription.class, Where.equal("serviceSubCategory.name", jTree1.getLastSelectedPathComponent().toString()));
                        Objects services = odb.getObjects(query3);
                        //remove services inside subcategory
                        if (!services.isEmpty() && services != null) {
                            System.out.println("number of services to be deleted: " + services.size());
                            while (services.hasNext()) {
                                ServiceDescription sd = (ServiceDescription) services.next();
                                removeServiceByDescription(sd);
                            }
                            odb.commit();
                        } else {
                            System.out.println("No services in category!");
                        }
                        //remove subcategory
                        IQuery query4 = new CriteriaQuery(Subcategory.class, Where.equal("name", selectedNode));
                        Objects subcategories = odb.getObjects(query4);
                        if (!subcategories.isEmpty() && subcategories != null) {
                            System.out.println("number of subcategories to be deleted: " + subcategories.size());
                            Subcategory sub = (Subcategory) subcategories.getFirst();
                            Category catt = sub.getCategory();
                            removeSubcategory(catt, sub);
                            odb.commit();
                        } else {
                            System.out.println("No such subcategory!");
                        }
                        refreshTreeData();
                        refreshTableModel();
                    }
                }
            }
        }
    }//TreeRemoveMenuAction

    private class TreeCategoryMenuAction extends AbstractAction{
        public TreeCategoryMenuAction(String name){
            super(name);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            ODB odb = null;
            try{
                odb = Constants.getDbConnection();
            }catch(NullPointerException ex){
                System.err.println(""+ex.getMessage());
                return;
            }

            String newCat = JOptionPane.showInputDialog(DatabaseManager.this, "Enter new category name", "new category");
            if (newCat != null && !newCat.equals("")) {
                Objects categories = odb.getObjects(Category.class);
                if (!categories.isEmpty()) {
                    //check duplicate name
                    while (categories.hasNext()) {
                        Category category = (Category) categories.next();
                        if (category.getName().equalsIgnoreCase(newCat)) {
                            JOptionPane.showMessageDialog(null, "Category already exists! Change new category name.", "Warning", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    //no duplicate name
                    addCategory(newCat);

                } else {
                    //if no cat present
                    addCategory(newCat);
                }
            } else {
                System.out.println("no name input");
            }
        }
    }

    private class TreeSubcategoryMenuAction extends AbstractAction{
        public TreeSubcategoryMenuAction(String name){
            super(name);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            ODB odb = null;
            try{
                odb = Constants.getDbConnection();
            }catch(NullPointerException ex){
                System.err.println(""+ex.getMessage());
                return;
            }

            if (jTree1.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(DatabaseManager.this, "No category selected!", "Error!", JOptionPane.ERROR_MESSAGE);
            } else {
                if (jTree1.getSelectionPath().getPath().length == 2) {
                    String newsubCat = JOptionPane.showInputDialog(DatabaseManager.this, "Enter new subcategory name:", "new subcategory");
                    if (newsubCat == null) {
                    } else if (newsubCat.equals("")) {
                        JOptionPane.showMessageDialog(DatabaseManager.this, "You have to input name!", "Warning", JOptionPane.ERROR_MESSAGE);
                    } else {
                        IQuery query = new CriteriaQuery(Category.class, Where.equal("name", jTree1.getLastSelectedPathComponent().toString()));
                        Objects categories = odb.getObjects(query);
                        Category category = (Category) categories.getFirst();

                        if (category.getSubcategories() == null) {
                            category.setSubcategories(new Vector());
                            addSubcategory(category, newsubCat);
                            odb.commit();
                        } else {
                            if (category.getSubcategories().isEmpty()) {
                                addSubcategory(category, newsubCat);
                                odb.commit();
                            } else {
                                for (Object sub : category.getSubcategories()) {
                                    Subcategory sc = (Subcategory) sub;
                                    if (sc.getName().equalsIgnoreCase(newsubCat)) {
                                        JOptionPane.showMessageDialog(DatabaseManager.this, "Subcategory already exists! Change new subcategory name.", "Warning", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                }
                                addSubcategory(category, newsubCat);
                                odb.commit();
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(DatabaseManager.this, "Selection is not a category!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//TreeSubcategoryMenuAction

    private class TreeEditMenuAction extends AbstractAction{
        public TreeEditMenuAction(String name){
            super(name);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            ODB odb = null;
            try{
                odb = Constants.getDbConnection();
            }catch(NullPointerException ex){
                System.err.println(""+ex.getMessage());
                return;
            }

            boolean FL_D = false;
            boolean FL_DD = false;
            if (jTree1.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(DatabaseManager.this, "Nothing selected!", "Error!", JOptionPane.ERROR_MESSAGE);
            } else {
                if (jTree1.getSelectionPath().getPath().length == 2) {
                    String newcat = JOptionPane.showInputDialog("Please input new name for the selected category " + jTree1.getLastSelectedPathComponent().toString() + ":", "new name");
                    if (newcat == null) {
                    } else if (newcat.equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(DatabaseManager.this, "You have to enter valid category name!", "Warning", JOptionPane.ERROR_MESSAGE);
                    } else {
                        IQuery query = new CriteriaQuery(Category.class, Where.equal("name", newcat));
                        Objects categories = odb.getObjects(query);
                        if (!categories.isEmpty()) {
                            JOptionPane.showMessageDialog(DatabaseManager.this, "Category already exists! Change category name.", "Warning", JOptionPane.ERROR_MESSAGE);
                        } else {
                            IQuery query1 = new CriteriaQuery(Category.class, Where.equal("name", jTree1.getLastSelectedPathComponent().toString()));
                            Objects cats = odb.getObjects(query1);
                            Category cat = (Category) cats.getFirst();
                            cat.setName(newcat);
                            odb.store(cat);
                            odb.commit();
                        }
                    }

                } else if (jTree1.getSelectionPath().getPath().length == 3) {
                    String newsubcat = JOptionPane.showInputDialog("Please input new name for the selected subcategory " + jTree1.getLastSelectedPathComponent().toString() + ":", "new name");
                    if (newsubcat == null) {
                    } else if (newsubcat.equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(DatabaseManager.this, "You have to enter valid subcategory name!", "Warning", JOptionPane.ERROR_MESSAGE);
                    } else {
                        IQuery query = new CriteriaQuery(Category.class, Where.equal("name", jTree1.getSelectionPath().getParentPath().getLastPathComponent().toString()));
                        Objects cats = odb.getObjects(query);
                        Category c = (Category) cats.getFirst();
                        for (Object subcategory : c.getSubcategories()) {
                            Subcategory s = (Subcategory) subcategory;
                            if (s.getName().equalsIgnoreCase(newsubcat)) {
                                JOptionPane.showMessageDialog(DatabaseManager.this, "Subcategory already exists! Change new subcategory name.", "Warning", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }

                        for (Object subcategory : c.getSubcategories()) {
                            Subcategory s = (Subcategory) subcategory;
                            if (s.getName().equalsIgnoreCase(jTree1.getLastSelectedPathComponent().toString())) {
                                s.setName(newsubcat);
                                odb.store(c);
                                odb.commit();
                                break;
                            }
                        }
                    }
                }
            }//actionPerformed
        }
    }//TreeEditMenuActio

}
