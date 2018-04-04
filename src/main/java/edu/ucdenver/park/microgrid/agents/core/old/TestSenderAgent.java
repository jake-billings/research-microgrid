/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.agents.core.old;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * TestSenderAgent
 *
 * class: JADE agent
 *
 * test agent that sends a string to "jake" every 2 seconds
 *
 * todo remove because this isn't useful; it's just here for reference
 *
 * @author Jake Billings
 */
public class TestSenderAgent extends Agent {
    protected void setup() {

        //---Welcome Message---
        //Print a message that says the agent is starting
        System.out.print("TestSenderAgent Starting...");
        //---Add Behaviors---
        addBehaviour(new TickerBehaviour(this, 2000) {
            public void onTick() {
                System.out.println("Sending...");

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent("bla...bla...bla");
                msg.addReceiver(new AID("jake", AID.ISLOCALNAME));
                send(msg);
            }
        });

        //---Completion Message---
        //Print a message that we finished setting up
        System.out.println("Done.");
        System.out.println("TestSenderAgent running as: " + getLocalName());
    }
}
