package pl.gda.pg.eti.kask.sa.alchemists.behaviours.craftsperson;

import jade.content.onto.basic.Action;
import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Craftsperson;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.WaitingBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestCrafting;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestProductInfo;

/**
 * UNFINISHED.
 * Currently only responds to requests for specific product recipies and fees.
 * 
 * @author TomaszWiercinski
 */
public class CraftspersonBehaviour extends WaitingBehaviour<Craftsperson>{

    public CraftspersonBehaviour(Craftsperson agent) {
        super(agent);
    }

    @Override
    protected void action(Action action, String conversationId, AID participant) {
        if (action.getAction() instanceof RequestProductInfo) {
            RequestProductInfo product_request = (RequestProductInfo)action.getAction();
            System.out.println(myAgent.getLocalName() + ": Received product recipe request for \"" + product_request.getProduct().getName() + "\".\n");
            myAgent.addBehaviour(new GiveProductRecipeBehaviour(myAgent, product_request, conversationId, participant));
        } /* else if (action.getAction() instanceof RequestCrafting) {
            RequestCrafting crafting_request = (RequestCrafting)action.getAction();
            System.out.println(myAgent.getLocalName() + ": Received product crafting request for recipe id " + crafting_request.getRecipe_id() + ".\n");
            myAgent.addBehaviour(new GiveCraftingProductBehaviour(myAgent, crafting_request, conversationId, participant));
        } */
    }
    
}