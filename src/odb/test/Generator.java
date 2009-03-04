/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package odb.test;

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

    public Generator() {
        for (int i = 0; i < 10000; i++) {
            Category c = new Category(String.valueOf(i));
            Subcategory s = new Subcategory(c, String.valueOf(i));
            ServiceAttributes service = new ServiceAttributes();
            service.setX(i);
            service.setY(i);
            ServiceDescription sd = new ServiceDescription(String.valueOf(i), String.valueOf(i), String.valueOf(i), c, s, String.valueOf(i));
            ServiceCore sc = new ServiceCore(service, sd);
            sc.getServiceAttributes().setServiceCore(sc);
            sc.getServiceDescription().setServiceCore(sc);
            odb.store(sc);
        } odb.commit();
    }
}
