/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.ArgumentsStartUp.Exception;

/**
 *
 * @author wara
 */
public class ValidationException extends Exception{

    public ValidationException(){
        super();
    }
    public ValidationException(String str){
        super(str);
    }
}
