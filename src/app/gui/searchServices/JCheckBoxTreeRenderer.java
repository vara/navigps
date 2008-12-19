/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.searchServices;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class JCheckBoxTreeRenderer extends DefaultTreeCellRenderer {
    private JPanel panel = new JPanel();
    public JCheckBox checkBox  = new JCheckBox();

    public JCheckBoxTreeRenderer() {
        super();
        panel.setLayout(new BorderLayout(3, 0));
        checkBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkBox.setMargin(new Insets(0, 0, 0, 0));
        panel.add(checkBox, BorderLayout.WEST);

        panel.setOpaque(false);
        checkBox.setOpaque(false);
    }
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
        
        boolean checked=false;
        boolean anyChecked=false;

        if(!(checked=((JCheckBoxTree)tree).isChecked(tree.getPathForRow(row)))){
            anyChecked = ((JCheckBoxTree)tree).isAnyChildChecked(tree.getPathForRow(row));
        }
        if(checked || anyChecked ){
            checkBox.setSelected(true);
        } else {
            checkBox.setSelected(false);
        }
        Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        comp.setForeground(Color.WHITE);
        setForeground(Color.WHITE);
        panel.add(comp, BorderLayout.CENTER);
        return (panel);
    }

    @Override
    public Color getBackgroundNonSelectionColor() {
        return null;
    }
    @Override
    public Color getBackground() {
        return null;
    }

}