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
public abstract class FindServiceBehaviour extends OneShotBehaviour {

    private final String serviceType;

    public FindServiceBehaviour(Agent agent, String serviceType) {
        super(agent);
        this.serviceType = serviceType;
    }

    @Override
    public void action() {
        DFAgentDescription dfad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        dfad.addServices(sd);
        try {
            DFAgentDescription[] services = DFService.search(myAgent, dfad);
            onResult(services);
        } catch (FIPAException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    protected abstract void onResult(DFAgentDescription[] services);
}
