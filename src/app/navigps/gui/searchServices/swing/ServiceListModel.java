package app.navigps.gui.searchServices.swing;

import app.database.odb.core.ServiceCore;
import app.navigps.gui.svgComponents.DisplayObjects.ObjectService;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.AbstractListModel;
import org.neodatis.odb.OID;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class ServiceListModel extends AbstractListModel{

    private ArrayList<ServiceCore> arrayServices = new ArrayList<ServiceCore>();

    public ServiceListModel(){
    }

    public ServiceListModel(Vector<ServiceCore> vServiceCore){
        setAllServices(vServiceCore);
    }

    @Override
    public int getSize() {
        return arrayServices.size();
    }

    @Override
    public Object getElementAt(int index) {
        if(index>=0 && index<arrayServices.size())
            return arrayServices.get(index);
        return "Bad array index "+index;
    }
    
    public void addServices(Vector<ServiceCore> vServiceCore){
        if(vServiceCore == null) return;
        int lastIndex = arrayServices.size()-1;
        for (ServiceCore sc : vServiceCore) {
            if(!arrayServices.contains(sc)){
                arrayServices.add(sc);
            }
        }
        int currentIndex = arrayServices.size()-1;
        fireContentsChanged(this, lastIndex,currentIndex);
    }

    public void setAllServices(Vector<ServiceCore> vServiceCore){
        arrayServices.clear();
        arrayServices.addAll(vServiceCore);
        fireContentsChanged(this, 0,arrayServices.size()-1);
    }

    public void removeElement(Object o){
        if(o instanceof ServiceCore){
            for (int i = 0; i < arrayServices.size(); i++) {
                if(getElementAt(i).equals(o)){
                    removeElementAt(i);
                }
            }
        }else if(o instanceof ObjectService){
            removeElement( ((ObjectService)o).getOID());
        }
    }

    public void removeElement(OID o){
        for (int i = 0; i < arrayServices.size(); i++) {
            if( ((ServiceCore)getElementAt(i)).getOID().equals(o)){
                removeElementAt(i);
            }
        }
    }

    public void removeElementAt(int index) {
        try{
            arrayServices.remove(index);
            fireContentsChanged(this, index, index);
        }catch(IndexOutOfBoundsException e){}
    }

    public void removeAll(){
        int index = arrayServices.size()-1;
        arrayServices.clear();
        if(index >=0)
            fireContentsChanged(this, 0, index);
    }

    public boolean isEmpty(){
        return arrayServices.isEmpty();
    }

    @Override
    public String toString() {
        return arrayServices.toString();
    }
}
