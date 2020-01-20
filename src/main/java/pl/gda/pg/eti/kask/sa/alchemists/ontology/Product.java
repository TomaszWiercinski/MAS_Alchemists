package pl.gda.pg.eti.kask.sa.alchemists.ontology;

import jade.content.Concept;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author TomaszWiercinski
 */
@Getter
@Setter
@EqualsAndHashCode
public class Product implements Concept {
    
    protected String name;
    protected int value;
    protected String service_title;
    protected String type;
    protected Boolean consumable;
    protected Integer id;
    private static Integer count = 0;
    
    public Product(String name, int value, String service_title, String type, Boolean consumable) {
        this.name = name;
        this.value = value;
        this.service_title = service_title;
        this.type = type;
        this.consumable = consumable;
        id = count;
        count++;
    }
    
    public Product() {
        id = count;
        count++;
    }
    
    public Product(String str) {
        String[] str_split = str.split(";");
        
        type = str_split[0];
        name = str_split[1];
        value = Integer.parseInt(str_split[2]);
        service_title = str_split[3];
        consumable = Boolean.parseBoolean(str_split[4]);
        id = Integer.parseInt(str_split[5]);
    }
    
    @Override
    public String toString() {
        return type + ";" + name + ";" + value + ";" + service_title + ";" + consumable + ";" + id;
    }
    
    public Boolean myEquals(Product product) {
        return product.getName().equals(name) && product.getType().equals(type);
    }
}
