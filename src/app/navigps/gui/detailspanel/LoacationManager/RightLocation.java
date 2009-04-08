/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.detailspanel.LoacationManager;

import app.navigps.gui.detailspanel.RoundWindow;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 *
 * @author wara
 */
public class RightLocation extends LocationManager{

    private boolean needRevalidate = false;

    public RightLocation(RoundWindow rw){
        super(rw);
    }

    @Override
    public Rectangle updateLocation(Container src) {
        int x = src.getWidth()-root.getWidth();
        int y = (src.getHeight()-root.getHeight())>>1;
        int w = root.getWidth();
        int h = root.getHeight();
        return new Rectangle(x, y, w, h);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if(e.getButton()==MouseEvent.BUTTON1 && e.getX()<sensitiveMouseReaction){
            System.out.println("Resize X side "+getClass().getSimpleName());
            resizeWidthPanel = true;
        }else if(e.getButton()==MouseEvent.BUTTON1 && e.getY()<sensitiveMouseReaction){
            System.out.println("Resize Y side "+getClass().getSimpleName());
            resizeHeghtPanel = true;
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {

        if(needRevalidate || !root.isDynamicRevalidate()){
            root.revalidate();
            needRevalidate=false;
        }
        resizeWidthPanel = false;
        resizeHeghtPanel = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(resizeWidthPanel || resizeHeghtPanel){
            int width =0,height = 0;
            if(resizeWidthPanel){
                width = root.getWidth()-e.getX()+2;
                root.setSize(width, root.getHeight());
            }
            else{
                height = root.getHeight()-e.getY();
                root.setSize(root.getWidth(), height);
            }
            root.updatePosition();
            needRevalidate=true;
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if(e.getX()<sensitiveMouseReaction||e.getY()<sensitiveMouseReaction){
            root.setCursor(Cursor.getPredefinedCursor( e.getX()<sensitiveMouseReaction ?
                                      Cursor.W_RESIZE_CURSOR:Cursor.N_RESIZE_CURSOR));
            cursorChanged = true;
        }else if(cursorChanged && !resizeWidthPanel&&!resizeHeghtPanel){
            root.setCursor(Cursor.getDefaultCursor());
        }
    }
}
