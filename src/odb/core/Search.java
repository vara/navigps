package odb.core;

import java.util.Vector;
import bridge.ODBridge;
import odb.utils.Constants;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

/**
 *
 * @author ACME
 */
public class Search implements ODBridge {

    private ODB odb = null;
    private float wspX;
    private float wspY;

    /**
     *
     * @return
     */
    @Override
    public Vector<String> getCategories() {
        odb = Constants.getDbConnection();
        Vector<String> v = new Vector<String>();
        if (odb != null) {
            Objects categories = odb.getObjects(Category.class);
            if (!categories.isEmpty()) {
                while (categories.hasNext()) {
                    Category c = (Category) categories.next();
                    v.add((String) c.getName());
                }
            } else {
                System.err.println("No categories avaliable!");
            }

        } else {
            System.err.println("No database initialized!");
        }

        return v;
    }

    /**
     *
     * @param category
     * @return
     */
    @Override
    public Vector<String> getSubcategories(String category) {
        odb = Constants.getDbConnection();
        Vector<String> v = new Vector<String>();
        if (odb != null) {
            IQuery query1 = new CriteriaQuery(Category.class, Where.equal("name", category));
            Objects categories = odb.getObjects(query1);
            if (!categories.isEmpty()) {
                Category c = (Category) categories.getFirst();
                if (!c.getSubcategories().isEmpty() && c.getSubcategories() != null) {
                    for (Object obj : c.getSubcategories()) {
                        Subcategory sub = (Subcategory) obj;
                        v.add((String) sub.getName());
                    }
                } else {
                    System.err.println("Subcategories empty or null!");
                }

            } else {
                System.err.println("Category doesn't exist!");
            }

        } else {
            System.err.println("No database initialized");
        }
        return v;
    }

    /**
     *
     * @param category
     * @return
     */
    @Override
    public Vector<String> getSubcategories(Vector<String> category) {
        Vector<String> v = new Vector<String>();
        for (String string : category) {
            Vector<String> tmpVec = getSubcategories(string);
            if (!tmpVec.isEmpty()) {
                v.addAll(tmpVec);
            }
        }
        return v;
    }

    /**
     *
     * @param category
     * @param x
     * @param y
     * @param radius
     * @return
     */
    @Override
    public Vector<ServiceCore> searchCategoryRadius(Vector category, double x, double y, double radius) {
        Vector v = new Vector();
        odb = Constants.getDbConnection();
        if (odb != null) {
            for (int i = 0; i < category.size(); i++) {
                IQuery query1 = new CriteriaQuery(ServiceDescription.class, Where.equal("category.name", category.get(i)));
                Objects res = odb.getObjects(query1);
                while (res.hasNext()) {
                    ServiceDescription obj = (ServiceDescription) res.next();
                    ServiceAttributes sa = obj.getServiceCore().getServiceAttributes();
                    if (Math.pow(sa.getX() - x, 2) + Math.pow(sa.getY() - y, 2) <= Math.pow(radius, 2)) {
                        v.add(obj.getServiceCore());
                    }
                }
            }
        } else {
            System.err.println("No database initialized");
        }
        return v;
    }
}
