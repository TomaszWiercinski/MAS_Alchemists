package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import jade.content.Concept;
import jade.core.AID;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.gda.pg.eti.kask.sa.alchemists.collections.Pair;

/**
 * Contains prices of a single product, its name and the AID of the merchant 
 * agent selling this product.
 * 
 * @author TomaszWiercinski
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ProductPrices implements Concept {

    protected String product_name = "";
    protected String list = "";
    protected AID merchant;
    
    public void add(Product product)
    {
        if (product_name.isEmpty())
            product_name = product.getName();
        
        if (list.isEmpty())
            list += product.getValue() + ";" + product.getId();
        else
            list += "," + product.getValue() + ";" + product.getId();
    }
    
    public List<Pair<Integer, Integer>> getParsedPrices() {
        String[] str_price_list = list.split(",");
        List<Pair<Integer, Integer>> pair_price_list = new ArrayList<>();
        
        for (String str_price : str_price_list)
        {
            String[] price_id_pair = str_price.split(";");
            pair_price_list.add(new Pair<>(Integer.parseInt(price_id_pair[0]), 
                    Integer.parseInt(price_id_pair[1])));
        }
        
        return pair_price_list;
    }
}
