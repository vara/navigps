package bridge;

import java.util.Vector;
import org.w3c.dom.Element;

public interface ODBridge {
    public Vector<Element> searchCategoryRadius(String category, float x, float y);       //dla danej kategorii
    public Vector<String> getCategories();                                                          //zwraca liste kategorii
    public Vector<String> getSubcategories(String category);                                                       //lista podkategorii dla danej kategorii
}
