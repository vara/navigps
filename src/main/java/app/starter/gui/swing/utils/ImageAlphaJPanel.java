/*
 * ImageJAlphaPanel.java
 *
 * Created on 2009-04-01, 14:56:34
 */

package app.starter.gui.swing.utils;

import app.navigps.gui.detailspanel.AlphaJPanel;
import app.navigps.utils.GraphicsUtilities;
import app.starter.gui.RunApp;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class ImageAlphaJPanel extends AlphaJPanel{
    
    private BufferedImage orginalImg = null;
    private BufferedImage renderImage = null;

    private String imgName;

    public ImageAlphaJPanel(String imgName){
        this.imgName = imgName;
        orginalImg = loadimage(imgName);
        setOpaque(false);

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2= (Graphics2D)g;
        super.paintComponent(g2);

        int width = getWidth();
        int height = getHeight();
        if(renderImage==null || renderImage.getWidth() != width || renderImage.getHeight()!=height){
            //Dimension newSize = calculateimageSize(orginalImg.getWidth(), orginalImg.getHeight());
            renderImage = GraphicsUtilities.createThumbnail(orginalImg,getWidth(),getHeight());
        }
        //int xpos = Math.abs(renderImage.getWidth()-width)/2;
        //System.out.println("xpos "+xpos);
        g2.setComposite(AlphaComposite.SrcOver);
        g2.drawImage(renderImage, 0, 0, null);
    }

    public static BufferedImage loadimage(String name){

        URL href = RunApp.class.getResource("resources/graphics/"+name);
        BufferedImage tmpBi = null;
        try {
            tmpBi = GraphicsUtilities.loadCompatibleImage(href);

        } catch (IOException ex) {
            System.err.println(""+ex);
        }

        return tmpBi;
    }

    protected Dimension calculateimageSize(int imageW,int imageH){
        Dimension size = new Dimension(getWidth(),getHeight());
        return size;
    }
}
