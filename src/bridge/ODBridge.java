package bridge;

import java.util.Vector;
import odb.core.ServiceCore;

public interface ODBridge {
    public Vector<ServiceCore> searchCategoryRadius(Vector category, double x, double y, double radius);       //dla danej kategorii
    public Vector<String> getCategories();                                                          //zwraca liste kategorii
    public Vector<String> getSubcategories(String category);                                                       //lista podkategorii dla danej kategorii
    public Vector<String> getSubcategories(Vector<String> category);
}
