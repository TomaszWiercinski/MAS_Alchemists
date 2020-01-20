package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import java.util.List;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.ReceiveResultBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Product;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductRecipe;

/**
 *
 * @author TomaszWiercinski
 */
public class ReceiveCraftingBehaviour extends ReceiveResultBehaviour<Mage> {

    public ReceiveCraftingBehaviour(Mage agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        if (predicate instanceof Result) {
            
            ProductRecipe product_recipe = (ProductRecipe)((Result) predicate).getValue();
            
            Product product = product_recipe.getRecipe().getProduct();
            List<Product> ingredients = product_recipe.getRecipe().parseIngredients();
            int fee = product_recipe.getFee();
            AID craftsperson = product_recipe.getMerchant();
            
            myAgent.getInventory().add(product);
            
        } else {
            System.out.println(myAgent.getLocalName() + ": No result\n");
        }
    }
    
}
