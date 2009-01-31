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

    public SubElementService[] searchRadius(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SubElementService[] searchCategoryRadius(Category category, float x, float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector getSubcategories(Category category) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SubElementService getSingleTest() {
        SubElementService sub;
        ODB odb = null;
        odb = ODBFactory.open(DataBaseConfig.getDefaultDatabasePath()+"neodatis.odb");
        

        IQuery query = new CriteriaQuery(SubElementService.class);

        Objects subcat = odb.getObjects(query);
        sub = (SubElementService)subcat.getFirst();

        return sub;
    }
}
