package pl.gda.pg.eti.kask.sa.alchemists.agents;

import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.FindServiceBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage.RequestIngredientPricesBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage.RequestProductBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage.RequestProductPricesBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage.RequestProductRecipiesBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage.WaitForCraftingPlanBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage.WaitForFinishBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage.WaitForRecipeAssessmentBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.collections.Pair;
import pl.gda.pg.eti.kask.sa.alchemists.collections.Triple;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.IngredientPrices;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductHerb;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductPotion;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Product;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductPrices;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductRecipe;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductWand;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.Recipe;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestIngredientPrices;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestProduct;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.RequestProductInfo;

/**
 * <h1>Mage agent</h1>
 * Performs purchase and crafting requests to fulfill win condition passed as 
 * arguments during creation.
 * 
 * @author TomaszWiercinski
 */
public class Mage extends BaseAgent {

    @Getter
    @Setter
    private int pouch; // Number of Knuts available to spend.
    
    // List of needed products, passed as arguments while starting the agent.
    private List<Product> product_list = new ArrayList<>(); 
    
    // All the necessary information about the products to perform a purchase.
    private List<Triple<Integer, Product, AID>> shopping_list = new ArrayList<>();
    
    // List of all acquired products.
    @Getter
    private List<Product> inventory = new ArrayList<>();
    
    // List of all known recipies and where to use them.
    // [(fee, recipe, craftsperson), ...]
    //@Getter
    //private List<Triple<Integer, Recipe, AID>> recipies = new ArrayList<>();
    @Getter
    private HashMap<Integer, Triple<Integer, Recipe, AID>> recipies = new HashMap<>();
    
    // Shopping list of ingredients for recipe assessment.
    // [recipe_id: [ingredient_shopping_list], ...]
    private HashMap<Integer, List<Triple<Integer, Product, AID>>> ingredient_list = new HashMap<>();
    
    // List of crafting requests to be performed.
    // [recipe_id, ...]
    private List<Integer> crafting_list = new ArrayList<>();
    
    public Mage() {
    }

    @Override
    protected void setup() {
        super.setup();
        parseArguments();
        assessEnvironment();
    }
    
    /**
     * <h1>Parse arguments</h1>
     * Parses given arguments into amount of available funds and list of needed 
     * products.
     */
    private void parseArguments() {
        System.out.println(this.getLocalName() + ": Starting Mage agent...");
        
        Object[] args = getArguments();
        int arg_index_start;
        
        // Parse pouch argument
        try {
            // Pouch argument given
            pouch = Integer.parseInt(args[0].toString());
            arg_index_start = 1;
        } catch (NumberFormatException e) {
            // Pouch argument not given, shopping list given
            pouch = 50;
            arg_index_start = 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            // No arguments passed, set defaults
            pouch = 50;
            arg_index_start = 0;
            product_list.add(new ProductPotion("Spirited Potion"));
            product_list.add(new ProductPotion("Uplifting Potion"));
            product_list.add(new ProductWand("Oak Wand"));
            
        }
        
        // Parse shopping list arguments
        for (int i = arg_index_start; i < args.length; i++)
        {
            Object o = args[i];
            
            String[] input = o.toString().split(";");
            
            char obj_type = input[0].charAt(0);
            String obj_name = input[1];
            int obj_num;
            
            if (input.length < 3)
                obj_num = 1;
            else
                obj_num = Integer.parseInt(input[2]);
            
            switch (obj_type)
            {
                case 'p':
                    for (int j = 0; j < obj_num; j++) {
                        ProductPotion new_product = new ProductPotion(obj_name);
                        new_product.setId(-1);
                        product_list.add(new_product);
                    }
                    break;
                case 'h':
                    for (int j = 0; j < obj_num; j++) {
                        ProductHerb new_product = new ProductHerb(obj_name);
                        new_product.setId(-1);
                        product_list.add(new_product);
                    }
                    break;
                case 'w':
                    for (int j = 0; j < obj_num; j++) {
                        ProductWand new_product = new ProductWand(obj_name);
                        new_product.setId(-1);
                        product_list.add(new_product);
                    }
                    break;
            }
        }
        
        // Log passed arguments
        System.out.println(this.getLocalName() + ": With " + pouch + " Knuts to their name.");
        for (Product product : product_list) 
            System.out.println(this.getLocalName() + ": Looking for \"" + 
                    product.getName() + "\" of type " + product.getType() + ".");
        System.out.println();
    }
    
