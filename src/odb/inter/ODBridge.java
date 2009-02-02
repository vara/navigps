package odb.inter;

import java.util.Vector;
import odb.core.Category;
import odb.core.ServiceDescription;

public interface ODBridge {
    
    public ServiceDescription[] searchRadius(double x,double y);                             //zamiast radiusa zakreslamy box
    public ServiceDescription[] searchCategoryRadius(Category category,float x,float y);       //dla danej kategorii
    public Vector getCategories();                                                          //zwraca liste kategorii
    public Vector getSubcategories(Category category);                                                       //lista podkategorii dla danej kategorii
    public ServiceDescription getSingleTest();

}
