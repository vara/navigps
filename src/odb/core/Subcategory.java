package odb.core;

/**
 *
 * @author ACME
 */
public class Subcategory {
    
    private String name;
    private Category category;
    
    /**
     *
     * @param category
     * @param name
     */
    public Subcategory(Category category,String name) {
        this.name = name;
        this.category = category;
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
    public Category getCategory() {
        return category;
    }

    /**
     *
     * @param category
     */
    public void setCategory(Category category) {
        this.category = category;
    }
}
