package app.database.odb.core;

/**
 *
 * @author ACME
 */
public class Subcategory {
    
    private String name ="no subcategory";
    private Category category;
    
    /**
     *
     * @param category
     * @param name
     */
    public Subcategory(Category category,String name) {
        setName(name);
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
        if(name!=null)
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

    @Override
    public String toString() {
        return "Subcategory: "+getName();
    }


}
