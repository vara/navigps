package app.ArgumentsStartUp.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class ParametersContainer {

    private static HashMap <String,AbstractParameter> mapOfParameters = new LinkedHashMap<String,AbstractParameter>();

    public static void putParameter(AbstractParameter ap){

        if(ap!=null)
            mapOfParameters.put(ap.getOption(), ap);
    }

    public static void putParameter(Vector<AbstractParameter> vap){
        if(vap != null){
            for (AbstractParameter ap : vap) {
                putParameter(ap);
            }
        }
    }

    public static AbstractParameter getParameter(String symbol){
        return mapOfParameters.get(symbol);
    }

    public static Vector<AbstractParameter> getAllParameters(){
        Vector<AbstractParameter> allParams = new Vector<AbstractParameter>();
        Set<Map.Entry<String, AbstractParameter>> set = mapOfParameters.entrySet();
        for (Map.Entry<String, AbstractParameter> me : set) {
            allParams.add(me.getValue());
        }
        return allParams;
    }

    public static boolean isEmpty(){
        return mapOfParameters.isEmpty();
    }
}
