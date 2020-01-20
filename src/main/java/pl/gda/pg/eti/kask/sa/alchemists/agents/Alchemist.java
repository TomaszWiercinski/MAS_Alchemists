package pl.gda.pg.eti.kask.sa.alchemists.agents;

import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductPotion;

/**
 * <h1>Merchant - Alchemist</h1>
 * Merchant agent selling potions.
 * 
 * @author TomaszWiercinski
 * @see ProductPotion
 */
public class Alchemist extends Merchant<ProductPotion> {
    
    public Alchemist() {
        super("alchemist");
    }

    @Override
    protected void setDefaults() {
        products.add(new ProductPotion("Spirited Potion", 15));
        products.add(new ProductPotion("Uplifting Potion", 15));
        products.add(new ProductPotion("The Dreamweaver's Spirit", 50));
    }

    @Override
    protected void addProduct(String obj_name, int obj_value) {
        products.add(new ProductPotion(obj_name, obj_value));
    }
}
