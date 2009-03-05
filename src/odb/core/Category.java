package odb.core;

import java.util.Vector;

public class Category {
    
    private String name;
    private Vector subcategories;
    
    public Category(String name) {
        this.name = name;
        subcategories = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector getSubcategories() {
        return subcategories;
    }

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
}
