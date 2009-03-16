package odb.core;

import config.DataBaseConfig;
import java.util.Vector;

/**
 *
 * @author wara
 */
public class Category {
    
    private String name;
    private Vector subcategories;
    private String icoPath;
    private String defaultIconExtension = ".png";
    
    /**
     *
     * @param name
     */
    public Category(String name) {
        this.name = name;
        subcategories = null;
        this.icoPath = DataBaseConfig.getIconPath()+name+defaultIconExtension;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public Vector getSubcategories() {
        return subcategories;
    }

    /**
     *
     * @param subcategories
     */
    public void setSubcategories(Vector subcategories) {
        this.subcategories = subcategories;
    }
    
    public void addSubcategory(Subcategory subcategory) {
        subcategories.add(subcategory);
    }
    
    public void removeSubcategory(Subcategory subcategory) {
        subcategories.remove(subcategory);
    }

    @Override
    public String toString() {
        String msg = getClass().getCanonicalName()+" [ Name : "+getName()+" ]";
        return msg;
    }

    /**
     * @return the icoPath
     */
    public String getIcoPath() {
        return icoPath;
    }

    /**
     * @param icoPath the icoPath to set
     */
    public void setIcoPath(String icoPath) {
        this.icoPath = icoPath;
    }
}
