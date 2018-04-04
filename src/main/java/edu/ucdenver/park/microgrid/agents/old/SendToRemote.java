/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.agents.old;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.behaviours.*;

public class SendToRemote extends Agent {

    double result = 2345.59475;
    protected void setup() {

        // Add a TickerBehaviour that schedules a request to seller agents every minute
        addBehaviour(new TickerBehaviour(this, 15000) { // 10 seconds
            protected void onTick() {

                // Perform the request
                myAgent.addBehaviour(new RequestPerformer());
            }
        });
    }

    protected void takeDown() {
        // Printout a dismissal message
        System.out.println("Agent "+getAID().getName()+" terminating.");
    }


    private class RequestPerformer extends Behaviour {

        public void action () {

            ACLMessage msgp = new ACLMessage(ACLMessage.INFORM);
            msgp.setContent(String.valueOf(result));
            AID remoteAMSf = new AID("Anas@10.20.102.93:1099/JADE", AID.ISGUID);
            remoteAMSf.addAddresses("http://NC2611-PC-21.ucdenver.pvt:7778/acc");
            msgp.addReceiver(remoteAMSf);
            send(msgp);
            System.out.println("\nMessage Sent to " + remoteAMSf);
            doDelete();


            //AID r = new AID("Anas@10.20.102.42:1099/JADE", AID.ISGUID);
            //r.addAddresses("http://NC2611-PC-18.ucdenver.pvt:7778/acc");
            // ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            // msg.addReceiver(r);
            // msg.setContent("Hello.!");
            // send(msg);
            // System.out.println("\nMessage Sent to " + r);



        }

        @Override
        public boolean done() {
            return false;
        }

        ;
    }
}



