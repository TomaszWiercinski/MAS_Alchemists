package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import java.util.logging.Level;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 *
 * @author psysiu
 */
@Log
public class AlchemyOntology extends BeanOntology {

    public static final String NAME = "alchemy-ontology";

    @Getter
    private static final AlchemyOntology instance = new AlchemyOntology(NAME);

    private AlchemyOntology(String name) {
        super(name);
        try {
            add(Product.class);
            add(ProductHerb.class);
            add(ProductPotion.class);
            add(ProductWand.class);
            add(RequestProductInfo.class);
            add(ProductPrices.class);
            add(ProductRecipe.class);
            add(Recipe.class);
            add(RequestProduct.class);
            add(RequestIngredientPrices.class);
            add(IngredientPrices.class);
        } catch (BeanOntologyException ex) {
            log.log(Level.SEVERE, null, ex);
        }

    }

}
