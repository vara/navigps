package odb.core;

/**
 *
 * @author praise
 */
public class Subcategory {
    
    private String name;
    private Category category;
    
    public Subcategory(Category category,String name) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
