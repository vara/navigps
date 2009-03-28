/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.utils;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *
 * @author vara
 */
public interface OutputVerboseStream {

    /**
     *
     * @param text
     */
    void outputVerboseStream(String text);
    /**
     *
     * @param text
     */
    void outputErrorVerboseStream(String text);
    /**
     *
     * @return
     */
    PrintStream getOutputStream();
    /**
     *
     * @return
     */
    PrintStream getErrOutputStream();
    /**
     *
     * @return
     */
    PrintWriter getOutputWriter();
    /**
     *
     * @return
     */
    PrintWriter getErrOutputWriter();
    /**
     *
     * @param val
     */
    void setTimeEnabled(boolean val);
    /**
     *
     * @return
     */
    boolean isTimeEnabled();
}
