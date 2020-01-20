package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.ReceiveResultBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.RequestActionBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestProductInfo;

/**
 * Requests prices for a specific product from merchant agents.
 * 
 * @author TomaszWiercinski
 * @see RequestProductInfo
 */
public class RequestProductPricesBehaviour extends RequestActionBehaviour<RequestProductInfo, Mage> {

    public RequestProductPricesBehaviour(Mage agent, AID participant, RequestProductInfo action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        return new ReceiveProductPricesBehaviour(myAgent, conversationId);
    }
    
}