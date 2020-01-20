package pl.gda.pg.eti.kask.sa.alchemists.agents;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.AlchemyOntology;

/**
 *
 * @author psysiu
 */
public class BaseAgent extends Agent {

    @Getter
    protected final List<String> activeConversationIds = new ArrayList<>();

    public BaseAgent() {
    }

    @Override
    protected void setup() {
        super.setup();
        getContentManager().registerLanguage(new SLCodec());
        getContentManager().registerOntology(AlchemyOntology.getInstance());
    }

}
