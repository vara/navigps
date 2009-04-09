/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.utils;

import java.io.PrintStream;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class Console extends OutputVerboseStreamAdapter{
    
    public static PrintStream out;    
    public static PrintStream err;

    static {
            out = System.out;
            err = System.err;
    }

    /**
     *
     * @param text
     */
    @Override
    public void outputVerboseStream(String text){
        out.println(text);
    }
    /**
     *
     * @param text
     */
    @Override
    public void outputErrorVerboseStream(String text) {
        err.println(text);
    }
    /**
     *
     * @return
     */
    @Override
    public PrintStream getOutputStream(){
        return out;
    }

    /**
     *
     * @return
     */
    @Override
    public PrintStream getErrOutputStream(){
        return err;
    }
}