    /**
     * <h1>Initial envirionment assessment</h1>
     * Sends requests for prices and recipies of needed products.
     */
    private void assessEnvironment() {
        SequentialBehaviour find_recipe_sb = new SequentialBehaviour(this);
        SequentialBehaviour find_product_sb = new SequentialBehaviour(this);
        
        // Get unique products
        List<Product> product_list_unique = product_list.stream().distinct().collect(Collectors.toList());
        
        System.out.println(this.getLocalName() + ": Looking for unique products...");
        
        for (Product product : product_list_unique) {
            System.out.println(this.getLocalName() + ": " + product);
            find_product_sb.addSubBehaviour(this.findProductPrices(product));
            find_recipe_sb.addSubBehaviour(findProductRecipies(product));
        }
        
        addBehaviour(find_product_sb);
        addBehaviour(find_recipe_sb);
        addBehaviour(new WaitForRecipeAssessmentBehaviour(this, find_product_sb, find_recipe_sb));
    }
    
    /**
     * <h1>Request prices</h1>
     * Price requests for a single product, sent to all agents with appropriate
     * registered service. The prices will later be added to the shopping list.
     * 
     * @param product Product to be found.
     * @return Behaviour for finding agents and requesting prices.
     */
    private FindServiceBehaviour findProductPrices(Product product) {
        String service_title = product.getService_title();
        
        FindServiceBehaviour behaviour = new FindServiceBehaviour(this, service_title) {

            @Override
            protected void onResult(DFAgentDescription[] services) {
                System.out.println(myAgent.getLocalName() + ": Looking for prices for \"" + product.getName() + "\".");
                System.out.println(myAgent.getLocalName() + ": Querying " + service_title + "s...");
                System.out.println();
                if (services != null && services.length > 0) {
                    for (DFAgentDescription service : services)
                    {
                        AID merchant = service.getName();
                        RequestProductInfo action = new RequestProductInfo(product);
                        RequestProductPricesBehaviour request = new RequestProductPricesBehaviour(Mage.this, merchant, action);
                        ((SequentialBehaviour) getParent()).addSubBehaviour(request);
                    }
                }
            }
        };
        
        return behaviour;
    }
    
    /**
     * <h1>Request prices</h1>
     * Price requests for a single product, sent to all agents with appropriate
     * registered service. The prices will later be added to the shopping list.
     * 
     * @param product Product to be found.
     * @return Behaviour for finding agents and requesting prices.
     */
    private FindServiceBehaviour findIngredientPrices(Product product, int recipe_id) {
        String service_title = product.getService_title();
        
        FindServiceBehaviour behaviour = new FindServiceBehaviour(this, service_title) {

            @Override
            protected void onResult(DFAgentDescription[] services) {
                System.out.println(myAgent.getLocalName() + ": Looking for prices for \"" + product.getName() + "\".");
                System.out.println(myAgent.getLocalName() + ": Querying " + service_title + "s...\n");
                if (services != null && services.length > 0) {
                    for (DFAgentDescription service : services)
                    {
                        AID merchant = service.getName();
                        RequestIngredientPrices action = new RequestIngredientPrices(product, recipe_id);
                        RequestIngredientPricesBehaviour request = new RequestIngredientPricesBehaviour(Mage.this, merchant, action);
                        ((SequentialBehaviour) getParent()).addSubBehaviour(request);
                    }
                }
            }
        };
        
        return behaviour;
    }
    
    /**
     * <h1>Request recipe</h1>
     * Recipe request for a single product, sent to all agents with appropriate
     * registered service. The recipies along with the respective fees will 
     * later be added to the recipe list.
     * 
     * @param product Product to be found
     * @return Behaviour for finding agents and requesteing recipe.
     */
    private FindServiceBehaviour findProductRecipies(Product product) {
        String service_title = "craftsperson";
        
        FindServiceBehaviour behaviour = new FindServiceBehaviour(this, service_title) {

            @Override
            protected void onResult(DFAgentDescription[] services) {
                System.out.println(myAgent.getLocalName() + ": Looking for recipies for \"" + product.getName() + "\".");
                System.out.println(myAgent.getLocalName() + ": Querying " + service_title + "s...");
                System.out.println();
                if (services != null && services.length > 0) {
                    for (DFAgentDescription service : services)
                    {
                        AID craftsperson = service.getName();
                        RequestProductInfo action = new RequestProductInfo(product);
                        RequestProductRecipiesBehaviour request = new RequestProductRecipiesBehaviour(Mage.this, craftsperson, action);
                        ((SequentialBehaviour) getParent()).addSubBehaviour(request);
                    }
                }
            }
        };
        
        return behaviour;
    }
    
