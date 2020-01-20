package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.ReceiveResultBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.RequestActionBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestCrafting;

/**
 * Requests crafting of a product from craftsperson agents.
 * 
 * @author TomaszWiercinski
 * @see pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestCrafting
 */
public class RequestCraftingBehaviour extends RequestActionBehaviour<RequestCrafting, Mage> {

    public RequestCraftingBehaviour(Mage agent, AID participant, RequestCrafting action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        return new ReceiveCraftingBehaviour(myAgent, conversationId);
    }
    
}
