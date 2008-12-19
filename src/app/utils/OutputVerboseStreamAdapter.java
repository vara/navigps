/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

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
    public PrintWriter getOutputStream(){
        return null;
    }

    @Override
    public PrintWriter getErrOutputStream(){
        return null;
    }
}
