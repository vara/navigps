 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DetailsPanel.java
 *
 * Created on 2008-12-30, 19:07:33
 */

package app.navigps.gui.displayItemsMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.util.gui.xmleditor.XMLTextEditor;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author vara
 */
public class DetailsPanel extends javax.swing.JPanel {
    
    /**
     *
     */
    protected XMLEditorPanel nodeXmlArea;


    /** Creates new form DetailsPanel */
    public DetailsPanel() {
        initComponents();
        //jTextArea1.setOpaque(false);
        //jScrollPane1.setOpaque(false);
        //jScrollPane1.setBackground(new Color(255,255,255,100));
        //jScrollPane1.setBorder(new OvalBorder(10,10));
        //jScrollPane1.getViewport().setOpaque(false);
        //jScrollPane1.getViewport().setBorder(null);

        jScrollPane2.setBackground(new Color(255,255,255,100));
        jScrollPane2.getViewport().setOpaque(false);
        jScrollPane2.getViewport().setBorder(null);

        jTable1.setModel(new AttributesTableModel(10, 2));
        nodeXmlArea = new XMLEditorPanel();
        jPanelXmlEditor.setLayout(new BorderLayout());
        jPanelXmlEditor.add(nodeXmlArea,BorderLayout.CENTER);
    }
    //public JTextArea getTextArea(){
        //return jTextArea1;
    //}

    /**
     *
     * @return
     */
    public JTable getJTable(){
        return jTable1;
    }

    /**
     *
     * @return
     */
    public XMLEditorPanel getXMLEditorPanel(){
        return nodeXmlArea;
    }

    private void updateAttributesTable(Element elem) {
        NamedNodeMap map = elem.getAttributes();
        AttributesTableModel tableModel =
            (AttributesTableModel) jTable1.getModel();
        // Remove and update rows from the table if needed...
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            String attrName = (String) tableModel.getValueAt(i, 0);
            String newAttrValue = "";
            if (attrName != null) {
                newAttrValue = elem.getAttributeNS(null, attrName);
            }
            if (attrName == null || newAttrValue.length() == 0) {
                tableModel.removeRow(i);
            }
            if (newAttrValue.length() > 0) {
                tableModel.setValueAt(newAttrValue, i, 1);
            }
        }

        // Add rows
        for (int i = 0; i < map.getLength(); i++) {
            Node attr = map.item(i);
            String attrName = attr.getNodeName();
            String attrValue = attr.getNodeValue();
            if (tableModel.getValueForName(attrName) == null) {
                Vector rowData = new Vector();
                rowData.add(attrName);
                rowData.add(attrValue);
                tableModel.addRow(rowData);
            }
        }
    }

    private void updateNodeXmlArea(Node node) {
        getXMLEditorPanel().getNodeXmlArea().setText(DOMUtilities.getXML(node));
    }

    /**
     *
     * @param elem
     */
    public void setPreviewElement(Element elem) {
       
        updateNodeXmlArea(elem);
        updateAttributesTable(elem);
    }

    /**
     *
     * @param al
     */
    public void addActionToButton(ActionListener al){
        jButton1.addActionListener(al);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanelXmlEditor = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        jScrollPane2.setBorder(null);
        jScrollPane2.setOpaque(false);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanelXmlEditorLayout = new javax.swing.GroupLayout(jPanelXmlEditor);
        jPanelXmlEditor.setLayout(jPanelXmlEditorLayout);
        jPanelXmlEditorLayout.setHorizontalGroup(
            jPanelXmlEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 281, Short.MAX_VALUE)
        );
        jPanelXmlEditorLayout.setVerticalGroup(
            jPanelXmlEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 155, Short.MAX_VALUE)
        );

        jButton1.setText("reload tree");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
            .addComponent(jPanelXmlEditor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelXmlEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanelXmlEditor;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    /**
     *
     */
    public static class AttributesTableModel extends DefaultTableModel {
        /**
         *
         * @param rowCount
         * @param columnCount
         */
        public AttributesTableModel(int rowCount, int columnCount) {
            super(rowCount, columnCount);
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "Name";
            } else {
                return "Value";
            }
        }

        /**
         * Gets the value of the attribute with the given attribute name.
         *
         * @param attrName
         *            The given attribute name
         * @return
         */
        public Object getValueForName(Object attrName) {
            for (int i = 0; i < getRowCount(); i++) {
                if (getValueAt(i, 0) != null
                        && getValueAt(i, 0).equals(attrName)) {
                    return getValueAt(i, 1);
                }
            }
            return null;
        }

        /**
         * Gets the name of the attribute with the table row.
         * @param i
         * @return
         */
        public Object getAttrNameAt(int i) {
            return getValueAt(i, 0);
        }

        /**
         * Gets the value of the attribute with the table row.
         * @param i 
         * @return
         */
        public Object getAttrValueAt(int i) {
            return getValueAt(i, 1);
        }

        /**
         * Gets the first row where the given attribute name appears.
         * @param attrName    The given attribute name
         * @return
         */
        public int getRow(Object attrName) {
            for (int i = 0; i < getRowCount(); i++) {
                if (getValueAt(i, 0) != null
                        && getValueAt(i, 0).equals(attrName)) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     *
     */
    protected class XMLEditorPanel extends JPanel {

        /**
         * The text area.
         */
        protected XMLTextEditor nodeXmlArea;

        /**
         * Constructor.
         */
        public XMLEditorPanel() {
            super(new BorderLayout());
            JScrollPane sp = new JScrollPane();
            sp.setViewportView(getNodeXmlArea());
            add(sp,BorderLayout.CENTER);
        }

        /**
         * Gets the nodeXmlArea.
         *
         * @return    the nodeXmlArea
         */
        protected XMLTextEditor getNodeXmlArea() {
            if (nodeXmlArea == null) {
                // Create syntax-highlighted text area
                nodeXmlArea = new XMLTextEditor();
                nodeXmlArea.setEditable(true);
            }
            return nodeXmlArea;
        }
    }


}