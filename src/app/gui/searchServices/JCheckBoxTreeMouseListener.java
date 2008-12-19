/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.searchServices;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created on 2008-12-17, 06:35:22
 * @author vara
 */
public class JCheckBoxTreeMouseListener extends MouseAdapter {
    private JCheckBoxTree tree;
    public JCheckBoxTreeMouseListener(JCheckBoxTree tree) {
        this.tree = tree;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        if(selRow != -1) {
            if (!tree.isRowSelected(selRow)) {
                tree.setSelectionRow(selRow);
            }
            Rectangle rect = tree.getRowBounds(selRow);
            if(rect.contains(e.getX(),e.getY())) {
                tree.setChecked(tree.getPathForRow(selRow));
            }
        }
    }
}