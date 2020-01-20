package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.ReceiveResultBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.RequestActionBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestIngredientPrices;

/**
 * Requests ingredient prices from merchant agents to be saved to mage agent's
 * ingredient list.
 * 
 * @author TomaszWiercinski
 * @see RequestIngredientPrices
 */
public class RequestIngredientPricesBehaviour extends RequestActionBehaviour<RequestIngredientPrices, Mage> {

    public RequestIngredientPricesBehaviour(Mage agent, AID participant, RequestIngredientPrices action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        return new ReceiveIngredientPricesBehaviour(myAgent, conversationId);
    }
}