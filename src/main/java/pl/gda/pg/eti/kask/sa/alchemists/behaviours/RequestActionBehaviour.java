package pl.gda.pg.eti.kask.sa.alchemists.behaviours;

import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.UUID;
import java.util.logging.Level;
import lombok.extern.java.Log;
import pl.gda.pg.eti.kask.sa.alchemists.agents.BaseAgent;
import pl.gda.pg.eti.kask.sa.alchemists.ontology.AlchemyOntology;

/**
 *
 * @author psysiu
 */
@Log
public abstract class RequestActionBehaviour<T extends AgentAction, E extends BaseAgent> extends OneShotBehaviour {

    protected final E myAgent;
    
    private final AID participant;

    private final T action;

    public RequestActionBehaviour(E agent, AID participant, T action) {
        this.participant = participant;
        this.action = action;
        this.myAgent = agent;
    }
    
    @Override
    public void action() {
        Action action = new Action(participant, this.action);

        String conversationId = UUID.randomUUID().toString();
        
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(new SLCodec().getName());
        request.setOntology(AlchemyOntology.getInstance().getName());
        request.addReceiver(participant);
        request.setConversationId(conversationId);
        
        try {
            myAgent.getContentManager().fillContent(request, action);
            myAgent.getActiveConversationIds().add(conversationId);
            myAgent.send(request);
            ReceiveResultBehaviour resultBehaviour = createResultBehaviour(conversationId);
            if (getParent() != null && getParent() instanceof SequentialBehaviour) {
                ((SequentialBehaviour)getParent()).addSubBehaviour(resultBehaviour);
            } else {
                myAgent.addBehaviour(resultBehaviour);
            }
        } catch (Codec.CodecException | OntologyException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
        }
    }
    
    protected abstract ReceiveResultBehaviour createResultBehaviour(String conversationId);

}
