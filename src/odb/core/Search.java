package odb.core;

import java.util.Vector;
import bridge.ODBridge;
import odb.utils.Constants;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.w3c.dom.Element;

/**
 *
 * @author praise
 */
public class Search implements ODBridge {

    private ODB odb = null;
    private float wspX;
    private float wspY;

    public Vector<Element> searchCategoryRadius(String category, float x, float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector<String> getCategories() {
        odb = Constants.getDbConnection();
        Vector v = new Vector();
        Objects categories = odb.getObjects(Category.class);

        if (categories.isEmpty()) {
            v.add(null);
        } else {
            while (categories.hasNext()) {
                Category c = (Category) categories.next();
                v.add(c.getName());
            }
        }
        return v;
    }

    public Vector<String> getSubcategories(String category) {
        odb = Constants.getDbConnection();
        Vector v = new Vector();

        IQuery query1 = new CriteriaQuery(Category.class, Where.equal("name", category));
        Objects categories = odb.getObjects(query1);
        if (categories.isEmpty()) {
            v.add(null);
        } else {
            Category c = (Category) categories.getFirst();

            if (c.getSubcategories().isEmpty()) {
                v.add(null);
            } else {
                for (Object obj : c.getSubcategories()) {
                    Subcategory sub = (Subcategory) obj;
                    v.add(sub.getName());
                }
            }
        }
        return v;
    }
}
