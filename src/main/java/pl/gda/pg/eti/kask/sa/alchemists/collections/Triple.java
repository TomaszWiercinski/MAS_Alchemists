package pl.gda.pg.eti.kask.sa.alchemists.collections;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 *
 * @author TomaszWiercinski
 */
@AllArgsConstructor
@Getter
@Setter
public class Triple<F extends Comparable<F>, S, T> 
        implements Comparable<Triple<F,S,T>> {
    
    private F first;
    private S second;
    private T third;
    
    @Override
    public int compareTo(Triple<F, S, T> p) {
        if (getFirst() == null || p.getFirst() == null) {
            return 0;
        }
        return getFirst().compareTo(p.getFirst());
    }
}
