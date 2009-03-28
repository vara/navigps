/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
 *
 * @author vara
 */
public class MyPopupMenu extends JPopupMenu{
    
    /**
     *
     */
    public MyPopupMenu()
    {
        setLightWeightPopupEnabled(true);
        setAlignmentX(100);
    }
    /**
     *
     * @param item
     */
    public void addToPopup(JMenuItem [] item)
    {
        if(item.length>0){
            for(int i=0;i<item.length;i++)
                add(item[i]);
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //final Graphics2D g2 = (Graphics2D) g;
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        //GradientPaint gradient1 = new GradientPaint(0.0f, (float) getHeight(),new Color(174,201,255),
        //					    0.0f, 8.5f, Color.white);
        //Rectangle rec1 = new Rectangle(0, 0, getWidth(), getHeight());
        //g2.setPaint(gradient1);
        //g2.fill(rec1);
    }
    
    /**
     *
     * @param str
     * @param icon
     * @param mnemo
     * @param skrot
     * @param al
     * @return
     */
    public static JMenuItem menuItem(String str, Icon icon, int mnemo, String skrot, ActionListener al)
    {
        JMenuItem mi = new JMenuItem(str, icon);
        mi.setMnemonic(mnemo);
        mi.setAccelerator(KeyStroke.getKeyStroke(skrot));
        mi.addActionListener(al);
        return mi;
    }
   
}
