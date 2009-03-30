/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.ArgumentsStartUp.core;

/**
 *
 * @author wara
 */
public interface ParameterCore {
    String getOption();
    String getOptionDescription();
    int getOptionValuesLength();
    void handleOption(String[] values);
}
