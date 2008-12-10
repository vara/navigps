/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui;

import app.utils.OutputVerboseStream;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JToggleButton;

/**
 * Created on 2008-12-09, 00:32:18
 * @author vara (idea Darryl.Burke)
 * < downloaded from http://forums.sun.com/thread.jspa?forumID=57&threadID=5306810>
 */
public class RotatedButton extends JButton implements MouseListener{


        private int width = 20;
        private XButton template;
        private boolean clockwise;
        private BufferedImage buttonImage = null;

        private float buttonAlpha = 0.0f;
        private boolean visibleAction = false;

        private OutputVerboseStream verboseStream = null;

        private int roundX=20;
        private int roundY=20;
        private RoundRectangle2D.Double visibleRect;

        public RotatedButton(String text, boolean clockwise,Dimension size,OutputVerboseStream l) {
            
            super.setVisible(false);

            verboseStream = l;
            template = new XButton(text);
            this.clockwise = clockwise;

            template.setSize(width,size.height);
            setSize(width,size.height);

            setLocation(size.width-width,0);            
            setOpaque(false);
            setBorder(new OvalBorder(roundX,roundY));
            addMouseListener(this);

            visibleRect = new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),roundX,roundY);
        }

        @Override
        public void paint(Graphics g) {
            getVerboseStream().outputVerboseStream("Rotated Button [Paint]");
            if (buttonImage == null || buttonImage.getWidth() != getWidth() ||
                    buttonImage.getHeight() != getHeight()) {
                buttonImage = getGraphicsConfiguration().
                        createCompatibleImage(getWidth(), getHeight());
            }
            Graphics gButton = buttonImage.getGraphics();
            gButton.setClip(visibleRect);
            super.paint(gButton);
            gButton.dispose();
            Graphics2D g2  = (Graphics2D)g.create();
            AlphaComposite newComposite =
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER,buttonAlpha);            
            g2.setComposite(newComposite);
            g2.setClip(visibleRect);
            g2.drawImage(buttonImage, 0, 0, null);
            g2.dispose();
        }

        @Override
        protected void paintComponent(Graphics g) {
            getVerboseStream().outputVerboseStream("Rotated Button [paintComponent]");
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

        public OutputVerboseStream getVerboseStream(){
            return verboseStream;
        }

        public void setAlpha(float val){
            if(val<0) val=0.0f; if(val>1) val=1.0f;
            buttonAlpha = val;
        }
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
        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}


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
