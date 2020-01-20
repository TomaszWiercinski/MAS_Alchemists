package pl.gda.pg.eti.kask.sa.alchemists.agents;

import pl.gda.pg.eti.kask.sa.alchemists.ontology.ProductHerb;

/**
 * <h1>Merchant - Herbalist</h1>
 * Merchant agent selling herbs.
 * 
 * @author TomaszWiercinski
 * @see ProductHerb
 */
public class Herbalist extends Merchant {
    
    public Herbalist() {
        super("herbalist");
    }

    @Override
    protected void setDefaults() {
        products.add(new ProductHerb("Spirit Grass", 5));
        products.add(new ProductHerb("Glitter Berry", 10));
        products.add(new ProductHerb("Dream Mint", 20));
    }

    @Override
    protected void addProduct(String obj_name, int obj_value) {
        products.add(new ProductHerb(obj_name, obj_value));
    }
}