    /**
     * <h1>Price handling</h1>
     * Handles prices of a specific product received from a Merchant agent. 
     * Adds cheapest products to shopping list and replaces the more expensive 
     * ones.
     * 
     * @param prices Received price list.
     */
    public void handleProductPrices(ProductPrices prices) {
        System.out.println(this.getLocalName() + ": Handling price list from " + prices.getMerchant().getLocalName());
        
        List<Pair<Integer, Integer>> price_id_pairs = prices.getParsedPrices();
        Collections.sort(price_id_pairs);
        
        // Look through products to buy and add them to the shopping list
        List<Product> products_to_delete = new ArrayList<>();
        for (Product product : product_list) {
            if (product.getName().equals(prices.getProduct_name()) && 
                    !price_id_pairs.isEmpty()) {
                products_to_delete.add(product);
                Product moved_product = product;
                moved_product.setId(price_id_pairs.get(0).getSecond());
                shopping_list.add(new Triple<>(price_id_pairs.get(0).getFirst(), moved_product, prices.getMerchant()));
                price_id_pairs.remove(0);
            }
        }
        
        // Remove moved products from product list
        for (Product product : products_to_delete) {
            product_list.remove(product);
        }
        
        // Check if there are better deals for products already on shopping list
        // Desc sorting needed to maximise saved money
        Collections.sort(shopping_list);
        Collections.reverse(shopping_list);
        if (!price_id_pairs.isEmpty()) {
            for (Triple<Integer, Product, AID> triple : shopping_list) {
                if (triple.getSecond().getName().equals(prices.getProduct_name()) &&
                        !price_id_pairs.isEmpty() && 
                        price_id_pairs.get(0).getFirst() < triple.getFirst()) {
                    triple.getSecond().setId(price_id_pairs.get(0).getSecond());
                    triple.setFirst(price_id_pairs.get(0).getFirst());
                    triple.setThird(prices.getMerchant());
                    price_id_pairs.remove(0);
                }
            }
        }
        
        // Log new shopping list
        System.out.println(this.getLocalName() + ": New shopping list...");
        for (Triple<Integer, Product, AID> triple : shopping_list) {
            System.out.println(this.getLocalName() + ": " + 
                    triple.getFirst() + ", " +
                    triple.getSecond() + ", " +
                    triple.getThird().getLocalName());
        }
        System.out.println();
    }
    
    /**
     * <h1>Price handling</h1>
     * Handles prices of a specific ingredient received from a Merchant agent. 
     * Adds cheapest products to shopping list and replaces the more expensive 
     * ones (within single recipe shopping list).
     * 
     * @param prices Received price list.
     */
    public void handleIngredientPrices(IngredientPrices prices) {
        System.out.println(this.getLocalName() + ": Handling ingredient price list...");
        
        List<Pair<Integer, Integer>> price_id_pairs = prices.getParsedPrices();
        Collections.sort(price_id_pairs);
        
        // Make sure the shopping list for the recipe was instantiated
        if (!this.ingredient_list.containsKey(prices.getRecipe_id())) {
            
            // Get the correct recipe
            Triple<Integer, Recipe, AID> recipe = null;
            if (recipies.containsKey(prices.getRecipe_id())) {
                recipe = recipies.get(prices.getRecipe_id());
            }
            if (recipe == null) {
                System.out.println(this.getLocalName() + ": INVALID RECIPE ID " + prices.getRecipe_id() + "!");
            }
            List<Product> ingredient_list = recipe.getSecond().parseIngredients();
            
            // Add ingredients to shopping list with max possible prices and unknown merchants.
            this.ingredient_list.put(prices.getRecipe_id(), new ArrayList<>());
            for (Product ingredient : ingredient_list)
                this.ingredient_list.get(prices.getRecipe_id()).add(
                new Triple<>(Integer.MAX_VALUE, ingredient, null));
        }
        
        // Make sure that the product prices don't belong to products already on the shopping list
        List<Pair<Integer, Integer>> price_id_pairs_to_remove = new ArrayList<>();
        for (Pair<Integer, Integer> pair : price_id_pairs) {
            for (Triple <Integer, Product, AID> triple : shopping_list) {
                if (Objects.equals(triple.getSecond().getId(), pair.getSecond())) {
                    price_id_pairs_to_remove.add(pair);
                }
            }
        }
        for (Pair<Integer, Integer> pair : price_id_pairs_to_remove)
            price_id_pairs.remove(pair);
        
        // Check if there are better deals for products already on shopping list
        // Desc sorting needed to maximise saved money
        Collections.sort(this.ingredient_list.get(prices.getRecipe_id()));
        Collections.reverse(this.ingredient_list.get(prices.getRecipe_id()));
        for (Triple<Integer, Product, AID> triple : this.ingredient_list.get(prices.getRecipe_id())) {
            if (triple.getSecond().getName().equals(prices.getProduct_name()) &&
                    !price_id_pairs.isEmpty() && 
                    price_id_pairs.get(0).getFirst() < triple.getFirst()) {
                triple.getSecond().setId(price_id_pairs.get(0).getSecond());
                triple.setFirst(price_id_pairs.get(0).getFirst());
                triple.setThird(prices.getMerchant());
                price_id_pairs.remove(0);
            }
        }
        
        // Log new shopping list
        System.out.println(this.getLocalName() + ": New ingredient shopping list for recipe ID " + prices.getRecipe_id() + "...");
        for (Triple<Integer, Product, AID> triple : this.ingredient_list.get(prices.getRecipe_id())) {
            if (triple.getThird() != null)
                System.out.println(this.getLocalName() + ": " + 
                        triple.getFirst() + ", " +
                        triple.getSecond() + ", " +
                        triple.getThird().getLocalName());
        }
        System.out.println();
    }
    
