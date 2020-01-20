package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import jade.content.Concept;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Information about a product created by a Craftsperson agent.
 * 
 * @author TomaszWiercinski
 * @see pl.gda.pg.eti.kask.sa.alchemists.agents.Craftsperson
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Recipe implements Concept {
    
    private Product product;
    private String ingredients = "";
    private int id;
    private static int count = 0;
    
    public Recipe() {
        id = count;
        count++;
    }
    
    public Recipe(Product product, Product ... ingredients) {
        this.product = product;
        for (Product ingr : ingredients)
            if (this.ingredients.isEmpty())
                this.ingredients += ingr.toString();
            else
                this.ingredients += ',' + ingr.toString();
    }
    
    public void addIngredient(Product product) {
        if (this.ingredients.isEmpty())
                this.ingredients += product.toString();
        else
            this.ingredients += ',' + product.toString();
    }
    
    @Override
    public String toString() {
        return product.toString() + ',' + ingredients;
    }    
    
    public List<Product> parseIngredients() {
        List<Product> ingredient_list = new ArrayList<>();
        
        String[] ingredient_str_array = ingredients.split(",");
        for (String ingredient_str : ingredient_str_array) {
            ingredient_list.add(new Product(ingredient_str));
        }
        
        return ingredient_list;
    }
}
