/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.ArgumentsStartUp.Exception;

/**
 *
 * @author wara
 */
public class ImmediatelyExitProgram extends Exception{
    public ImmediatelyExitProgram(){
        super();
    }
    public ImmediatelyExitProgram(String str){
        super(str);
    }
}
