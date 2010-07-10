package app.navigps.gui.detailspanel.LoacationManager;

import app.navigps.gui.detailspanel.*;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 *
 * @author wara
 */
public class LeftLocation extends LocationManager{

    private boolean needRevalidate = false;

    public LeftLocation(RoundWindow rw){
        super(rw);
    }

    @Override
    public Rectangle updateLocation(Container src) {                    
        return new Rectangle(src.getLocation().x,
                            (src.getHeight()-root.getHeight())>>1,
                            root.getWidth(),
                            root.getHeight());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int xr = root.getWidth()-e.getX();
        if(e.getButton()==MouseEvent.BUTTON1 && xr<sensitiveMouseReaction){
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
            int height = 0;
            if(resizeWidthPanel){
                root.setSize(e.getX()+4, root.getHeight());
            }
            else{
                height =  root.getHeight()-e.getY();
                root.setSize(root.getWidth(), height);
            }
            root.updatePosition();
            needRevalidate=true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {        
        int xr = root.getWidth()-e.getX();
        //System.out.println("xr: "+xr);
        if(xr<sensitiveMouseReaction||e.getY()<sensitiveMouseReaction){
            root.setCursor(Cursor.getPredefinedCursor( xr<sensitiveMouseReaction ?
                                      Cursor.W_RESIZE_CURSOR:Cursor.N_RESIZE_CURSOR));
            cursorChanged = true;
        }else if(cursorChanged && !resizeWidthPanel&&!resizeHeghtPanel){
            root.setCursor(Cursor.getDefaultCursor());
        }
    }
}
