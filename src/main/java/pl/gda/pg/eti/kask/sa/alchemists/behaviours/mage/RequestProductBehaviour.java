package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.ReceiveResultBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.RequestActionBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestProduct;

/**
 * Requests products from merchant agents to be saved to mage agent's inventory.
 * 
 * @author TomaszWiercinski
 * @see RequestProduct
 */
public class RequestProductBehaviour extends RequestActionBehaviour<RequestProduct, Mage> {

    public RequestProductBehaviour(Mage agent, AID participant, RequestProduct action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        return new ReceiveProductBehaviour(myAgent, conversationId);
    }
    
}
