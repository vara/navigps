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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author wara
 */
public class TableRendererForFileParameter extends DefaultTableCellRenderer{

    public JPanel panel = new JPanel();
    public JButton button  = new JButton("...");

    public TableRendererForFileParameter(){
        panel.setLayout(new BorderLayout(3, 3));
        panel.setOpaque(false);
        
        //button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel panelForButt = new JPanel(new BorderLayout(3,3));
        panelForButt.setOpaque(false);
        panelForButt.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 4));

        button.setMargin(new Insets(1, 1,1, 1));
        button.setFont(button.getFont().deriveFont(9));

        panelForButt.add(button);
        panel.add(panelForButt, BorderLayout.EAST);

        button.setPreferredSize(new Dimension(13, 13));

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if(!hasFocus)
            setBackground((row % 2 == 0) ? Color.white : new Color(231,239,246));

        hasFocus = false;
        Object obj = ((ObjectParameterForJTable)value).getValueForTable();

        String val="";
        if( obj instanceof String){
            val = (String)obj;           
        }

        Component comp = super.getTableCellRendererComponent(table, val, isSelected, hasFocus, row, column);


        panel.add(comp,BorderLayout.CENTER);
        return panel;
    }

}
