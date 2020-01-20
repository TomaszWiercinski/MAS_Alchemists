package pl.gda.pg.eti.kask.sa.alchemists.behaviours.merchant;

import jade.content.onto.basic.Action;
import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Merchant;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.WaitingBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestIngredientPrices;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestProduct;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestProductInfo;

/**
 * 
 * @author TomaszWiercinski
 */
public class MerchantBehaviour extends WaitingBehaviour<Merchant>{

    public MerchantBehaviour(Merchant agent) {
        super(agent);
    }

    @Override
    protected void action(Action action, String conversationId, AID participant) {
        if (action.getAction() instanceof RequestProduct) {
            RequestProduct product_request = (RequestProduct) action.getAction();
            System.out.println(myAgent.getLocalName() + ": Received product purchase request for \"" + product_request.getProduct().getName() + "\".\n");
            myAgent.addBehaviour(new SellProductBehaviour(myAgent, product_request, conversationId, participant));
        } else if (action.getAction() instanceof RequestProductInfo) {
            RequestProductInfo product_request = (RequestProductInfo)action.getAction();
            System.out.println(myAgent.getLocalName() + ": Received product prices request for \"" + product_request.getProduct().getName() + "\".\n");
            myAgent.addBehaviour(new GiveProductPricesBehaviour(myAgent, product_request, conversationId, participant));
        } else if (action.getAction() instanceof RequestIngredientPrices) {
            RequestIngredientPrices product_request = (RequestIngredientPrices)action.getAction();
            System.out.println(myAgent.getLocalName() + ": Received ingredient prices request for \"" + 
                    product_request.getProduct().getName() + "\", recipe id: " + 
                    product_request.getRecipe_id() + ".\n");
            myAgent.addBehaviour(new GiveIngredientPricesBehaviour(myAgent, product_request, conversationId, participant));
        }
    }
}
