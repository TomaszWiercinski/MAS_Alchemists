package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains prices of a single ingredient, its name, id of the recipe and the 
 * AID of the merchant agent selling this ingredient.
 * 
 * @author TomaszWiercinski
 * @see ProductPrices
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class IngredientPrices extends ProductPrices {

    private int recipe_id;
    
}