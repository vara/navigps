/*
 * NaviToolBarPanel.java
 *
 * Created on 2009-04-15, 14:54:40
 */

package app.navigps.gui.ToolBar;

import app.navigps.gui.ToolBar.Layout.NaviToolBarLayout;
import app.navigps.gui.detailspanel.AlphaJPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class NaviToolBarPanel extends AlphaJPanel implements ContainerListener{

    public NaviToolBarPanel(){
       super(new NaviToolBarLayout(2,0));
       init();
    }

    private void init(){
       setBorder(new EmptyBorder(0,1,0,1));
       setBackground(new Color(238,238,238));
       addContainerListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       /* final Graphics2D g2 = (Graphics2D) g;
        GradientPaint gradient1 = new GradientPaint(0.0f, (float) getHeight(), Color.white,
                0.0f, 8.5f, new Color(235, 245, 255));
        Rectangle rec1 = new Rectangle(-1, -1, getWidth()+1, getHeight()+1);
        g2.setPaint(gradient1);
        //g2.setColor(Color.RED);
        g2.fill(rec1);
        */         
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
    }

    @Override
    public void componentAdded(ContainerEvent e) {
        System.err.println("comp added "+e.getChild().getName());
    }

    @Override
    public void componentRemoved(ContainerEvent e) {
        System.err.println("comp removed "+e.getChild().getName());
    }
}
