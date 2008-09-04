/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.xml.stream.events.Comment;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


/**
 *
 * @author vara
 */

public class SVGDOMTreeRenderer extends DefaultTreeCellRenderer{

    public SVGDOMTreeRenderer(){
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
						  boolean sel,
						  boolean expanded,
						  boolean leaf, int row,
						  boolean hasFocus) {
	Node node = (Node)value;
	if(node instanceof Element) return elementPanel((Element)node);
	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	if(node instanceof CharacterData)
	    setText(characterString((CharacterData)node));
	else
	    setText(node.getClass()+":"+node.toString());
	return this;
    }
    
    protected JPanel elementPanel(Element el){
	JPanel pan = new JPanel();
	pan.add(new JLabel("Element : "+el.getTagName()));
	pan.add(new JTable(new SVGDOMAttributeTableModel(el.getAttributes())));
	return pan;	
    }

    private String characterString(CharacterData node) {
	//System.out.println("metoda characterString");
	StringBuilder builder = new StringBuilder(node.getData());
	for (int i = 0; i < builder.length(); i++) {
	    if(builder.charAt(i)=='\r'){
		builder.replace(i, i+1, "\\r");
		i++;
	    }else if(builder.charAt(i)=='\n'){
		builder.replace(i, i+1, "\\n");
		i++;
	    }else  if(builder.charAt(i)=='\t'){
		builder.replace(i, i+1, "\\t");
		i++;
	    }	    	    
	}
	if(node instanceof CDATASection){
	    builder.insert(0,"CDATASection: ");		
	}else if(node instanceof Text){
	    builder.insert(0,"Text: ");		
	}if(node instanceof Comment){
	    builder.insert(0,"Comment: ");		
	}
	//System.out.println(""+builder.toString());
	return builder.toString();
    }
}
