package app.database.odb.core;

import java.util.Vector;
import app.database.odb.bridge.ODBBridge;
import app.database.odb.utils.Constants;
import org.neodatis.odb.ODB;
import org.neodatis.odb.OID;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

/**
 *
 * @author ACME
 */
public class Search implements ODBBridge {    
    private float wspX;
    private float wspY;

    /**
     *
     * @return
     */
    @Override
    public Vector<String> getCategories() {

        Vector<String> v = new Vector<String>();

        ODB odb = null;
        try{
            odb = Constants.getDbConnection();
        }catch(NullPointerException ex){
            System.err.println(""+ex.getMessage());
            return v;
        }
        
        Objects categories = odb.getObjects(Category.class);
        if (!categories.isEmpty()) {
            while (categories.hasNext()) {
                Category c = (Category) categories.next();
                v.add((String) c.getName());
            }
        } else {
            System.err.println(getClass().getCanonicalName()+" No categories avaliable!");
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
        
        Vector<String> v = new Vector<String>();

        ODB odb = null;
        try{
            odb = Constants.getDbConnection();
        }catch(NullPointerException ex){
            System.err.println(""+ex.getMessage());
            return v;
        }
        
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
                System.err.println(getClass().getCanonicalName()+" Subcategories empty or null!");
            }

        } else {
            System.err.println(getClass().getCanonicalName()+" Category doesn't exist!");
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

        ODB odb = null;
        try{
            odb = Constants.getDbConnection();
        }catch(NullPointerException ex){
            System.err.println(""+ex.getMessage());
            return v;
        }
        
        for (int i = 0; i < category.size(); i++) {
            IQuery query1 = new CriteriaQuery(ServiceDescription.class, Where.equal("category.name", category.get(i)));
            Objects res = odb.getObjects(query1);
            while (res.hasNext()) {
                Object object = res.next();
                ServiceDescription obj = (ServiceDescription) object;
                ServiceAttributes sa = obj.getServiceCore().getServiceAttributes();
                if (Math.pow(sa.getX() - x, 2) + Math.pow(sa.getY() - y, 2) <= Math.pow(radius, 2)) {
                    ServiceCore sc = obj.getServiceCore();
                    OID oid = odb.getObjectId(obj.getServiceCore());
                    sc.setOID(oid);
                    v.add(sc);
                }
            }
        }
        return v;
    }
}
