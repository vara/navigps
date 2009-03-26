package odb.test;

import java.util.Date;
import java.util.Vector;
import odb.core.Category;
import odb.core.ServiceAttributes;
import odb.core.ServiceCore;
import odb.core.ServiceDescription;
import odb.core.Subcategory;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

/**
 *
 * @author praise
 */
class Generator {

    private ODB odb = ODBFactory.open("./resources/odb/neodatis.odb");
    Category c = new Category("cat_test");
    Subcategory s = new Subcategory(c, "sub_test");

    public Generator() {

        c.setSubcategories(new Vector());
        c.addSubcategory(s);
        odb.store(c);
        odb.commit();
        
        Date start = new Date();
        for (int i = 0; i < 30000; i++) {
            ServiceAttributes service = new ServiceAttributes();
            service.setX(i);
            service.setY(i);
            ServiceDescription sd = new ServiceDescription(String.valueOf(i), String.valueOf(i), String.valueOf(i), c, s, String.valueOf(i),"City"+i);
            ServiceCore sc = new ServiceCore(service, sd);
            sc.getServiceAttributes().setServiceCore(sc);
            sc.getServiceDescription().setServiceCore(sc);
            odb.store(sc);
        }
        odb.commit();
        Date stop = new Date();
        System.out.println("Changes commited, execution time: " + (stop.getTime() - start.getTime()) / 1000 + " s");
    }
}
