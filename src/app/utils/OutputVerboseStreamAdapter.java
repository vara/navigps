/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;

/**
 *
 * @author vara
 */
public abstract class OutputVerboseStreamAdapter implements OutputVerboseStream{

    private boolean displayTime = false;

    protected String getTime(){
        Date date = new Date();
        return "["+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"]";
    }

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
    @Override
    public void setTimeEnabled(boolean val){
        displayTime = val;
    }
    @Override
    public boolean isTimeEnabled(){
        return displayTime;
    }
}
