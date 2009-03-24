/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import app.utils.NaviPoint;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;

/**
 *
 * @author wara
 */
public interface ObjectToDisplayService {

    public ImageIcon getIcon();
    public String getServiceName();
    public String getDescription();
    public String getCategory();
    public NaviPoint getCoordinate();
    public void transformCoordinate(AffineTransform at);
}
