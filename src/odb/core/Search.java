package odb.core;

import config.DataBaseConfig;
import java.util.Vector;
import odb.inter.ODBridge;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

/**
 *
 * @author praise
 */
public class Search implements ODBridge {

    private float wspX;
    private float wspY;
    
    public Search() {
        
    }

    public Vector getCategories() {
        
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ServiceDescription[] searchRadius(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ServiceDescription[] searchCategoryRadius(Category category, float x, float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector getSubcategories(Category category) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ServiceDescription getSingleTest() {
        ServiceDescription sub;
        ODB odb = null;
        odb = ODBFactory.open(DataBaseConfig.getDefaultDatabasePath()+"neodatis.odb");
        

        IQuery query = new CriteriaQuery(ServiceDescription.class);

        Objects subcat = odb.getObjects(query);
        sub = (ServiceDescription)subcat.getFirst();

        return sub;
    }
}
