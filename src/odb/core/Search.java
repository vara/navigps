package odb.core;

import java.util.Vector;
import odb.inter.ODBridge;

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

    public SubElementService[] searchCategoryRadius(String category, float x, float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SubElementService[] searchRadius(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
