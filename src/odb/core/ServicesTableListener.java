package odb.core;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author praise
 */
public class ServicesTableListener implements TableModelListener {

    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            System.out.println("Table listener UPDATE at cloumn: " +e.getColumn()+" row: "+e.getFirstRow());

            switch(e.getColumn()) {
                case 0:
                    System.out.println("name change");
                    break;
                case 1:
                    System.out.println("category change");
                    break;
                case 2:
                    System.out.println("subcategory change");
                    break;
                case 3:
                    System.out.println("X change");
                    break;
                case 4:
                    System.out.println("Y change");
                    break;
            }
            
        }
    }
}
