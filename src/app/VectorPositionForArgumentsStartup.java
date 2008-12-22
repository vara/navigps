/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app;

import java.util.Vector;

/**
 *
 * @author vara
 */
public class VectorPositionForArgumentsStartup {
    
    private Vector<String> vec = new Vector<String>();
    private int counter=0;
    
    public VectorPositionForArgumentsStartup(String [] arg){
	
        for (String string : arg) {
            vec.add(string);
        }
    }
    
    @Override
    public String toString(){
        String ret="";
        for (String string : vec) {
            ret+=string+" ";
        }
        return ret;
    }
    
    public String getParameter()throws IndexOutOfBoundsException{
        if(counter<vec.size())
            return vec.elementAt(counter);
        throw new IndexOutOfBoundsException("End list of parameters");
    }
    public String getNextParameter()throws IndexOutOfBoundsException{
        try {
            String ret = getParameter();
            incrementCounter();
            return ret;
        } catch (IndexOutOfBoundsException ex) {
            decrementCounter(); //return to last element in vec
            throw ex;
        }
    }
    
    public boolean isNextParameter(){
        return getCounter()<vec.size();
    }
    
    public String getPreviewParameter()throws LackOfNextItemException{
        try{

            String str = getNextParameter();
            decrementCounter();
            return str;

        }catch(IndexOutOfBoundsException e){
            throw new LackOfNextItemException("Lack of next item ...");
        }
    }
    private void incrementCounter(){
        counter++;
    }
    private void decrementCounter(){
        counter--;
    }
    public final int getCounter(){
        return counter;
    }
    public int getContentSize(){
        return vec.size();
    }
}
