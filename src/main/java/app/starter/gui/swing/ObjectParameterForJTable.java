/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.starter.gui.swing;

import app.ArgumentsStartUp.FileValueParameter;
import app.ArgumentsStartUp.NoValueParameter;
import app.ArgumentsStartUp.SizeValueParameter;
import app.ArgumentsStartUp.core.AbstractParameter;
import java.awt.Dimension;

/**
 *
 * @author wara
 */
public class ObjectParameterForJTable {

    private Boolean bValue = false;
    private String sValue = "";
    private Dimension dValue= null;

    private AbstractParameter abstractParam;

    public ObjectParameterForJTable(AbstractParameter ap){
        abstractParam = ap;
    }

    /**
     * @return the abstractParam
     */
    public AbstractParameter getAbstractParam() {
        return abstractParam;
    }

    public Object getValueForTable(){
        return checkParameter();
    }

    public void setValueForTable(Object obj){
        if(obj instanceof String){
            sValue = (String)obj;
        }else if(obj instanceof Dimension){
            dValue = (Dimension)obj;
        }else if(obj instanceof Boolean){
            bValue = (Boolean)obj;
        }
    }

    private Object checkParameter(){
        if(abstractParam instanceof SizeValueParameter){
            return dValue;
        }else if(abstractParam instanceof FileValueParameter){
            return sValue;
        }else if(abstractParam instanceof NoValueParameter){
            return bValue;
        }
        return null;
    }
}
