/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.VerboseTextPane;

import app.navigps.gui.MyPopupMenu;
import app.navigps.utils.NaviLogger;
import app.navigps.utils.OutputVerboseStreamAdapter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ComponentUI;
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
 * Created on 2008-12-18, 13:53:36
 * @author vara
 */
public class MyTextPane extends JTextPane{


    private StyledDocument document;

    private SimpleAttributeSet defaultAttributes;
    private SimpleAttributeSet errorAtributes;

    private DocumentStatus pipe = new DocumentStatus();

    private LinkedList<Action> actions = new LinkedList<Action>();

    private boolean autoRoll = true;

    /**
     *
     */
    public static final Font DEFAULT_FONT = new Font("monospaced",Font.PLAIN,12);

    /**
     *
     */
    public MyTextPane(){

        setFont(DEFAULT_FONT);
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
        setDocument(document);

        setEditable(false);
        actions.add(new ClearAllAction("Clear All",
                        null,
                        "Clear All text from document",KeyEvent.VK_C));

        actions.add(new ShowTimeAction(ShowTimeAction.SHOW_TIME,
                        null,
                        "Show/Hide Time Stemp", KeyEvent.VK_T));

        addMouseListener(new MouseForVerboseTextPane());

        addEndText(new Date()+"\nVerbose text pane created ", errorAtributes);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        Component parent = getParent();
        ComponentUI compUi = getUI();

        return parent != null ?
            (compUi.getPreferredSize(this).width <= parent.getSize().width) : true;
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g2);
    }

    /**
     *
     * @return
     */
    public DocumentStatus getDocumentStatus(){
        return pipe;
    }

    /**
     * @return the actions
     */
    public LinkedList<Action> getMyActions() {
        return actions;
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
     *
     * @param str
     * @param attr
     */
    public synchronized void addEndText(String str,SimpleAttributeSet attr){
        try {
            ((StyledDocument)getDocument()).insertString(getDocument().getLength(), str, attr);            
            if(isAutoScroll()){
                Component c = getParent();
                if(c instanceof JViewport){                    
                    JViewport vp = (JViewport)c;
                    vp.scrollRectToVisible(vp.getViewRect());
                }
            }
        } catch (BadLocationException ex) {
            NaviLogger.log.logp(Level.WARNING,getClass().getName(),
                    "addEndTextnl(String str)","str \""+str+"\"",ex);
        }
    }
    /**
     *
     * @param str
     * @param attr
     */
    public synchronized void addEndTextnl(String str,SimpleAttributeSet attr){
        addEndText(str+"\n", attr);
    }

    /**
     * @return the autoScroll
     */
    public boolean isAutoScroll() {
        return autoRoll;
    }

    /**
     * @param autoScroll the autoScroll to set
     */
    public void setAutoScroll(boolean autoScroll) {
        this.autoRoll = autoScroll;
    }

    /**
     *
     */
    protected class DocumentStatus extends OutputVerboseStreamAdapter{
        /**
         *
         */
        public DocumentStatus(){
            //setTimeEnabled(true);
        }
        /**
         *
         * @param text
         */
        @Override
        public void outputVerboseStream(String text){
            SimpleAttributeSet attr = new SimpleAttributeSet(getDefaultAttributes());
            if(isTimeEnabled()){
                addEndText(getTime()+" ", attr);
            }
            addEndTextnl(text,attr);
        }
        /**
         *
         * @param text
         */
        @Override
        public void outputErrorVerboseStream(String text) {
            SimpleAttributeSet attr = new SimpleAttributeSet(getErrorAtributes());
            if(isTimeEnabled()){
                addEndText(getTime()+" ", attr);
            }
            if(text.startsWith("\tat")){
                StyleConstants.setForeground(attr,Color.BLUE);
                if(isAutoScroll()!=false)
                    setAutoScroll(false);
            }            
            addEndTextnl(text,attr);
        }
    }

    /**
     *
     */
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

    /**
     *
     */
    public class ClearAllAction extends AbstractAction{

        /**
         *
         * @param text
         * @param icon
         * @param desc
         * @param mnemonic
         */
        public ClearAllAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic){
            super(text);
            putValue(AbstractAction.SMALL_ICON, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
            putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.ALT_DOWN_MASK));
            setEnabled(true);

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            setText("");
            //setEnabled(false);
        }
    }

    /**
     *
     */
    public class ShowTimeAction extends AbstractAction{

        /**
         *
         */
        public static final String SHOW_TIME = "Show Time";
        /**
         *
         */
        public static final String HIDE_TIME = "Hide Time";

        /**
         *
         * @param text
         * @param icon
         * @param desc
         * @param mnemonic
         */
        public ShowTimeAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic){
            super(text);
            putValue(AbstractAction.SMALL_ICON, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
            putValue(AbstractAction.ACCELERATOR_KEY,
		    KeyStroke.getKeyStroke(mnemonic,InputEvent.ALT_DOWN_MASK));
            setEnabled(true);

            checkDisplayText();
        }

        private void checkDisplayText(){
            putValue(AbstractAction.NAME, pipe.isTimeEnabled() ? HIDE_TIME : SHOW_TIME);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            pipe.setTimeEnabled(!pipe.isTimeEnabled());
            checkDisplayText();
        }
    }

    /**
     *
     */
    protected class MouseForVerboseTextPane extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e){
            if(e.getButton()==MouseEvent.BUTTON3){
                MyPopupMenu popup = new MyPopupMenu();
                for (Action action : actions) {
                    popup.add(new JMenuItem(action));
                }
                for (Action action : getActions()) {
                    popup.add(new JMenuItem(action));
                }
                popup.show(MyTextPane.this,e.getX(),e.getY());
            }
        }
    }
}
