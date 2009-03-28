/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.VerboseTextPane;

import app.navigps.gui.VerboseTextPane.MyTextPane.DocumentStatus;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *
 * @author vara
 */

public class JTextPaneForVerboseInfo extends JScrollPane{

    private MyTextPane textPane = new MyTextPane();
    
    
    /**
     *
     */
    public JTextPaneForVerboseInfo(){

        add(textPane);
        setViewportView(textPane);
        getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!e.getValueIsAdjusting()) {
                    return;
                }
                JScrollBar sb = JTextPaneForVerboseInfo.this.getVerticalScrollBar();
                int currentVal = sb.getValue();
                int range = sb.getMaximum() - sb.getMinimum()
                    - sb.getModel().getExtent();
                boolean scrollVal = range == currentVal;
                if(scrollVal != textPane.isAutoScroll())
                    textPane.setAutoScroll(scrollVal);
            }
        });

    }
    /**
     *
     * @return
     */
    public JTextPane getTextEditor(){
        return getTextPane();
    }   

    /**
     *
     * @return
     */
    public DocumentStatus getInforamtionPipe(){
        return textPane.getDocumentStatus();
    }

    /**
     * @return the textPane
     */
    protected JTextPane getTextPane() {
        return textPane;
    }
}
