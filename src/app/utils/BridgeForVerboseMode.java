/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import config.MainConfiguration;
import java.util.LinkedList;

/**
 *
 * @author vara
 */
public class BridgeForVerboseMode implements OutputVerboseStream{

    private LinkedList<OutputVerboseStream> updateComponets = 
			new LinkedList<OutputVerboseStream>();
        
    public BridgeForVerboseMode(){
	if(MainConfiguration.getMode())
	    addComponentsWithOutputStream(new OutputVerboseStreamToConsole());
    }
    
    @Override
    public void outputVerboseStream(String text){
	
	for (OutputVerboseStream ucomp : updateComponets) {
	    ucomp.outputVerboseStream(text);
	}
    }
    public void addComponentsWithOutputStream(OutputVerboseStream l){	
	if(l!=null)
	    updateComponets.add(l);	
    }
    
    public class OutputVerboseStreamToConsole extends OutputVerboseStreamAdapter{
	
	@Override
	public void outputVerboseStream(String text){
	    System.out.println(text);
	}
    }
}
