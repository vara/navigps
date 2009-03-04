/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package odb.test;

import java.util.Date;
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

        ServiceAttributes servicae = new ServiceAttributes();
        servicae.setX((float) Math.random());
        servicae.setY((float) Math.random());
        ServiceDescription sdx = new ServiceDescription("indexer", "indexer", "indexer", c, s, "indexer");
        ServiceCore sca = new ServiceCore(servicae, sdx);
        sca.getServiceAttributes().setServiceCore(sca);
        sca.getServiceDescription().setServiceCore(sca);
        odb.store(c);
        odb.store(s);
        odb.store(sca);
        odb.commit();

        String[] fieldNamesss = {"name"};
        String[] fieldNames = {"serviceNumber","serviceName","serviceStreet","additionaInfo"};
        String[] fieldNamess = {"x", "y"};
        odb.getClassRepresentation(ServiceDescription.class).addUniqueIndexOn("serviceD-index", fieldNames, true);
        odb.getClassRepresentation(ServiceAttributes.class).addUniqueIndexOn("serviceA-index", fieldNamess, true);
        odb.getClassRepresentation(Category.class).addUniqueIndexOn("CAT-index", fieldNamesss, true);

        Date start = new Date();
        for (int i = 0; i < 1000; i++) {
            ServiceAttributes service = new ServiceAttributes();
            service.setX(i);
            service.setY(i);
            ServiceDescription sd = new ServiceDescription(String.valueOf(i), String.valueOf(i), String.valueOf(i), c, s, String.valueOf(i));
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
