package pl.gda.pg.eti.kask.sa.alchemists.behaviours.craftsperson;

import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Craftsperson;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.AlchemyOntology;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestCrafting;
import java.util.logging.Level;
import lombok.extern.java.Log;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductRecipe;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Recipe;

/**
 * Response to a recipe request. Sends recipe for specified product and fee.
 * 
 * THIS DOES NOT WORK (yet?) / PLEASE DO NOT LOOK AT THIS MESS
 * 
 * @author TomaszWiercinski
 */
@Log
public class GiveCraftingProductBehaviour extends OneShotBehaviour {

    protected final Craftsperson myAgent;
    protected final String conversationId;
    protected final AID participant;
    protected final RequestCrafting action;
    
    public GiveCraftingProductBehaviour(Craftsperson agent, RequestCrafting action, String conversationId, AID participant) {
        super(agent);
        this.myAgent = agent;
        this.action = action;
        this.conversationId = conversationId;
        this.participant = participant;
    }

    @Override
    public void action() {
        System.out.println(myAgent.getLocalName() + ": Crafting product for recipe \"" + action.getRecipe_id() + "\"...\n");
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
        ProductRecipe product_recipe = new ProductRecipe();
        product_recipe.setMerchant(myAgent.getAID());
        product_recipe.setFee(myAgent.getFee());
        Boolean found = false;
        for (Recipe recipe : myAgent.getRecipies()) {
            if (recipe.getProduct().getName().equals(action.getRecipe_id()))
            {
                product_recipe.setRecipe(recipe);
                found = true;
                break;
            }
        }
        
        return new Result(action, product_recipe);
    }
    
}