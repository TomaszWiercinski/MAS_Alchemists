package pl.gda.pg.eti.kask.sa.alchemists.behaviours.merchant;

import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.List;
import java.util.Objects;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Merchant;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.AlchemyOntology;
import java.util.logging.Level;
import lombok.extern.java.Log;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Product;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestProduct;

/**
 *
 * @author TomaszWiercinski
 */
@Log
public class SellProductBehaviour extends OneShotBehaviour {

    protected final Merchant myAgent;

    protected final String conversationId;

    protected final AID participant;

    protected final RequestProduct action;
    
    public SellProductBehaviour(Merchant agent, RequestProduct action, String conversationId, AID participant) {
        super(agent);
        this.myAgent = agent;
        this.action = action;
        this.conversationId = conversationId;
        this.participant = participant;
    }

    @Override
    public void action() {
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
    
    protected Predicate performAction() {
        System.out.println(myAgent.getLocalName() + ": Looking for \"" + 
                action.getProduct().getName() + "\" with ID " +
                action.getProduct().getId() + " in stock...");
        
        Product found_product = null;
        
        List<Product> product_list = myAgent.getProducts();
        
        for (Product product : product_list) {
            if (Objects.equals(product.getId(), action.getProduct().getId())) {
                found_product = product;
                break;
            }
        }
        
        if (found_product != null) {
            System.out.println(myAgent.getLocalName() + ": Found! Sending the product...\n");
            myAgent.getProducts().remove(found_product);
            return new Result(action, found_product);
        } else {
            System.out.println(myAgent.getLocalName() + ": Product not found...\n");
            return null;    
        }
    }
}