package pl.gda.pg.eti.kask.sa.alchemists.behaviours;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Level;
import lombok.extern.java.Log;
import pl.gda.pg.eti.kask.sa.alchemists.agents.BaseAgent;

/**
 *
 * @author psysiu
 */
@Log
public abstract class WaitingBehaviour<E extends BaseAgent> extends CyclicBehaviour {

    protected final E myAgent;

    public WaitingBehaviour(E agent) {
        super(agent);
        myAgent = agent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchAll();
        for (String id : myAgent.getActiveConversationIds()) {
            mt = MessageTemplate.and(mt, MessageTemplate.not(MessageTemplate.MatchConversationId(id)));
        }
        ACLMessage message = myAgent.receive(mt);
        if (message != null) {
            try {
                ContentElement ce = myAgent.getContentManager().extractContent(message);
                if (ce instanceof Action) {
                    action((Action) ce, message.getConversationId(), message.getSender());
                }
            } catch (Codec.CodecException | OntologyException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    protected abstract void action(Action action, String conversationId, AID participant);

}
