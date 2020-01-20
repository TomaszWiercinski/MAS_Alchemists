package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.ReceiveResultBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Product;

/**
 *
 * @author psysiu
 */
public class ReceiveProductBehaviour extends ReceiveResultBehaviour<Mage> {

    public ReceiveProductBehaviour(Mage agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        if (predicate instanceof Result) {
            
            Product product = (Product)((Result) predicate).getValue();
            
            String product_name = product.getName();
            int product_value = product.getValue();
            
            if (product_value <= 0)
                System.out.println(myAgent.getLocalName() + ": Got \"" + product_name + "\" for free. Nice!\n");
            else
            {
                System.out.println(myAgent.getLocalName() + ": Got " + product_name + " for " + product_value + " Knuts.\n");
                myAgent.setPouch(myAgent.getPouch() - product_value);
            }
            
            myAgent.getInventory().add(product);
            
        } else {
            System.out.println(myAgent.getLocalName() + ": No result\n");
        }
    }
    
}
