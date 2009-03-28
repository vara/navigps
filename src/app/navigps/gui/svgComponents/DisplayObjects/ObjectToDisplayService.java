package app.navigps.gui.svgComponents.DisplayObjects;

import app.navigps.utils.NaviPoint;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;

/**
 *
 * @author wara
 */
public interface ObjectToDisplayService extends ObjectServiceInterface{

    public ImageIcon getIcon();
    public String getServiceName();
    public String getDescription();
    public String getCategory();
    public NaviPoint getCoordinate();
    public void transformCoordinate(AffineTransform at);    
}
