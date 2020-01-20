package pl.gda.pg.eti.kask.sa.alchemists.behaviours;

import jade.content.ContentElement;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
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
public abstract class ReceiveResultBehaviour<E extends BaseAgent> extends Behaviour {

    private boolean done = false;

    protected final E myAgent;

    protected final String conversationId;

    protected MessageTemplate mt;

    public ReceiveResultBehaviour(E agent, String conversationId) {
        super(agent);
        this.myAgent = agent;
        this.conversationId = conversationId;
    }

    @Override
    public void onStart() {
        super.onStart();
        mt = MessageTemplate.MatchConversationId(conversationId);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            done = true;
            try {
                ContentElement ce = myAgent.getContentManager().extractContent(msg);
                handleResult((Predicate) ce, msg.getSender());
            } catch (Codec.CodecException | OntologyException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean done() {
        return done;
    }

    protected abstract void handleResult(Predicate predicate, AID participant);

}
