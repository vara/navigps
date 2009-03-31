/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.searchServices.swing;

import app.database.odb.core.ServiceCore;
import java.util.Vector;
import javax.swing.AbstractListModel;

/**
 *
 * @author wara
 */
public class ServiceListModel extends AbstractListModel{

    private Vector<ServiceCore> vServices = new Vector<ServiceCore>();

    public ServiceListModel(){
    }

    public ServiceListModel(Vector<ServiceCore> vServiceCore){
        setAllServices(vServiceCore);
    }

    @Override
    public int getSize() {
        return vServices.size();
    }

    @Override
    public Object getElementAt(int index) {
        return vServices.get(index).getServiceDescription().getServiceName();
    }



    public void addServices(Vector<ServiceCore> vServiceCore){
        int lastIndex = vServices.size()-1;
        vServices.addAll(vServiceCore);
        fireContentsChanged(this, lastIndex,vServices.size()-1);
    }

    public void setAllServices(Vector<ServiceCore> vServiceCore){
        vServices.clear();
        vServices.addAll(vServiceCore);
        fireContentsChanged(this, 0,vServices.size()-1);
    }

    public void removeElementAt(int index) {
       vServices.remove(index);
       fireIntervalRemoved(this, index, index);
    }

    public void removeAll(){
        int index = vServices.size()-1;
        vServices.clear();
        if(index >=0)
            fireIntervalRemoved(this, 0, index);
    }

    public boolean isEmpty(){
        System.out.println("is Empty "+vServices.isEmpty());
        return vServices.isEmpty();
    }
}
