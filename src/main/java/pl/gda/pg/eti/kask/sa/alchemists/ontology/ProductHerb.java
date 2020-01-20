package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import lombok.NoArgsConstructor;

/**
 *
 * @author TomaszWiercinski
 */
@NoArgsConstructor
public class ProductHerb extends Product {
    
    public ProductHerb(String name, int value)
    {
        super(name, value, "herbalist", "h", true);
    }
    
    public ProductHerb(String name)
    {
        super(name, 0, "herbalist", "h", true);
    }
    
}
