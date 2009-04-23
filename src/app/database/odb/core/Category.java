package app.database.odb.core;

import app.config.DataBaseConfig;
import java.util.Vector;

/**
 *
 * @author ACME
 */
public class Category {
    
    private String name = "no category";
    private Vector subcategories;
    private String icoPath;
    private String defaultIconExtension = ".png";
    
    /**
     *
     * @param name
     */
    public Category(String name) {
        setName(name);
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
        if(name != null)
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
        return "Category: " + getName();
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
/*
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Category)
            return hashCode() == obj.hashCode();
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 59 * hash + (this.subcategories != null ? this.subcategories.hashCode() : 0);
        return hash;
    }
*/
}
