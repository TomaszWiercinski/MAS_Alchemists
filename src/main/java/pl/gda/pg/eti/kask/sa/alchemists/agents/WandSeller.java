package pl.gda.pg.eti.kask.sa.alchemists.agents;

import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductWand;

/**
 * <h1>Merchant - Wand Seller</h1>
 * Merchant agent selling wands.
 * 
 * @author TomaszWiercinski
 * @see ProductWand
 */
public class WandSeller extends Merchant {
    
    public WandSeller() {
        super("wandseller");
    }

    @Override
    protected void setDefaults() {
        products.add(new ProductWand("Oak Wand", 10));
        products.add(new ProductWand("Sandalwood Wand", 20));
    }
    
    @Override
    protected void addProduct(String obj_name, int obj_value) {
        products.add(new ProductWand(obj_name, obj_value));
    }

}
