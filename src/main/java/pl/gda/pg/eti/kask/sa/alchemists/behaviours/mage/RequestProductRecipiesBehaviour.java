package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.ReceiveResultBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.RequestActionBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestProductInfo;

/**
 * Requests a recipe for a specific product from a craftsperson agent.
 * 
 * @author TomaszWiercinski
 * @see RequestProductInfo
 */
public class RequestProductRecipiesBehaviour extends RequestActionBehaviour<RequestProductInfo, Mage> {

    public RequestProductRecipiesBehaviour(Mage agent, AID participant, RequestProductInfo action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        return new ReceiveProductRecipeBehaviour(myAgent, conversationId);
    }
    
}
