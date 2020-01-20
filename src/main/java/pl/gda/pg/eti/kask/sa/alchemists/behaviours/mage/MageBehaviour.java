package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;

/**
 *
 * @author psysiu
 */
public class MageBehaviour extends SimpleBehaviour {

    private final Behaviour behaviour;
    
    private final Mage myAgent;

    public MageBehaviour(Behaviour behaviour, Mage agent) {
        super(agent);
        this.behaviour = behaviour;
        this.myAgent = agent;
    }

    @Override
    public void action() {
        if (!behaviour.done()) {
            System.out.println("Still waiting");
        } else {
            System.out.println("I've got everything! I have " + myAgent.getPouch() + " Knuts left.");
        }
    }

    @Override
    public boolean done() {
        return behaviour.done();
    }
}
