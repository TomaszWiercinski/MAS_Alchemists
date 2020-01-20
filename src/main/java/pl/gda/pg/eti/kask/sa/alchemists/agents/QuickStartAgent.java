package pl.gda.pg.eti.kask.sa.alchemists.agents;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h1>Start scenario</h1>
 * Quickly starts all agents according to specifications given in a text file.
 * 
 * @author TomaszWiercinski
 */
public class QuickStartAgent extends Agent {
    
    List<AgentController> agents = new ArrayList<>();
    
    public QuickStartAgent() {
    }

    @Override
    protected void setup() {
        super.setup();
        
        Object[] args = getArguments();
        
        File file = new File(args[0].toString()); 
        try {
            Scanner sc = new Scanner(file); 

            while (sc.hasNextLine()) {
                String[] new_agent = sc.nextLine().split("/");
                
                String nickname = new_agent[0];
                String classname = new_agent[1];
                Object[] agent_args;
                if (new_agent.length < 3) {
                    agent_args = new Object[0];
                } else {
                    agent_args = new_agent[2].split(",");
                }
                
                Thread.sleep(100);
                
                AgentController new_agentc = this.getContainerController().createNewAgent(nickname, classname, agent_args);
                new_agentc.start();
                agents.add(new_agentc);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Couldn't find \"" + args[0].toString() + "\".");
        } catch (StaleProxyException ex) {
            System.out.println("I don't even, like, know what happened but something, like, went wrong???");
            System.out.println(ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(QuickStartAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Kills all the started agents.
     */
    @Override
    public void doDelete() {
        try {
            for (AgentController ac : agents) {
                ac.kill();
            }
        } catch (StaleProxyException ex) {
            System.out.println("I don't even, like, know what happened but something, like, went wrong???");
            System.out.println(ex);
        }
        
        super.doDelete(); //To change body of generated methods, choose Tools | Templates.
    }
}
