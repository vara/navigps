/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.starter.gui.swing;

import app.ArgumentsStartUp.core.AbstractParameter;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author wara
 */
public class ParameterTableModel extends AbstractTableModel{

    private Vector colName = new Vector();
    private Vector rowData = new Vector();

    public ParameterTableModel(String [] colName)
    {
        initColName(colName);
    }

    private void initColName(String [] colName)
    {
        for(int i=0;i<colName.length;i++)
            this.colName.add(i, colName[i]);
    }

    @Override
    public int getRowCount() {
       return rowData.size();
    }

    @Override
    public int getColumnCount() {
        return colName.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return (((Vector) rowData.elementAt(rowIndex)).elementAt(columnIndex));        
    }

    @Override
    public String getColumnName(int col){
        if (col<=getColumnCount()){
            //System.out.println(colName.elementAt(col));
            return (String)colName.elementAt(col);
        }
        return "";
    }

    @Override
    public Class getColumnClass(int c){        
	    return getValueAt(0,c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col){   
        return true;//getValueAt(row,col) instanceof SingleValueParameter;
    }

    public void insertRow(AbstractParameter param){

        Vector data = new Vector();
        data.addElement(param.getOption());
        data.addElement(new ObjectParameterForJTable(param));
        
        rowData.add(data);
        fireTableDataChanged();
    }

    public void insertRow(Vector<AbstractParameter> paramas){
        if(paramas.firstElement() instanceof AbstractParameter){
            for(int i=0;i<paramas.size();i++)
                insertRow((AbstractParameter)paramas.get(i));
        }
    }

    public Object getObject(Object obj){
	    for(int i=0;i<getRowCount();i++)
		    for(int j=0;j<getColumnCount();j++){
			    AbstractParameter tmp = (AbstractParameter)getValueAt(i, j);
			    if(tmp.getOption().equals( ((AbstractParameter)obj).getOption() ))
					    return tmp;
		    }
	    return null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println(getClass().getCanonicalName()+" method Finalize !");
    }

    public void clear(){
        rowData.clear();
    }
}

