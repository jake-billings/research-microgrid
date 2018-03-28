package edu.ucdenver.park.microgrid.agents;

import edu.ucdenver.park.microgrid.data.MicrogridGraph;
import edu.ucdenver.park.microgrid.data.abs.Datum;
import edu.ucdenver.park.microgrid.live.MicrogridDatumMessage;
import edu.ucdenver.park.microgrid.live.MicrogridGraphMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;

/**
 * MicrogridSenderAgent
 * <p>
 * abstract class: JADE agent
 * <p>
 * mutable
 * <p>
 * MicrogridSenderAgent is a superclass/example for agents that want to send data to the MicrogridReceiver agent
 * MicrogridSenderAgents send MicrogridGraphMessages periodically
 * MicrogridSenderAgents send MicrogridDatumMessages as data becomes available
 * <p>
 * How to use:
 * subclass this agent
 */
public abstract class MicrogridSenderAgent extends Agent {
    /**
     * receiver
     * <p>
     * AID
     * <p>
     * the agent id (AID) of the MicrogridReceiverAgent that should receive our graph data messages
     */
    private final AID receiver;

    /**
     * subgraph
     * <p>
     * MicrogridGraph
     * <p>
     * the subgraph (subgraph of the entire grid) that this agent knows about
     * should be statically configured in the constructor of a subclass
     */
    private MicrogridGraph subgraph;

    /**
     * gridUpdatePeriod
     * <p>
     * long
     * <p>
     * the time in milliseconds between each message we send to update the grid graph data on the map
     * (this is also the amount of time roughly it will take for us to disappear from the map when shutdown)
     */
    private final long gridUpdatePeriod;

    /**
     * MicrogridSenderAgent()
     * <p>
     * constructor
     *
     * @param receiver         the agent id (AID) of the MicrogridReceiverAgent that should receive our graph data messages
     * @param subgraph         the subgraph (subgraph of the entire grid) that this agent knows about (should be statically configured in the constructor of a subclass)
     * @param gridUpdatePeriod the time in milliseconds between each message we send to update the grid graph data on the map (this is also the amount of time roughly it will take for us to disappear from the map when shutdown)
     */
    public MicrogridSenderAgent(AID receiver, MicrogridGraph subgraph, long gridUpdatePeriod) {
        this.receiver = receiver;
        this.setSubgraph(subgraph);
        this.gridUpdatePeriod = gridUpdatePeriod;
    }

    /**
     * setup()
     * <p>
     * configures this agent and adds its behaviors
     */
    protected void setup() {

        //---Welcome Message---
        //Print a message that says the agent is starting
        System.out.print("MicrogridSenderAgent Starting...");

        //Start the socket.io server
        //server.init();

        //---Add Behaviors---
        addBehaviour(new SendMicrogridGraphMessageBehavior(this, this.gridUpdatePeriod, 1000));

        //---Completion Message---
        //Print a message that we finished setting up
        System.out.println("Done.");
        System.out.println("MicrogridSenderAgent running as: " + getLocalName());
    }

    /**
     * sendDatum()
     * <p>
     * sends a Datum object to the receiver agent via JADE messaging
     * (catches exceptions for ease of use and consistency with SendMicrogridGraphMessageBehavior behavior)
     *
     * @param d the datum object to send
     */
    protected void sendDatum(Datum d) {
        try {
            sendObjectMessage(new MicrogridDatumMessage(d), this.receiver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sendObjectMessage()
     * <p>
     * sends the "content" object to the agent identified by the AID receiver via JADE messaging
     * <p>
     * called by SendMicrogridGraphMessageBehavior and sendDatum
     *
     * @param content  object to send
     * @param receiver agent id of the target receiver (usually this.receiver)
     * @throws IOException throws IOException because serialization could fail
     */
    private void sendObjectMessage(Serializable content, AID receiver) throws IOException {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContentObject(content);
        msg.addReceiver(receiver);
        send(msg);
    }

    //----Getters and Setters----
    public MicrogridGraph getSubgraph() {
        return subgraph;
    }

    public void setSubgraph(MicrogridGraph subgraph) {
        if (subgraph == null) {
            throw new IllegalArgumentException("argument subgraph to MicrogridSenderAgent cannot be null");
        }
        this.subgraph = subgraph;
    }

    /**
     * SendMicrogridGraphMessageBehavior
     * <p>
     * class
     * private internal class: this class exists INSIDE the MicrogridReceiverAgent class and is private to it (it is used only in setup())
     * behavior: this a JADE behavior class
     * <p>
     * This behavior sends our microgrid graph data to the receiver agent every period milliseconds
     * We use MicrogridGraphMessages that expire just after the next is scheduled to be sent (see MicrogridGraphMessage docs)
     * <p>
     * We must send this information periodically because graph data expires on the LiveMicrogridGraph in the receiver agent.
     * This keeps the graph up to date if agents go offline (the subgraph they contribute will eventually be removed)
     */
    private class SendMicrogridGraphMessageBehavior extends TickerBehaviour {
        /**
         * bufferTimeMillis
         * <p>
         * long
         * <p>
         * the amount of bufferTimeMillis time in milliseconds between when our next message is scheduled and the current should expire
         * for instance, consider a period of 10000 ms and a bufferTimeMillis of 2000 ms
         * if we stop sending data, our data will expire after 120000 ms
         */
        private final long bufferTimeMillis;

        /**
         * SendMicrogridGraphMessageBehavior()
         *
         * @param a                agent (this when in setup())
         * @param bufferTimeMillis the amount of buffer time in milliseconds between when our next message is scheduled and the current should expire
         */
        private SendMicrogridGraphMessageBehavior(MicrogridSenderAgent a, long period, long bufferTimeMillis) {
            super(a, period);
            this.bufferTimeMillis = bufferTimeMillis;
        }

        @Override
        protected void onTick() {
            try {
                sendObjectMessage(new MicrogridGraphMessage(getSubgraph(), System.currentTimeMillis() + getPeriod() + bufferTimeMillis), receiver);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
