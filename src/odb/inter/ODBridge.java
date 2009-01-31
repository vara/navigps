package odb.inter;

import java.util.Vector;
import odb.core.Category;
import odb.core.SubElementService;

public interface ODBridge {
    
    public SubElementService[] searchRadius(double x,double y);                             //zamiast radiusa zakreslamy box
    public SubElementService[] searchCategoryRadius(Category category,float x,float y);       //dla danej kategorii
    public Vector getCategories();                                                          //zwraca liste kategorii
    public Vector getSubcategories(Category category);                                                       //lista podkategorii dla danej kategorii
    public SubElementService getSingleTest();

}
