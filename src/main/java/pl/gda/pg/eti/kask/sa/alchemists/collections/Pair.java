package pl.gda.pg.eti.kask.sa.alchemists.collections;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 *
 * @author TomaszWiercinski
 */
@AllArgsConstructor
@Getter
public class Pair<F extends Comparable<F>, S> 
        implements Comparable<Pair<F,S>> {
    
    private F first;
    private S second;
    
    @Override
    public int compareTo(Pair<F, S> p) {
        if (getFirst() == null || p.getFirst() == null) {
            return 0;
        }
        return getFirst().compareTo(p.getFirst());
    }
}
