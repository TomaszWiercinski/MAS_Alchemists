package pl.gda.pg.eti.kask.sa.alchemists.behaviours.merchant;

import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Merchant;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.AlchemyOntology;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Product;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductPrices;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestProductInfo;
import java.util.logging.Level;
import lombok.extern.java.Log;

/**
 * Sends product prices to a requesting mage agent.
 *
 * @author TomaszWiercinski
 */
@Log
public class GiveProductPricesBehaviour<P extends Product> extends OneShotBehaviour {

    protected final Merchant<P> myAgent;
    protected final String conversationId;
    protected final AID participant;
    protected final RequestProductInfo action;
    
    public GiveProductPricesBehaviour(Merchant agent, RequestProductInfo action, String conversationId, AID participant) {
        super(agent);
        this.myAgent = agent;
        this.action = action;
        this.conversationId = conversationId;
        this.participant = participant;
    }

    @Override
    public void action() {
        System.out.println(myAgent.getLocalName() + ": Giving product prices for \"" + action.getProduct().getName() + "\"...\n");
        Predicate result = performAction();
        ACLMessage msg;
        if (result != null) {
            msg = new ACLMessage(ACLMessage.INFORM);
        } else {
            msg = new ACLMessage(ACLMessage.REFUSE);
        }
        msg.setLanguage(new SLCodec().getName());
        msg.setOntology(AlchemyOntology.getInstance().getName());
        msg.setConversationId(conversationId);
        msg.addReceiver(participant);
        try {
            if (result != null) {
                myAgent.getContentManager().fillContent(msg, result);
            }
            myAgent.send(msg);
        } catch (Codec.CodecException | OntologyException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Gets all the requested information from the merchant agent.
     * 
     * @return Predicate containing prices, product name and merchant's AID.
     */
    protected Predicate performAction() {
        ProductPrices product_prices = new ProductPrices();
        product_prices.setMerchant(myAgent.getAID());
        product_prices.setProduct_name(action.getProduct().getName());
        
        Boolean found = false;
        for (Product product : myAgent.getProducts()) {
            if (product.getName().equals(action.getProduct().getName())) {
                product_prices.add(product);
                found = true;
            }
        }
        
        return new Result(action, product_prices);
        
    }
    
}
