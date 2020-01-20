package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import jade.content.AgentAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request for ingredient prices sent to merchant agents. Contains information 
 * about the required ingredient and recipe id.
 * 
 * @author TomaszWiercinski
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestIngredientPrices implements AgentAction {
    
    private Product product;
    private int recipe_id;
    
}
