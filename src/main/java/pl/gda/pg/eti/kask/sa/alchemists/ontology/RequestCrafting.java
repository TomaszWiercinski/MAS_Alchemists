package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import jade.content.AgentAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request for crafting of a specific product. Contains id of a specific recipe.
 * 
 * @author TomaszWiercinski
 * @see pl.gda.pg.eti.kask.sa.alchemists.agents.Craftsperson
 * @see Recipe
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestCrafting implements AgentAction {
    
    private int recipe_id;
    
}
