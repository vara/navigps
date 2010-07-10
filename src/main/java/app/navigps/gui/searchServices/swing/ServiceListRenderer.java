/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewClass.java
 *
 * Created on 2009-04-02, 15:43:51
 */

package app.navigps.gui.searchServices.swing;

import app.database.odb.core.ServiceCore;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class ServiceListRenderer extends JLabel implements ListCellRenderer{

    public ServiceListRenderer(){
        setOpaque(true);
    }
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        if(value instanceof ServiceCore){
            ServiceCore sc = (ServiceCore)value;
            String text = sc.getServiceDescription().getServiceName();
            text += " from "+sc.getServiceDescription().getCity();
            setText(text);
            String icoName = sc.getServiceDescription().getCategory().getName();            
            setIcon(ImageListForServices.getIcon(icoName, true));
        }else{
            setText(value.toString());
            System.err.println(getClass().getCanonicalName()+" [ Upss wrong renderer expected 'ServiceCore' clazz]");
        }

        if (isSelected) {
          setBackground(list.getSelectionBackground());
          setForeground(list.getSelectionForeground());
        } else {
          setBackground(new Color(255,255,255,0));
          setForeground(new Color(200,200,200));
        }
        return this;
    }
}
