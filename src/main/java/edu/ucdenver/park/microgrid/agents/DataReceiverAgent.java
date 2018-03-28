package edu.ucdenver.park.microgrid.agents;

import edu.ucdenver.park.microgrid.live.LiveMicrogridGraph;
import edu.ucdenver.park.microgrid.socketioserver.MicrogridSocketIOServer;
import jade.core.Agent;

/**
 * DataReceiverAgent
 *
 * class: JADE agent
 *
 * This is the Jade agent that receives data from other controller agents and passes it to our Socket.IO server
 *
 * This is the entry point to the program.
 *
 * This ties everything together.
 */
public class DataReceiverAgent extends Agent {
    /**
     * liveGrid
     *
     * LiveMicrogridGraph
     *
     * see docs for LiveMicrogridGraph
     *
     * this object accepts message events that we get from jade a processes them into
     *  a graph state
     *
     * it also processes events and sends them to SocketIOServer (since SocketIOServer gets a copy and
     *  registers itself some event listeners)
     *
     * The job of setup in this agent is to create a SocketIOServer with a reference to this liveGrid
     * The job of this agent is to parse Message object from jade and call liveGrid.receiveMessage()
     *  the socket server will then receive those events and pass them to the frontend for rendering
     */
    LiveMicrogridGraph liveGrid = new LiveMicrogridGraph();

    /**
     * server
     *
     * MicrogridSocketIOServer
     *
     * see docs for MicrogridSocketIOServer
     *
     * this object manages the Socket.IO connection to the frontend javascript that
     *  renders a map of this data
     *
     * it has a reference to the liveGrid and registers its own event handlers
     *
     * when we update liveGrid, server gets the update and forwards it as JSON to the frontend via Socket.io
     */
    MicrogridSocketIOServer server = new MicrogridSocketIOServer(liveGrid, (short) 4000);

    /**
     * setup()
     *
     * setup this data receiver object by starting the Socket.IO server
     */
    protected void setup() {
        //Print a message that
        System.out.print("DataReceiverAgent Starting...");

        //Start the server
        server.init();

        //Print a message that we finished setting up
        System.out.println("Done.");
        System.out.println("DataReceiverAgent running as: "+ getLocalName());
    }
}
