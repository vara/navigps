/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import javax.swing.table.AbstractTableModel;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author vara
 */
public class SVGDOMAttributeTableModel extends AbstractTableModel{

    private NamedNodeMap map;
    
    public SVGDOMAttributeTableModel(NamedNodeMap map){
	this.map = map;
    }

    public int getRowCount() {
	return map.getLength();
    }

    public int getColumnCount() {
	return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
	return columnIndex==0 ? map.item(rowIndex).getNodeName() : map.item(rowIndex).getNodeValue();
    }
}
