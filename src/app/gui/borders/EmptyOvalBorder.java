/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.borders;

import java.awt.Component;
import java.awt.Graphics;

/**
 * Created on 2008-12-15, 23:17:16
 * @author vara
 */
public class EmptyOvalBorder extends OvalBorder{

    public EmptyOvalBorder(){
    }
    public EmptyOvalBorder(double recW,double recH){
        super(recH, recH);
    }
    @Override
     public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        
     }
}
