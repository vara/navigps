/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *
 * @author vara
 */
public abstract class OutputVerboseStreamAdapter implements OutputVerboseStream{

    public OutputVerboseStreamAdapter(){
    }

    @Override
    public void outputVerboseStream(String text) {}

    @Override
    public void outputErrorVerboseStream(String text) {}

    @Override
    public PrintStream getOutputStream(){
        return null;
    }

    @Override
    public PrintStream getErrOutputStream(){
        return null;
    }

    @Override
    public PrintWriter getErrOutputWriter(){
        return null;
    }
    @Override
    public PrintWriter getOutputWriter(){
        return null;
    }
}
