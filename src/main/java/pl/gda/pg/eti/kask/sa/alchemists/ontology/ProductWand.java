package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import lombok.NoArgsConstructor;

/**
 *
 * @author TomaszWiercinski
 */
@NoArgsConstructor
public class ProductWand extends Product {
    
    public ProductWand(String name, int value)
    {
        super(name, value, "wandseller", "w", false);
    }
    
    public ProductWand(String name)
    {
        super(name, 0, "wandseller", "w", false);
    }
    
}
