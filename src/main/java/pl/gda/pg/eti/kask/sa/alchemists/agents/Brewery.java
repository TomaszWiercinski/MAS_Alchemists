package pl.gda.pg.eti.kask.sa.alchemists.agents;

import pl.gda.pg.eti.kask.sa.alchemists.ontology.Product;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductHerb;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductPotion;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductWand;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Recipe;

/**
 * <h1>Craftsperson - Brewery</h1>
 * UNFINISHED.
 * Currently only responds to requests for specific product recipies and fees.
 * 
 * @author TomaszWiercinski
 * @see ProductPotion
 */
public class Brewery extends Craftsperson {
    
    public Brewery() {
        super();
    }

    @Override
    protected void setDefaults() {
        recipies.add(
                    new Recipe(
                    new ProductPotion("Spirited Potion"), 
                    new ProductHerb("Spirit Grass"),
                    new ProductWand("Oak Wand")));
            
        recipies.add(
                new Recipe(
                        new ProductPotion("Uplifting Potion"),
                        new ProductWand("Oak Wand"),
                        new ProductHerb("Glitter Berry")));

        recipies.add(
                new Recipe(
                        new ProductPotion("The Dreamweaver's Spirit"),
                        new ProductWand("Sandalwood Wand"), 
                        new ProductHerb("Dream Mint"), 
                        new ProductHerb("Spirit Grass")));
    }

    @Override
    protected void setDefaultFee() {
        fee = 5;
    }

    @Override
    protected void addRecipe(String[] sub_args) {
        Recipe new_recipe = new Recipe();
        new_recipe.setProduct(new ProductPotion(sub_args[0]));
        for (int j = 1; j < sub_args.length; j += 2)
            switch (sub_args[j]) {
                case "w":
                    new_recipe.addIngredient(new ProductWand(sub_args[j+1]));
                    break;
                case "h":
                    new_recipe.addIngredient(new ProductHerb(sub_args[j+1]));
                    break;
                case "p":
                    new_recipe.addIngredient(new ProductPotion(sub_args[j+1]));
                    break;
                default:
                    new_recipe.addIngredient(new Product(sub_args[j+1]));
                    break;
            }
        
        recipies.add(new_recipe);
    }

}
