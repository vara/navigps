/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.displayItemsMap;

import app.gui.borders.OvalBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * Created on 2008-12-30, 19:10:57
 * @author vara
 */
public class MainDetailsPanel extends JSplitPane{

    private JPanel upperContent = new JPanel(new BorderLayout(20, 20));
    private JPanel lowerContent = new JPanel(new BorderLayout(20, 20));

    /**
     *
     */
    public MainDetailsPanel(){
        super(JSplitPane.VERTICAL_SPLIT, true);
        setBorder(null);
        setAlignmentX(5);
        setAlignmentY(10);
        setOneTouchExpandable(true);
        setDividerLocation(0.5);
        init();
    }

    private void init(){
        upperContent.setBorder(new CompoundBorder(new EmptyBorder(2,2,8,2),new OvalBorder(8,2,8,4,10,10)));
        lowerContent.setBorder(new CompoundBorder(new EmptyBorder(8,2,2,2),new OvalBorder(8,8,8,8,10,10)));
        
        setLeftComponent(upperContent);
        setRightComponent(lowerContent);

        setResizeWeight(0.7D);
    }

    /**
     *
     * @param comp
     */
    public void addToUpperContent(Component comp){
        upperContent.add(comp);
    }

    /**
     *
     * @param comp
     */
    public void addToLowerContent(Component comp){
        lowerContent.add(comp);
    }
}
