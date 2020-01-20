package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.ReceiveResultBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.IngredientPrices;

/**
 * Receives requested ingredient prices from a merchant agent.
 * 
 * @author TomaszWiercinski
 * @see IngredientPrices
 */
public class ReceiveIngredientPricesBehaviour extends ReceiveResultBehaviour<Mage> {

    public ReceiveIngredientPricesBehaviour(Mage agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        System.out.println(myAgent.getLocalName() + ": Receiving ingredient prices from " + participant.getLocalName() + "...");
        
        if (predicate instanceof Result) {
            IngredientPrices prices = (IngredientPrices)((Result) predicate).getValue();
            if (!prices.getList().isEmpty()) {
                System.out.println(myAgent.getLocalName() + ": ProductPrices(" + prices.getList() + ")\n");
                myAgent.handleIngredientPrices(prices);
            } else {
                System.out.println(myAgent.getLocalName() + ": No result\n");
            }
        } else {
            System.out.println(myAgent.getLocalName() + ": No result\n");
        }
    }
}
