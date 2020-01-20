package pl.gda.pg.eti.kask.sa.alchemists.agents;

import jade.domain.DFService;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.merchant.MerchantBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.RegisterServiceBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Product;

/**
 * Parent class of all agents selling products.
 * 
 * @author TomaszWiercinski
 * @param <P> Class of products sold by merchant.
 */
public abstract class Merchant<P extends Product> extends BaseAgent {

    // Products sold by merchant
    @Getter
    protected final List<Product> products = new ArrayList<>();
    
    // Specific service title of merchant, default = "merchant"
    private String service_title = "merchant";
    
    public Merchant(String service_title) {
        this.service_title = service_title;
    }

    @Override
    protected void setup() {
        System.out.println(this.getLocalName() + ": Starting " + service_title + " agent, with...");
        super.setup();
        
        Object[] args = getArguments();
        
        // Set defaults if no arguments passed
        if (args.length == 0)
            setDefaults();
        
        for (Object arg : args)
        {
            String arg_str = arg.toString();
            String[] sub_args = arg_str.split(";");
            
            String obj_name = sub_args[0];
            int obj_value = Integer.parseInt(sub_args[1]);
            int obj_count = 1;
            
            if (sub_args.length == 3)
                obj_count = Integer.parseInt(sub_args[2]);
            
            // Add new products
            for (int i = 0; i < obj_count; i++ ) {
                addProduct(obj_name, obj_value);
            }
        }
        
        // Log passed arguments
        for (Product product : products)
            System.out.println(this.getLocalName() + ": " + product.getName() + " for " + product.getValue() + " Knuts, ID = " + product.getId());
        System.out.println();
        
        addBehaviour(new RegisterServiceBehaviour(this, service_title));
        addBehaviour(new MerchantBehaviour(this));
    }
    
    @Override
    protected void takeDown() 
    {
       System.out.println(this.getLocalName() + ": Deregistering service.");
       try { DFService.deregister(this); }
       catch (Exception e) {}
    }
    
    protected abstract void setDefaults();
    
    protected abstract void addProduct(String obj_name, int obj_value);
}