package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import lombok.NoArgsConstructor;

/**
 *
 * @author TomaszWiercinski
 */
@NoArgsConstructor
public class ProductPotion extends Product {
    
    public ProductPotion(String name, int value)
    {
        super(name, value, "alchemist", "p", true);
    }
    
    public ProductPotion(String name)
    {
        super(name, 0, "alchemist", "p", true);
    }
    
}
