/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.ArgumentsStartUp;

import java.io.File;

/**
 *
 * @author wara
 */
public abstract class FileValueParameter extends SingleValueParameter{

    public FileValueParameter(String symbol) {
        super(symbol);
    }

    @Override
    public void handleOption(String optionValue) {

        File file = createFile(optionValue);
        if(file == null){
            throw new IllegalArgumentException();
        }
        handleOption(file);
    }

    @Override
    public boolean isExit() {
        return false;
    }
    
    public abstract void handleOption(File optionValue);

    public File createFile(String path){
        File f = new File(path);

        if(!f.exists()){            
            f = null;
        }
        return f;
    }
}
