/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps;

import java.util.Vector;

/**
 *
 * @author vara
 */
public class VectorPositionForArgumentsStartup {
    
    private Vector<String> vec = new Vector<String>();
    private int counter=0;
    
    /**
     *
     * @param arg
     */
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
    
    /**
     *
     * @return
     * @throws java.lang.IndexOutOfBoundsException
     */
    public String getParameter()throws IndexOutOfBoundsException{
        if(counter<vec.size())
            return vec.elementAt(counter);
        throw new IndexOutOfBoundsException("End list of parameters");
    }
    /**
     *
     * @return
     * @throws java.lang.IndexOutOfBoundsException
     */
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
    
    /**
     *
     * @return
     */
    public boolean isNextParameter(){
        return getCounter()<vec.size();
    }
    
    /**
     *
     * @return
     * @throws app.LackOfNextItemException
     */
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
    /**
     *
     * @return
     */
    public final int getCounter(){
        return counter;
    }
    /**
     *
     * @return
     */
    public int getContentSize(){
        return vec.size();
    }
}
