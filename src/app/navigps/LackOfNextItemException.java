/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps;

/**
 *
 * @author vara
 */
public class LackOfNextItemException extends Exception{

    /**
     *
     */
    public LackOfNextItemException(){
	super();
    }
    /**
     *
     * @param s
     */
    public LackOfNextItemException(String s){
	super(s);
    }
}
