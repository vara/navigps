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

    /**
     *
     * @return
     */
    protected String getTime(){
        Date date = new Date();
        return "["+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"]";
    }

    /**
     *
     */
    public OutputVerboseStreamAdapter(){
    }

    /**
     *
     * @param text
     */
    @Override
    public void outputVerboseStream(String text) {}

    /**
     *
     * @param text
     */
    @Override
    public void outputErrorVerboseStream(String text) {}

    /**
     *
     * @return
     */
    @Override
    public PrintStream getOutputStream(){
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public PrintStream getErrOutputStream(){
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public PrintWriter getErrOutputWriter(){
        return null;
    }
    /**
     *
     * @return
     */
    @Override
    public PrintWriter getOutputWriter(){
        return null;
    }
    /**
     *
     * @param val
     */
    @Override
    public void setTimeEnabled(boolean val){
        displayTime = val;
    }
    /**
     *
     * @return
     */
    @Override
    public boolean isTimeEnabled(){
        return displayTime;
    }
}
