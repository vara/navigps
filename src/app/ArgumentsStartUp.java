/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app;

import app.utils.MyLogger;
import config.MainConfiguration;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author vara
 */
public class ArgumentsStartUp {

    private boolean isArguments = false;
    private boolean exitProgram = true;    
    private String infoParameters = "\n\t-v  \t(verbose mode) result return by function only on console" +
				    "\n\t-vg \t(verbose mode) like -v + create window in gui with the same result on console" +
				    "\n\t-sp  \t(Show properties) create window in app with properties chart file" +
				    "\n\t-c  \t(path to configuration file) not suported yet !!!"+
				    "\n\t-f  \t(path to chart file) file format documents must by only svg !!!"+
				    "\n\t-h  \t(Show this text)";
    
    public ArgumentsStartUp(String [] arg){
	
	if(arg.length>0){
	    try {
		
		cheackParameters(arg);
		
	    } catch (Exception ex) {
		helpInformation(ex.getMessage());
	    }
	}else{
	    MyLogger.log.log(Level.FINE,"no arguments to start application");
	}
    }
    
    private void cheackParameters(String [] arg) throws Exception{
	
	VectorPositionForArgumentsStartup vp =new VectorPositionForArgumentsStartup(arg);
	ValidateParameter valid = new ValidateParameter();
	
	String strTmp = "";
	Boolean boolTmp;
	
	while(true){
	    try {
		String param = vp.getNextParameter();
		//System.out.println("" +param);
		if(param.charAt(0)=='-'){
		    if( !(strTmp=param.substring(1)).equals("") ){
			
			if((boolTmp = valid.getInformationOnParameter(strTmp))!=null){
			    
			    if(boolTmp.booleanValue()){
				//parameter with value				
				String val;
				if(vp.isNextParameter()){
				    val = vp.getNextParameter();
				    if(val.charAt(0)=='-'){
					throw new Exception("Missing file to parameter "+strTmp);
				    }
				    if((new File(val)).exists())
				    {
					orderCommands(new Parameter(strTmp,val));
				    }else {
					throw new Exception("This path to file doesn't exist "+val);
				    }
				}else{
				    throw new Exception("Missing file to parameter "+strTmp);
				}			
				
			    }else{//only one parameter (switch)
				
				orderCommands(new Parameter(strTmp,null));
			    }
			    
			}else{
			    throw new Exception("Unrecognized parameter '"+param+"'\tTry with '-h'");
			}
			
		    }else{
			throw new Exception("Bad parameter only '-' not suported\tTry with '-h'");
		    }
		}else{
		    throw new Exception("Bad parameter , missing '-' in '"+param+"'\tTry with '-h'");
		}

	    } catch (IndexOutOfBoundsException ex) {
		//System.out.println("Koniec "+ex.getMessage());
		break;//end while
	    }
	}
	MyLogger.log.log(Level.FINE,"Arguments to start application ["+vp+"]");
    }
    
    public void orderCommands(Parameter p) throws Exception{
	
	switch(p.getCharParam()){
	    case 'v':
		
		    MainConfiguration.setMode(true);
		    break;
	    case 'f':
		
		    MainConfiguration.setPathChartToFile(p.getValue());
		    //You can add check whether this is a file?
		    
		    break;
	    case 's'+'p':
		
		    MainConfiguration.setShowDocumentProperties(true);
		    break;
	    case 'v'+'g':
		
		    MainConfiguration.setModeVerboseGui(true);
		    break;
	    case 'h':
		
		    setExitProgram(true);
		    throw new Exception(getHelpInformation());		    
		    
	    default:
		    setExitProgram(true);
		    throw new Exception("Unrecognized parameter '"+p.getParam()+"'\tTry with '-h'");
	}
    }
    
    public String getHelpInformation(){
	return infoParameters;
    }
    
    public boolean isArguments(){
	return isArguments;
    }
    
    public void setExitProgram(boolean val){
	exitProgram = val;
    }
    
    public boolean exitProgram(){
	return exitProgram;
    }
    
    private void setArgumentsOn(){
	isArguments = true;
    }
    
    public class ValidateParameter {
    
    private HashMap map = new HashMap();
    
	public ValidateParameter(){

	    map.put("vg",new Boolean(false));
	    map.put("v",new Boolean(false));
	    map.put("sp",new Boolean(false));
	    map.put("f",new Boolean(true));
	    map.put("c",new Boolean(true));
	    map.put("h",new Boolean(false));
	}
	public Boolean getInformationOnParameter(String val){
	    return (Boolean)map.get(val);
	}
    }
    
    protected void helpInformation(String text){
	System.out.println(""+text);
	if(exitProgram())
	    System.exit(2);
    }
}
