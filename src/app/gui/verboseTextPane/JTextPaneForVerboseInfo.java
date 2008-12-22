/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.verboseTextPane;

import app.gui.*;
import app.gui.verboseTextPane.MyTextPane.ClearAllAction;
import app.utils.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
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

    private JTextPane textPane = new MyTextPane();
    private StyledDocument document;
    
    private SimpleAttributeSet defaultAttributes;
    private SimpleAttributeSet errorAtributes;
    
    private DocumentStatus pipe = new DocumentStatus();
    
    public JTextPaneForVerboseInfo(){

        StyleContext context = new StyleContext();
        Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
        StyleConstants.setFontSize(style, 4);
        StyleConstants.setSpaceAbove(style, 1);
        StyleConstants.setSpaceBelow(style, 1);

        //StyleConstants.setBold(attributes, true);
        int positions[] = { TabStop.ALIGN_BAR, TabStop.ALIGN_CENTER, TabStop.ALIGN_DECIMAL,
                    TabStop.ALIGN_LEFT, TabStop.ALIGN_RIGHT };
        TabStop [] tabstop = new TabStop[] {new TabStop(130, positions[3], TabStop.ALIGN_CENTER)};
        TabSet tabset = new TabSet(tabstop);

        defaultAttributes = new SimpleAttributeSet();
        errorAtributes = new SimpleAttributeSet();
        StyleConstants.setTabSet(defaultAttributes,tabset);        
        StyleConstants.setForeground(errorAtributes, new Color(165,2,2));
        
        document = new DefaultStyledDocument(context);
        document.setParagraphAttributes(0,1,defaultAttributes,true);
        document.addDocumentListener(new MyDocumentListener());
        textPane.setDocument(document);
        
        add(textPane);
        setViewportView(textPane);
    }
    public JTextPane getTextEditor(){
        return getTextPane();
    }
    
    public synchronized void addEndTextnl(String str,SimpleAttributeSet attr){

        if(str.startsWith("\tat")){
            attr = new SimpleAttributeSet(attr);
            StyleConstants.setForeground(attr,Color.BLUE);
        }
        try {
            getDocument().insertString(getDocument().getLength(), str+"\n", attr);
            getTextPane().setCaretPosition(getDocument().getLength());

        } catch (BadLocationException ex) {
            MyLogger.log.logp(Level.WARNING,getClass().getName(),
                    "addEndTextnl(String str)","str \""+str+"\"",ex);
        }
    }

    public void addEndTextnl(String str){
        addEndTextnl(str,getDefaultAttributes());
    }

    public DocumentStatus getInforamtionPipe(){
        return pipe;
    }

    /**
     * @return the document
     */
    protected StyledDocument getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    protected void setDocument(StyledDocument document) {
        this.document = document;
    }

    /**
     * @return the defaultAttributes
     */
    protected SimpleAttributeSet getDefaultAttributes() {
        return defaultAttributes;
    }

    /**
     * @param defaultAttributes the defaultAttributes to set
     */
    protected void setDefaultAttributes(SimpleAttributeSet defaultAttributes) {
        this.defaultAttributes = defaultAttributes;
    }

    /**
     * @return the errorAtributes
     */
    protected SimpleAttributeSet getErrorAtributes() {
        return errorAtributes;
    }

    /**
     * @param errorAtributes the errorAtributes to set
     */
    protected void setErrorAtributes(SimpleAttributeSet errorAtributes) {
        this.errorAtributes = errorAtributes;
    }

    /**
     * @return the textPane
     */
    protected JTextPane getTextPane() {
        return textPane;
    }

    /**
     * @param textPane the textPane to set
     */
    protected void setTextPane(JTextPane textPane) {
        this.textPane = textPane;
    }

    protected class DocumentStatus extends OutputVerboseStreamAdapter{
        @Override
        public void outputVerboseStream(String text){
            addEndTextnl(text);
        }
        @Override
        public void outputErrorVerboseStream(String text) {
            addEndTextnl(text,getErrorAtributes());
        }
    }        

    protected class MyDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            //displayEditInfo(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            //displayEditInfo(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            //displayEditInfo(e);
        }

        private void displayEditInfo(DocumentEvent e) {
                Document document = (Document)e.getDocument();
                int changeLength = e.getLength();
                System.out.println(e.getType().toString() + ": "
                    + changeLength + " character"
                    + ((changeLength == 1) ? ". " : "s. ")
                    + " Text length = " + document.getLength());
        }
    }
}
