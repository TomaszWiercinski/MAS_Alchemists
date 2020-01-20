package pl.gda.pg.eti.kask.sa.alchemists.agents;

import jade.domain.DFService;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.craftsperson.CraftspersonBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.RegisterServiceBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Product;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Recipe;

/**
 * Parent class of all agents crafting new products.
 * Responsible for service registration and deregistration. Preliminary argument
 * parsing.
 * 
 * @author TomaszWiercinski
 */
public abstract class Craftsperson extends BaseAgent {

    @Getter
    protected final List<Recipe> recipies = new ArrayList<>();
    private String service_title = "craftsperson";
    @Getter
    protected int fee;
    
    public Craftsperson() {
    }
    
    public Craftsperson(String service_title) {
        this.service_title = service_title;
    }

    @Override
    protected void setup() {
        System.out.println(this.getLocalName() + ": Starting " + service_title + " agent...");
        super.setup();
        
        Object[] args = getArguments();
        int arg_index = 1;

        // Get brewery fee.
        try {
            fee = Integer.parseInt(args[0].toString());
        } catch (NumberFormatException e) {
            setDefaultFee();
            arg_index = 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            // Set defaults if no arguments passed
            setDefaults();
            setDefaultFee();
        }
        
        for (int i = arg_index; i < args.length; i++) {
            String[] sub_args = args[i].toString().split(";");
            addRecipe(sub_args);
        }
        
        // Log passed fee
        System.out.println(this.getLocalName() + ": Setting the fee to " + fee + " Knuts.");
        
        // Log passed recipies
        for (Recipe recipe : recipies)
        {
            System.out.println(this.getLocalName() + ": Adding recipe for \"" + recipe.getProduct().getName() + " to recipe book:");
            for (Product product : recipe.parseIngredients())
            {
                System.out.println(this.getLocalName() + ": Ingredient: \"" + product.getName() + "\"");
            }
        }
        System.out.println();
        
        addBehaviour(new RegisterServiceBehaviour(this, service_title));
        addBehaviour(new CraftspersonBehaviour(this));
    }
    
    @Override
    protected void takeDown() 
    {
       System.out.println(this.getLocalName() + ": Deregistering service.");
       try { DFService.deregister(this); }
       catch (Exception e) {}
    }
    
    protected abstract void setDefaults();
    
    protected abstract void setDefaultFee();
    
    protected abstract void addRecipe(String[] sub_args);

}
