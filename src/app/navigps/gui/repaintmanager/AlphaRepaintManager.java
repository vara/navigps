/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.repaintmanager;

import app.navigps.gui.detailspanel.AlphaJPanel;
import java.awt.Container;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.RepaintManager;

/**
 *
 * @author wara
 */
public class AlphaRepaintManager extends RepaintManager {

    @Override
    public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {

        Rectangle dirtyRegion = getDirtyRegion(c);
        if (dirtyRegion.width == 0 && dirtyRegion.height == 0) {
            int lastDeltaX = c.getX();
            int lastDeltaY = c.getY();
            Container parent = c.getParent();
            while (parent instanceof JComponent) {
                if (!parent.isVisible() || (parent.getPeer() == null)) {
                    return;
                }
                if (parent instanceof AlphaJPanel &&
                        (((AlphaJPanel)parent).getAlpha() < 1f ||
                        !parent.isOpaque())) {
                    x += lastDeltaX;
                    y += lastDeltaY;
                    lastDeltaX = lastDeltaY = 0;
                    c = (JComponent)parent;
                }
                lastDeltaX += parent.getX();
                lastDeltaY += parent.getY();
                parent = parent.getParent();
            }
        }
        super.addDirtyRegion(c, x, y, w, h);
    }
}//AlphaRepaintManager
