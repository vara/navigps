/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app;

/**
 *
 * @author vara
 */
public class Parameter {
        
    private String param;
    private String value;
        
    public Parameter(String p,String val){
        param = p;
        value = val;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
