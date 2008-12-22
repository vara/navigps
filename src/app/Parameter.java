/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app;

import java.util.Vector;

/**
 *
 * @author vara
 */
public class Parameter {
        
    private String param;
    private Vector <String> value = new Vector<String>(3,1);
        
    public Parameter(String p,String val){
        param = p;
        value.add(val);
    }
    public Parameter(String p){
        param = p;
    }
    public String getParam() {
        return param;
    }
    public char getCharParam(){
        char [] charArr = param.toCharArray();
        char sumChar=0;
        for (char c : charArr) {
            sumChar+=c;
        }
        return sumChar;
    }
    public void setParam(String param) {
        this.param = param;
    }

    public String getValue(int index){

        try{
            return value.get(index); 
        }catch(Exception e){
            return null;
        }
    }

    public void addValue(String value) {
        this.value.add(value);
    }
    public Vector<String> getVecValue(){
        return value;
    }
}
