/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import java.io.PrintStream;

/**
 *
 * @author wara
 */
public class Console extends OutputVerboseStreamAdapter{
    public static PrintStream out = System.out;
    public static PrintStream err = System.err;
    @Override
    public void outputVerboseStream(String text){
        out.println(text);
    }
    @Override
    public void outputErrorVerboseStream(String text) {
        err.println(text);
    }
    @Override
    public PrintStream getOutputStream(){
        return out;
    }

    @Override
    public PrintStream getErrOutputStream(){
        return err;
    }
}
