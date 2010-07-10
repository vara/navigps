package app.navigps.gui.svgComponents.DisplayObjects;

import app.database.odb.core.ServiceCore;
import org.neodatis.odb.OID;

/**
 *
 * @author wara
 */
public interface ObjectServiceInterface {
    public OID getOID();
    public void updateService(ServiceCore sc);
}
