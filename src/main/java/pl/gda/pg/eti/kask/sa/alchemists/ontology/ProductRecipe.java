package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import jade.content.Concept;
import jade.core.AID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author TomaszWiercinski
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ProductRecipe implements Concept {
    
    private Recipe recipe;
    private AID merchant;
    private int fee;
    
}