    /**
     * <h1>Recipe handling</h1>
     * Handles the recipe of a specific product received from a Craftsperson 
     * agent. Adds not yet known recipies to recipe list and replaces expensive
     * recipies with those with a smaller fee.
     * <p>
     * Works under the assumption that there can only exist one recipe for 
     * a single product. The only difference between recipies for a single product
     * is the fee. 
     * 
     * @param recipe Received recipe.
     */
    public void handleProductRecipe(ProductRecipe recipe) {
        AID merchant = recipe.getMerchant();
        Recipe new_recipe = recipe.getRecipe();
        int fee = recipe.getFee();
        
        System.out.println(this.getLocalName() + ": Handling recipe from " + merchant.getLocalName());
        
        // Check if the mage already knows this recipe
        Boolean found = false;
        for(Map.Entry<Integer, Triple<Integer, Recipe, AID>> entry : recipies.entrySet()) {
            Triple<Integer, Recipe, AID> triple = entry.getValue();

            if (new_recipe.getProduct().myEquals(triple.getSecond().getProduct())) {
                found = true;
                if (fee < triple.getFirst()) {
                    triple.setFirst(fee);
                    triple.setSecond(new_recipe);
                    triple.setThird(merchant);
                }
                break;
            }
        }
        
        // If no such recipe was found, add it
        if (!found) {
            recipies.put(new_recipe.getId(), new Triple<>(fee, new_recipe, merchant));
        }
        
        // Log known recipies
        System.out.println(this.getLocalName() + ": New recipe list...");
        recipies.forEach((id, triple) -> {
            System.out.println(this.getLocalName() + ": " + 
                    "ID = " + id + ", " +
                    "fee = " + triple.getFirst() + ", " +
                    "recipe = " + triple.getSecond() + ", " +
                    "craftsperson = " + triple.getThird().getLocalName());
        });
        System.out.println();
        
    }
    
    /**
     * <h1>Recipe assessment</h1>
     * Assesses recipies that are necessery to fulfill win conditions, i.e.
     * recipies for products unavailable for purchase on the market.
     */
    public void requiredRecipeAssessment() {
        
        List<Triple<Integer, Recipe, AID>> recipies_to_assess = new ArrayList<>();
        
        // Check if win is even possible
        // Impossible if there are no recipies for products unavailable on the market.
        Boolean win_possible = true;
        for (Product product : product_list) {
            win_possible = false;
            for(Map.Entry<Integer, Triple<Integer, Recipe, AID>> entry : recipies.entrySet()) {
                Triple<Integer, Recipe, AID> triple = entry.getValue();
                if (product.myEquals(triple.getSecond().getProduct())) {
                    win_possible = true;
                    recipies_to_assess.add(triple);
                    break;
                }
            }
            if (!win_possible)
                break;
        }
        if (!win_possible) {
            System.out.println(this.getLocalName() + ": Some of the products seem to be missing...");
            failMission();
            return;
        }
        
        // Skip if there's no required recipies to assess
        if (recipies_to_assess.isEmpty()) {
            checkFeasibility();
            return;
        }
        
        System.out.println(this.getLocalName() + ": Assessing recipies...\n");
        
        //Assess the recipies for products unavailable on the market
        SequentialBehaviour assess_recipies_sb = new SequentialBehaviour(this);
        for (Triple<Integer, Recipe, AID> triple : recipies_to_assess) {
            SequentialBehaviour assess_recipe_sb = assessSingleRecipe(triple.getSecond());
            addBehaviour(assess_recipe_sb);
            assess_recipies_sb.addSubBehaviour(assess_recipe_sb);
        }
        
        addBehaviour(assess_recipies_sb);
        addBehaviour(new WaitForCraftingPlanBehaviour(this, assess_recipies_sb));
    }
    
