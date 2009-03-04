/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.gui.detailspanel.AlphaJPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author wara
 */
public class BumpArea extends AlphaJPanel{

    private Color [] colors = {new Color(255,255,255),new Color(30,30,30)};
    private int gap = 3;
    private float dash1[] = {1.0f,gap};
    private BasicStroke dashed = new BasicStroke(1.0f,
                                      BasicStroke.CAP_BUTT,
                                      BasicStroke.JOIN_BEVEL,
                                      1.0f, dash1, 0.0f);
    public BumpArea(){
        setOpaque(false);
        setAlpha(0.6f);
        //setBorder(new EmptyBorder(4,4,4,4));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g.create();
        Insets ins = getInsets();

        int width = getWidth()-ins.right;
        int height = getHeight()-ins.bottom;
        int x = ins.left;
        int y = ins.top;
        System.err.println("insets "+ins);
        g2.setStroke(dashed);
        for (int i = x; i <=width; i++) {
            int offset = i % (gap+1);
            g2.setColor(colors[i%2]);
            g2.drawLine(i,y+offset,i, height);
        }
        g2.dispose();
    }
}
