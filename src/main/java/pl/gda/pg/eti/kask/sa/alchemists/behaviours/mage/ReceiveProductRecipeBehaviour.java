package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.ReceiveResultBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductRecipe;

/**
 * Receives requested product recipe from a craftsperson agent.
 * 
 * @author TomaszWiercinski
 * @see ProductRecipe
 */
public class ReceiveProductRecipeBehaviour extends ReceiveResultBehaviour<Mage> {

    public ReceiveProductRecipeBehaviour(Mage agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        System.out.println(myAgent.getLocalName() + ": Receiving product recipe from " + participant.getLocalName() + "...");
        
        if (predicate instanceof Result) {
            ProductRecipe product_recipe = (ProductRecipe)((Result) predicate).getValue();
            if (product_recipe.getRecipe() != null) {
                System.out.println(myAgent.getLocalName() + ": Recipe(" + 
                        product_recipe.getRecipe().getProduct().getName() + "," + 
                        product_recipe.getRecipe().getIngredients() + ")\n");
                myAgent.handleProductRecipe(product_recipe);
            } else {
                System.out.println(myAgent.getLocalName() + ": No result\n");
            }
        } else {
            System.out.println(myAgent.getLocalName() + ": No result\n");
        }
    }
}