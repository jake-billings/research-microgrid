/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.agents.core;

import edu.ucdenver.park.microgrid.live.LiveMicrogridGraph;
import edu.ucdenver.park.microgrid.message.MicrogridDatumMessage;
import edu.ucdenver.park.microgrid.message.MicrogridGraphMessage;
import edu.ucdenver.park.microgrid.socketioserver.MicrogridSocketIOServer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * MicrogridReceiverAgent
 * <p>
 * class: JADE agent
 * <p>
 * This is the Jade agent that receives data from other controller agents and passes it to our Socket.IO server
 * <p>
 * This is the entry point to the program.
 * <p>
 * This ties everything together.
 *
 * @author Jake Billings
 */
public class MicrogridReceiverAgent extends Agent {
    /**
     * liveGrid
     * <p>
     * LiveMicrogridGraph
     * <p>
     * see docs for LiveMicrogridGraph
     * <p>
     * this object accepts message events that we get from jade a processes them into
     * a graph state
     * <p>
     * it also processes events and sends them to SocketIOServer (since SocketIOServer gets a copy and
     * registers itself some event listeners)
     * <p>
     * The job of setup in this agent is to create a SocketIOServer with a reference to this liveGrid
     * The job of this agent is to parse Message object from jade and call liveGrid.receiveMessage()
     * the socket server will then receive those events and pass them to the frontend for rendering
     */
    LiveMicrogridGraph liveGrid = new LiveMicrogridGraph();

    /**
     * server
     * <p>
     * MicrogridSocketIOServer
     * <p>
     * see docs for MicrogridSocketIOServer
     * <p>
     * this object manages the Socket.IO connection to the frontend javascript that
     * renders a map of this data
     * <p>
     * it has a reference to the liveGrid and registers its own event handlers
     * <p>
     * when we update liveGrid, server gets the update and forwards it as JSON to the frontend via Socket.io
     */
    MicrogridSocketIOServer server = new MicrogridSocketIOServer(liveGrid, (short) 4000);

    /**
     * setup()
     * <p>
     * setup this data receiver object by starting the Socket.IO server
     */
    protected void setup() {
        //---Welcome Message---
        //Print a message that says the agent is starting
        System.out.print("MicrogridReceiverAgent Starting...");

        //Start the socket.io server
        server.init();

        //---Add Behaviors---
        addBehaviour(new ReceiveBehavior());

        //---Completion Message---
        //Print a message that we finished setting up
        System.out.println("Done.");
        System.out.println("MicrogridReceiverAgent running as: " + getLocalName());
    }

    /**
     * ReceiveBehavior
     * <p>
     * class
     *  private internal class: this class exists INSIDE the MicrogridReceiverAgent class and is private to it (it is used only in setup())
     *  behavior: this a JADE behavior class
     * <p>
     * This CyclicBehavior continuously reads messages via JADE's messaging system and passes the messages
     * back into LiveMicrogridGraph
     */
    private class ReceiveBehavior extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                try {
                    Object contentObject = msg.getContentObject();
                    if (contentObject instanceof MicrogridGraphMessage) {
                        liveGrid.receiveMessage((MicrogridGraphMessage) contentObject);
                    } else if (contentObject instanceof MicrogridDatumMessage) {
                        liveGrid.receiveMessage((MicrogridDatumMessage) contentObject);
                    } else {
                        System.err.println("WARNING: Unknown message received in MicrogridReceiverAgent");
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
            block();
        }
    }
}