    private SequentialBehaviour assessSingleRecipe(Recipe recipe) {
        System.out.println(this.getLocalName() + ": Assessing recipe for \"" + 
                recipe.getProduct().getName() + "\".");
        
        SequentialBehaviour assess_recipe_sb = new SequentialBehaviour(this);
        for (Product ingredient : recipe.parseIngredients()) {
            assess_recipe_sb.addSubBehaviour(findIngredientPrices(ingredient, recipe.getId()));
        }
        return assess_recipe_sb;
        
        // Save to a seperate list (only if they aren't already on the shopping list, check by Product ID)
        
        // Check if all necessary ingredients are on the list
        // If not, tag the recipe as impossible or remove it from recipe list.
        // If yes, tag the recipe with total price of all ingredients.
        
    }
    
    public void checkFeasibility() {
        if (!product_list.isEmpty())
        {
            System.out.println(this.getLocalName() + ": Some of the products seem to be missing...");
            failMission();
            return;
        }
        
        int total = 0;
        
        for (Triple<Integer, Product, AID> triple : shopping_list) {
            total += triple.getFirst();
        }
        
        // TODO: add recipies to feasibility check
        
        System.out.println(this.getLocalName() + ": Getting everything would cost me " + total + " Knuts.");
        System.out.println(this.getLocalName() + ": I have " + pouch + " Knuts to my name.\n");
        
        if (pouch < total)
            failMission();
        else
            commenceShopping();
    }
    
    public void failMission() {
        System.out.println(this.getLocalName() + ": Whatever, I'm heading back home!\n");
    }
    
    public void commenceShopping() {
        System.out.println(this.getLocalName() + ": Commencing shopping!!!\n");
        SequentialBehaviour shopping = new SequentialBehaviour(this);
        
        for (Triple<Integer, Product, AID> triple : shopping_list) {
            RequestProduct action = new RequestProduct();
            action.setProduct(triple.getSecond());
            shopping.addSubBehaviour(new RequestProductBehaviour(this, triple.getThird(), action));
        }
        
        addBehaviour(shopping);
        addBehaviour(new WaitForFinishBehaviour(this, shopping));
    }
    
    public void finish() {
        // TODO: Check if really finished.
        
        System.out.println(this.getLocalName() + ": Done! Let's recap...");
        
        for (Product product : inventory) {
            System.out.println(this.getLocalName() + ": Bought \"" + product.getName() + "\" for " + product.getValue() + " Knuts.");
        }
        
        if (pouch == 0)
            System.out.println(this.getLocalName() + ": My pouch has run out of Knuts!");
        else
            System.out.println(this.getLocalName() + ": I have " + pouch + " Knuts left in my pouch.");
        
        System.out.println(this.getLocalName() + ": Bye!\n");
    }

    public void planCrafting() {
        System.out.println(this.getLocalName() + ": Choosing recipe to craft...");
        
        int best_savings = Integer.MIN_VALUE;
        int best_savings_id = -1;
        
        // Are there any recipies that must be crafted.
        for(Map.Entry<Integer, List<Triple<Integer, Product, AID>>> entry : ingredient_list.entrySet()) {
            Integer id = entry.getKey();
            List<Triple<Integer, Product, AID>> list = entry.getValue();
            
            Product product = recipies.get(id).getSecond().getProduct();
            product.setId(-1);
            
            if (product_list.contains(product)) {
                best_savings_id = id;
                product_list.remove(product);
                break;
            }
        }
        
        if (best_savings_id != -1) {
            crafting_list.add(best_savings_id);
            
            //checkFeasibility();
            
            System.out.println(this.getLocalName() + ": NOT YET IMPLEMENTED, WIN CONDITIONS NOT MET.");
            this.failMission();
            return;
        }
        
        System.out.println(this.getLocalName() + ": Found recipe to craft - " + 
                recipies.get(best_savings_id).getSecond().getProduct().getName());
        
        System.out.println(this.getLocalName() + ": NOT YET IMPLEMENTED, WIN CONDITIONS NOT MET.");
        this.failMission();
    }
}
