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
        
    /**
     *
     * @param p
     * @param val
     */
    public Parameter(String p,String val){
        param = p;
        value.add(val);
    }
    /**
     *
     * @param p
     */
    public Parameter(String p){
        param = p;
    }
    /**
     *
     * @return
     */
    public String getParam() {
        return param;
    }
    /**
     *
     * @return
     */
    public char getCharParam(){
        char [] charArr = param.toCharArray();
        char sumChar=0;
        for (char c : charArr) {
            sumChar+=c;
        }
        return sumChar;
    }
    /**
     *
     * @param param
     */
    public void setParam(String param) {
        this.param = param;
    }

    /**
     *
     * @param index
     * @return
     */
    public String getValue(int index){

        try{
            return value.get(index); 
        }catch(Exception e){
            return null;
        }
    }

    /**
     *
     * @param value
     */
    public void addValue(String value) {
        this.value.add(value);
    }
    /**
     *
     * @return
     */
    public Vector<String> getVecValue(){
        return value;
    }
}
