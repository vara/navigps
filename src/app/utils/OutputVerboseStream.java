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
public interface OutputVerboseStream {

    void outputVerboseStream(String text);
    void outputErrorVerboseStream(String text);
    PrintStream getOutputStream();
    PrintStream getErrOutputStream();
    PrintWriter getOutputWriter();
    PrintWriter getErrOutputWriter();
    void setTimeEnabled(boolean val);
    boolean isTimeEnabled();
}
