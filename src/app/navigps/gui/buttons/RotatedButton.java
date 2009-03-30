/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.buttons;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JToggleButton;

/**
 * Created on 2008-12-09, 00:32:18
 * @author vara (idea Darryl.Burke)
 * < downloaded from http://forums.sun.com/thread.jspa?forumID=57&threadID=5306810>
 */
public class RotatedButton extends RoundButton{


        private XButton template;
        private boolean clockwise;
        
        /**
         *
         * @param text
         * @param clockwise
         * @param size
         */
        public RotatedButton(String text, boolean clockwise,Dimension size) {
            
            super(text);
            template = new XButton(text);
            this.clockwise = clockwise;
            template.setSize(size);
            setPreferredSize(size);
            //setLocation(size.width-width,0);
        }       
        /**
         *
         * @param text
         * @param clockwise
         * @param size
         * @param round
         */
        public RotatedButton(String text, boolean clockwise,Dimension size,double round) {
            this(text, clockwise, size);
            setRound(round, round);
        }
        /**
         *
         * @param g
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //getVerboseStream().outputVerboseStream("Rotated Button [paintComponent]");
            Graphics2D g2 = (Graphics2D) g.create();
            Dimension d = getSize();
            template.setSize(d.height, d.width);
            
            if (clockwise) {
                g2.rotate(Math.PI / 2.0);
                g2.translate(0, -getSize().width);
            } else {
                g2.translate(0, getSize().height);
                g2.rotate(- Math.PI / 2.0);
            }
            template.setSelected(this.getModel().isPressed());
            template.paintComponent(g2);
            g2.dispose();
        }
        /*
        public void showToggleButton(){

            if(buttonAlpha!=1.0f && !visibleAction)
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getVerboseStream().outputVerboseStream("showToggleButton thread");
                        visibleAction = true;
                        setVisible(true);
                        for (float i = 0; i < 1.05; i+=0.05) {
                            
                                setAlpha(i);
                                repaint();
                            try {Thread.sleep(100);} catch (InterruptedException ex) {}
                        }
                        visibleAction = false;
                    }
                })).start();
        }

        public void hideToggleButton(){

            final Runnable run = new Runnable() {
                @Override
                public void run() {
                    getVerboseStream().outputVerboseStream("hideToggleButton thread");
                    visibleAction = true;
                    for (float i = buttonAlpha; i >0; i -= 0.05) {
                        setAlpha(i);
                        repaint();
                        try{Thread.sleep(100);} catch (InterruptedException ex) {}
                    }
                    setVisible(false);
                    visibleAction=false;
                }
            };
            if(buttonAlpha!=0.0f && !visibleAction)
                (new Thread(run)).start();

        }
         */

        private class XButton extends JToggleButton {

            public XButton(String text) {
                 super(text);
            }
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g2);
            }
        }
}
