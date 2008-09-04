/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

/**
 *
 * @author vara
 */
public class JTextPaneForVerboseInfo extends JScrollPane{

    private JTextPane text = new JTextPane();
    private StyledDocument doc;
    
    private SimpleAttributeSet attributes;
    
    public JTextPaneForVerboseInfo(){
	StyleContext context = new StyleContext();
	doc = new DefaultStyledDocument(context);

	Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
	StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
	StyleConstants.setFontSize(style, 4);
	StyleConstants.setSpaceAbove(style, 2);
	StyleConstants.setSpaceBelow(style, 2);

	attributes = new SimpleAttributeSet();
	//StyleConstants.setBold(attributes, true);
	int positions[] = { TabStop.ALIGN_BAR, TabStop.ALIGN_CENTER, TabStop.ALIGN_DECIMAL,
			    TabStop.ALIGN_LEFT, TabStop.ALIGN_RIGHT };
	TabStop tabstop = new TabStop(180, positions[3], TabStop.LEAD_DOTS);
	TabSet tabset = new TabSet(new TabStop[] { tabstop });	
	StyleConstants.setTabSet(attributes,tabset);
	
	text.setDocument(doc);
	add(text);	
	text.setEditable(false);	
	setViewportView(text);	
    }
    public JTextPane getTextEditor(){return text;}
    
    public void addEndTextnl(String str){
	try {

	    //Logger.getLogger("navigps").log(Level.SEVERE, null, str);
	    doc.setParagraphAttributes(doc.getLength(), 1, attributes, false);
	    doc.insertString(doc.getLength(), str+"\n", attributes);
	} catch (BadLocationException ex) {
	    Logger.getLogger("navigps").log(Level.SEVERE, null, ex);
	}
	
    }
}
