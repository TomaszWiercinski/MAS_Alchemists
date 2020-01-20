package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import jade.content.Concept;
import java.util.ArrayList;
import java.util.List;

/**
 * Used as a quick work-around for issues with sending lists.
 * 
 * @author TomaszWiercinski
 */
public class ProductList implements Concept {
    
    private String list = "";
    
    public void add(Product product) {
    if (this.list.isEmpty())
        this.list += product.toString();
    else
        this.list += ',' + product.toString();
    }
    
    @Override
    public String toString() {
        return list;
    }    
    
    public List<Product> parse() {
        List<Product> ingredient_list = new ArrayList<>();
        
        String[] ingredient_str_array = list.split(",");
        for (String ingredient_str : ingredient_str_array) {
            ingredient_list.add(new Product(ingredient_str));
        }
        
        return ingredient_list;
    }
    
}


