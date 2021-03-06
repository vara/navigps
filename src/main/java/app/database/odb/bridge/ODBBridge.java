package app.database.odb.bridge;

import java.util.Vector;
import app.database.odb.core.ServiceCore;

/**
 *
 * @author ACME
 */
public interface ODBBridge {
    /**
     *
     * @param category
     * @param x
     * @param y
     * @param radius
     * @return
     */
    public Vector<ServiceCore> searchCategoryRadius(Vector category, double x, double y, double radius);       //dla danej kategorii
    /**
     *
     * @return
     */
    public Vector<String> getCategories();                                                          //zwraca liste kategorii
    /**
     *
     * @param category
     * @return
     */
    public Vector<String> getSubcategories(String category);                                                       //lista podkategorii dla danej kategorii
    /**
     *
     * @param category
     * @return
     */
    public Vector<String> getSubcategories(Vector<String> category);
}
