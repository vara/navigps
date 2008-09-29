package odb.inter;

import java.util.Vector;
import odb.core.SubElementService;

public interface ODBridge {
    
    public SubElementService[] searchRadius(double x,double y);                             //zamiast radiusa zakreslamy box
    public SubElementService[] searchCategoryRadius(String category,float x,float y);       //dla danej kategorii
    public Vector getCategories();                                                          //zwraca liste kategorii

}
