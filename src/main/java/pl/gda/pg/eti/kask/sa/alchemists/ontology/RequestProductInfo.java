package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import jade.content.AgentAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author TomaszWiercinski
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestProductInfo implements AgentAction {
    
    private Product product;
    
}
