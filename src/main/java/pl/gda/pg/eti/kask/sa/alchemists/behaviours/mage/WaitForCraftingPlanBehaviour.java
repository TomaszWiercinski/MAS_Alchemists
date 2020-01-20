package pl.gda.pg.eti.kask.sa.alchemists.behaviours.mage;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import pl.gda.pg.eti.kask.sa.alchemists.agents.Mage;

/**
 * Waits for the completion of all passed behaviours and runs the planCrafting 
 * method.
 * 
 * @author TomaszWiercinski
 */
public class WaitForCraftingPlanBehaviour extends SimpleBehaviour {

    private final Behaviour[] behaviour;
    private final Mage myAgent;
    private Boolean done = false;

    public WaitForCraftingPlanBehaviour(Mage agent, Behaviour ... behaviour) {
        super(agent);
        this.behaviour = behaviour;
        this.myAgent = agent;
    }

    @Override
    public void action() {
        
        done = true;
        for (Behaviour b : behaviour) {
            if (!b.done())
            {
                done = false;
                break;
            }
        }
        
        if (done) {
            System.out.println(myAgent.getLocalName() + ": Wait for finish complete.\n");
            myAgent.planCrafting();
        }
    }

    @Override
    public boolean done() {
        return done;
    }
}
