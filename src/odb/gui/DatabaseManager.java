package odb.gui;

import config.DataBaseConfig;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import odb.core.Category;
import odb.core.ServiceAttributes;
import odb.core.ServiceCore;
import odb.core.ServiceDescription;
import odb.core.Subcategory;
import odb.utils.Constants;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

/**
 *
 * @author ACME
 */
public class DatabaseManager extends javax.swing.JDialog {

    private ODB odb = Constants.getDbConnection();
    private JPopupMenu popup;
    private JMenuItem categoryMenu;
    private JMenuItem subcategoryMenu;
    private JMenuItem editMenu;
    private JMenuItem removeMenu;
    private JMenu newMenu;

    /** Creates new form Manager
     * @param parent
     * @param modal
     */
    public DatabaseManager(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setLocationRelativeTo(parent);
        initComponents();
        refreshTree();
        loadTreePopup();
        initServicesTable();

        initFormListeners();
    }

    private void initFormListeners() {
        //JTABLE mouse adapter
        jTable1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    IQuery query = new CriteriaQuery(ServiceAttributes.class, Where.and().add(Where.equal("x", jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 3))).add(Where.equal("y", jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 4))));
                    Objects cats = odb.getObjects(query);
                    if (cats != null && !cats.isEmpty()) {
                        ServiceAttributes sa = (ServiceAttributes) cats.getFirst();
                        ServiceCore sc = sa.getServiceCore();
                        ServiceEditor se = new ServiceEditor(null, true, sc);
                        se.setVisible(true);
                    } else {
                        System.out.println("cell selection error!");
                    }
                }
            }
        });
        //POPUP remove menu listener
        removeMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTree1.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(DatabaseManager.this, "Nothing selected!", "Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (jTree1.getSelectionPath().getPath().length == 2) {
                        System.out.println("Removing category: " + jTree1.getLastSelectedPathComponent().toString());
                        Object[] options = {"Yes", "No", "Cancel"};
                        int n = JOptionPane.showOptionDialog(rootPane, "Remove category " + jTree1.getLastSelectedPathComponent().toString() + "? Removing category will cause all of its subcategories and services be removed as well!", "Prompt", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                        if (n == 0) {
                            IQuery query1 = new CriteriaQuery(ServiceDescription.class, Where.equal("category.name", jTree1.getLastSelectedPathComponent().toString()));
                            Objects services = odb.getObjects(query1);
                            //remove services inside category
                            if (!services.isEmpty() && services != null) {
                                System.out.println("number of services to be deleted: " + services.size());
                                while (services.hasNext()) {
                                    ServiceDescription sd = (ServiceDescription) services.next();
                                    ServiceCore sc = sd.getServiceCore();
                                    ServiceAttributes sa = sc.getServiceAttributes();
                                    odb.delete(sa);
                                    odb.delete(sd);
                                    odb.delete(sc);
                                }
                                odb.commit();
                            } else {
                                System.out.println("No services in category!");
                            }
                            //remove subcategories inside category
                            IQuery query = new CriteriaQuery(Subcategory.class, Where.equal("category.name", jTree1.getLastSelectedPathComponent().toString()));
                            Objects subcategories = odb.getObjects(query);
                            if (!subcategories.isEmpty() && subcategories != null) {
                                System.out.println("number of subcategories to be deleted: " + subcategories.size());
                                while (subcategories.hasNext()) {
                                    Subcategory sub = (Subcategory) subcategories.next();
                                    odb.delete(sub);
                                }
                                odb.commit();
                            } else {
                                System.out.println("No subcategories in category!");
                            }
                            //remove selected category
                            IQuery query2 = new CriteriaQuery(Category.class, Where.equal("name", jTree1.getLastSelectedPathComponent().toString()));
                            Objects cats = odb.getObjects(query2);
                            if (!cats.isEmpty() && cats != null) {
                                System.out.println("Categories to be deleted: " + cats.size());
                                Category category = (Category) cats.getFirst();
                                odb.delete(category);
                                odb.commit();
                            } else {
                                System.err.println("No category match!");
                            }
                            refreshTree();
                            reloadServicesTable();
                        }
                    } else if (jTree1.getSelectionPath().getPath().length == 3) {
                        System.out.println("Removing subcategory: " + jTree1.getLastSelectedPathComponent().toString());
                        Object[] options = {"Yes", "No", "Cancel"};
                        int n = JOptionPane.showOptionDialog(rootPane, "Remove subcategory " + jTree1.getLastSelectedPathComponent().toString() + "? Removing subcategory will cause all of its services be removed as well!", "Prompt", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                        if (n == 0) {
                            IQuery query1 = new CriteriaQuery(ServiceDescription.class, Where.equal("serviceSubCategory.name", jTree1.getLastSelectedPathComponent().toString()));
                            Objects services = odb.getObjects(query1);
                            //remove services inside category
                            if (!services.isEmpty() && services != null) {
                                System.out.println("number of services to be deleted: " + services.size());
                                while (services.hasNext()) {
                                    ServiceDescription sd = (ServiceDescription) services.next();
                                    ServiceCore sc = sd.getServiceCore();
                                    ServiceAttributes sa = sc.getServiceAttributes();
                                    odb.delete(sa);
                                    odb.delete(sd);
                                    odb.delete(sc);
                                }
                                odb.commit();
                            } else {
                                System.out.println("No services in category!");
                            }
                            //remove subcategory
                            IQuery query = new CriteriaQuery(Subcategory.class, Where.equal("name", jTree1.getLastSelectedPathComponent().toString()));
                            Objects subcategories = odb.getObjects(query);
                            if (!subcategories.isEmpty() && subcategories != null) {
                                System.out.println("number of subcategories to be deleted: " + subcategories.size());
                                Subcategory sub = (Subcategory) subcategories.getFirst();
                                Category catt = sub.getCategory();
                                catt.removeSubcategory(sub);
                                odb.delete(sub);
                                odb.store(catt);
                                odb.commit();
                            } else {
                                System.out.println("No such subcategory!");
                            }
                            refreshTree();
                            reloadServicesTable();
                        }
                    }
                }
            }
        });

        //POPUP category menu listener
        categoryMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (odb != null) {
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

                } else {
                    System.out.println("no db initialized");
                }
            }
        });
        //POPUP subcategory menu listener
        subcategoryMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTree1.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(DatabaseManager.this, "No category selected!", "Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (jTree1.getSelectionPath().getPath().length == 2) {
                        boolean FL_DS = false;
                        String newsubCat = JOptionPane.showInputDialog(DatabaseManager.this, "Enter new subcategory name:", "new subcategory");

                        if (newsubCat == null) {
                            //cancel
                        } else if (newsubCat.equals("")) {
                            JOptionPane.showMessageDialog(DatabaseManager.this, "You have to input name!", "Warning", JOptionPane.ERROR_MESSAGE);
                        } else {
                            IQuery query = new CriteriaQuery(Category.class, Where.equal("name", jTree1.getLastSelectedPathComponent().toString()));
                            Objects categories = odb.getObjects(query);
                            Category category = (Category) categories.getFirst();

                            if (category.getSubcategories() == null) {
                                category.setSubcategories(new Vector());
                                category.addSubcategory(new Subcategory(category, newsubCat));
                                odb.store(category);
                                odb.commit();
                            } else {
                                if (category.getSubcategories().isEmpty()) {
                                    category.addSubcategory(new Subcategory(category, newsubCat));
                                } else {
                                    for (Object sub : category.getSubcategories()) {
                                        Subcategory sc = (Subcategory) sub;
                                        if (sc.getName().equalsIgnoreCase(newsubCat)) {
                                            FL_DS = true;
                                        }
                                    }
                                    if (FL_DS) {
                                        JOptionPane.showMessageDialog(DatabaseManager.this, "Subcategory already exists! Change new subcategory name.", "Warning", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        category.addSubcategory(new Subcategory(category, newsubCat));
                                        odb.store(category);
                                        odb.commit();
                                    }
                                }
                            }
                            refreshTree();
                        }
                    } else {
                        JOptionPane.showMessageDialog(DatabaseManager.this, "Selection is not a category!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
        //POPUP edit menu listener
        editMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean FL_D = false;
                boolean FL_DD = false;
                if (jTree1.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(DatabaseManager.this, "Nothing selected!", "Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (jTree1.getSelectionPath().getPath().length == 2) {
                        String newcat = JOptionPane.showInputDialog("Please input new name for the selected category " + jTree1.getLastSelectedPathComponent().toString() + ":", "new name");
                        if (newcat == null) {
//                            cancel
                        } else if (newcat.equalsIgnoreCase("")) {
                            JOptionPane.showMessageDialog(DatabaseManager.this, "You have to enter valid category name!", "Warning", JOptionPane.ERROR_MESSAGE);
                        } else {
                            Objects categories = odb.getObjects(Category.class);
                            while (categories.hasNext()) {
                                Category c = (Category) categories.next();
                                if (c.getName().equalsIgnoreCase(newcat)) {
                                    FL_D = true;
                                    break;
                                }
                            }
                            if (FL_D) {
                                JOptionPane.showMessageDialog(DatabaseManager.this, "Category already exists! Change category name.", "Warning", JOptionPane.ERROR_MESSAGE);
                            } else {
                                IQuery query = new CriteriaQuery(Category.class, Where.equal("name", jTree1.getLastSelectedPathComponent().toString()));
                                Objects cats = odb.getObjects(query);
                                Category cat = (Category) cats.getFirst();
                                cat.setName(newcat);
                                odb.store(cat);
                                odb.commit();
                                refreshTree();
                                initServicesTable();
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
                                    FL_DD = true;
                                }
                            }
                            if (FL_DD) {
                                JOptionPane.showMessageDialog(DatabaseManager.this, "Subcategory already exists! Change new subcategory name.", "Warning", JOptionPane.ERROR_MESSAGE);
                            } else {
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
                            refreshTree();
                        }
                    }
                }
            }
        });
    }

    public void reloadServicesTable() {
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
                jTable1.setModel(model);
            } else {
                jTable1.setModel(model);
            }
        } else {
            System.err.println("DataBase not initialized!");
        }
    }

    private void addNewService() {
        ServiceFactory sf = new ServiceFactory(null, true);
        sf.setVisible(true);
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
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Avaliable services", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 153, 255))); // NOI18N

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBackground(new java.awt.Color(255, 255, 204));
        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
        );

        jButton3.setFont(new java.awt.Font("Verdana", 0, 11));
        jButton3.setText("New ...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Attributes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 153, 255))); // NOI18N

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

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Category tree", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 153, 255))); // NOI18N

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
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
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

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        disconnectDatabase();
        Constants.setManagerWindow(null);
    }//GEN-LAST:event_formWindowClosing

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        addNewService();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTree1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseReleased
        if (evt.isPopupTrigger()) {
            popup.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_jTree1MouseReleased

    private void jTree1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MousePressed
        if (evt.isPopupTrigger()) {
            popup.show(evt.getComponent(), evt.getX(), evt.getY());
        }
}//GEN-LAST:event_jTree1MousePressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        Constants.setManagerWindow(this);
    }//GEN-LAST:event_formWindowOpened

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
        odb.store(new Category(category));
        odb.commit();
        refreshTree();
    }

    private void loadTreePopup() {
        popup = new JPopupMenu();

        newMenu = new JMenu("New");
        categoryMenu = new JMenuItem("Category");
        subcategoryMenu = new JMenuItem("Subcategory");
        removeMenu = new JMenuItem("Remove");
        editMenu = new JMenuItem("Edit");

        newMenu.add(categoryMenu);
        newMenu.add(subcategoryMenu);

        popup.add(newMenu);
        popup.add(new JPopupMenu.Separator());
        popup.add(editMenu);
        popup.add(removeMenu);
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

    private void refreshTree() {
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
     */
    public void initServicesTable() {
        Vector v = null;

        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setToolTipText("Double click to edit!");
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                jTextArea1.setText("");
                if (odb != null) {
                    int selectedRow = jTable1.getSelectedRow();
                    System.out.println("Selected row " + selectedRow);
                    if (selectedRow != -1) {
                        Float sx = (Float) jTable1.getModel().getValueAt(selectedRow, 3);
                        Float sy = (Float) jTable1.getModel().getValueAt(selectedRow, 4);
                        System.out.println("SX " + sx + " sy " + sy);

                        IQuery query = new CriteriaQuery(ServiceAttributes.class, Where.and().add(Where.equal("x", sx)).add(Where.equal("y", sy)));

                        Objects cats = odb.getObjects(query);
                        if (cats != null && !cats.isEmpty()) {
                            ServiceAttributes sa = (ServiceAttributes) cats.getFirst();
                            ServiceDescription sd = sa.getServiceCore().getServiceDescription();

                            Subcategory subCat = sd.getServiceSubCategory();
                            String sSubCat = "no subcategory";
                            if (subCat != null) {
                                sSubCat = subCat.getName();
                            }
                            /*
                             *  FIXME null pointer exception !!!!!!!!!!!!
                             */
                            jTextArea1.setText("Attributes:\nService at x: " + sa.getX() +
                                    " y: " + sa.getY() +
                                    "\nDescription:\nName: " + sd.getServiceName() +
                                    "\nStreet: " + sd.getServiceStreet() +
                                    "\nNumber: " + sd.getServiceNumber() +
                                    "\nCategory: " + sd.getCategory().getName() +
                                    "\nSubcategory: " + sSubCat +
                                    "\nAdditional: " + sd.getAdditionaInfo());
                        } else {
                            System.out.println("empty selection");
                        }
                    } else {
                        System.err.println(this.getClass().getCanonicalName() + " valueChanged -> selected row is -1 !!!");
                    }

                } else {
                    System.out.println("db not initialized!");
                }
            }
        });
        reloadServicesTable();
    }
}
