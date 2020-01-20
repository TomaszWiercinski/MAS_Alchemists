package pl.gda.pg.eti.kask.sa.alchemists.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.logging.Level;
import lombok.extern.java.Log;

/**
 *
 * @author psysiu
 */
@Log
public class RegisterServiceBehaviour extends OneShotBehaviour {

    private final String serviceType;

    public RegisterServiceBehaviour(Agent agent, String serviceType) {
        super(agent);
        this.serviceType = serviceType;
    }

    @Override
    public void action() {
        DFAgentDescription dfad = new DFAgentDescription();
        dfad.setName(myAgent.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        sd.setName(myAgent.getName() + "-" + serviceType);
        dfad.addServices(sd);
        try {
            DFService.register(myAgent, dfad);
        } catch (FIPAException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

}
