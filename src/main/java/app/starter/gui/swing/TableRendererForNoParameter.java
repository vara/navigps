/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.starter.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.lang.Object;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author wara
 */
public class TableRendererForNoParameter extends DefaultTableCellRenderer{

    public JPanel panel = new JPanel();    
    public JCheckBox checkBox  = new JCheckBox();

    public TableRendererForNoParameter(){
        panel.setLayout(new BorderLayout(3, 0));
        panel.setOpaque(false);
        checkBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
        checkBox.setMargin(new Insets(0, 0, 0, 0));
        panel.add(checkBox, BorderLayout.EAST);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if(!hasFocus)
            setBackground((row % 2 == 0) ? Color.white : new Color(231,239,246));

        hasFocus = false;
        Object obj = ((ObjectParameterForJTable)value).getValueForTable();

        if( obj instanceof Boolean){
            boolean val = (Boolean)obj;
            if(val){
                checkBox.setSelected(true);                
            } else {
                checkBox.setSelected(false);
            }
        }

        Component comp = super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);



        panel.add(comp,BorderLayout.CENTER);
        return panel;
    }

}
