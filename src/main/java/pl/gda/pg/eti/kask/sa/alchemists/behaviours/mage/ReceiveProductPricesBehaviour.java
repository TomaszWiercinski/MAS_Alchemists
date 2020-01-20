package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.ReceiveResultBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductPrices;

/**
 * Receives requested product prices from a merchant agent.
 * 
 * @author TomaszWiercinski
 * @see ProductPrices
 */
public class ReceiveProductPricesBehaviour extends ReceiveResultBehaviour<Mage> {

    public ReceiveProductPricesBehaviour(Mage agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        System.out.println(myAgent.getLocalName() + ": Receiving product prices from " + participant.getLocalName() + "...");
        
        if (predicate instanceof Result) {
            ProductPrices prices = (ProductPrices)((Result) predicate).getValue();
            if (!prices.getList().isEmpty()) {
                System.out.println(myAgent.getLocalName() + ": ProductPrices(" + prices.getList() + ")\n");
                myAgent.handleProductPrices(prices);
            } else {
                System.out.println(myAgent.getLocalName() + ": No result\n");
            }
        } else {
            System.out.println(myAgent.getLocalName() + ": No result\n");
        }
    }
}
