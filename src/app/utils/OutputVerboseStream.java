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
public interface OutputVerboseStream {

    void outputVerboseStream(String text);
    void outputErrorVerboseStream(String text);
    PrintWriter getOutputStream();
    PrintWriter getErrOutputStream();
}
