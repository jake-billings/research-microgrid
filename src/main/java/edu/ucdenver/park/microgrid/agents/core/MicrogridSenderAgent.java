/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.agents.core;

import edu.ucdenver.park.microgrid.data.MicrogridDatum;
import edu.ucdenver.park.microgrid.data.MicrogridGraph;
import edu.ucdenver.park.microgrid.data.abs.Datum;
import edu.ucdenver.park.microgrid.message.MicrogridDatumMessage;
import edu.ucdenver.park.microgrid.message.MicrogridGraphMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
 * implements a message send queue and send behavior instead of direct sending; this was necessary because repeated
 * send requests over a network caused reliability issues
 * <p>
 * How to use:
 * subclass this agent
 *
 * @author Jake Billings
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

    private final BlockingQueue<ACLMessage> jadeMessageSendQueue;

    /**
     * MicrogridSenderAgent()
     * <p>
     * constructor
     *
     * @param receiver         the agent id (AID) of the MicrogridReceiverAgent that should receive our graph data messages
     * @param subgraph         the subgraph (subgraph of the entire grid) that this agent knows about (should be statically configured in the constructor of a subclass)
     * @param gridUpdatePeriod the time in milliseconds between each message we send to update the grid graph data on the map (this is also the amount of time roughly it will take for us to disappear from the map when shutdown)
     *                         cannot be less than 1000 ms (to preserve network bandwidth)
     */
    public MicrogridSenderAgent(AID receiver, MicrogridGraph subgraph, long gridUpdatePeriod) {
        this.setSubgraph(subgraph);
        if (receiver == null)
            throw new IllegalArgumentException("receiver cannot be null when creating MicrogridSenderAgent");
        if (gridUpdatePeriod < 1000)
            throw new IllegalArgumentException("gridUpdatePeriod cannot be less than 1000 when creating MicrogridSenderAgent");
        this.receiver = receiver;
        this.gridUpdatePeriod = gridUpdatePeriod;

        this.jadeMessageSendQueue = new LinkedBlockingQueue<ACLMessage>(20);
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

        //---Add Behaviors---
        addBehaviour(new SendMicrogridGraphMessageBehavior(this, this.gridUpdatePeriod, 2000));
        addBehaviour(new SendJadeMessagesBehavior(this, 10));

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
     * <p>
     * note: this doesn't "actually" send the datum; it calls sendObjectMessage()
     * sendObjectMessage() doesn't "actually" send it either; it adds it to the message send queue,
     * and the send behavior sends it
     *
     * @param d the datum object to send
     */
    protected void sendDatum(MicrogridDatum d) {
        try {
            sendObjectMessage(new MicrogridDatumMessage(d), this.receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * sendObjectMessage()
     * <p>
     * sends the "content" object to the agent identified by the AID receiver via JADE messaging
     * <p>
     * called by SendMicrogridGraphMessageBehavior and sendDatum
     * <p>
     * adds the content as an ACLMessage to the message send queue (which is sent by the message send behavior)
     * if the queue is full, we filter out all the data messages and try to add it
     * if it's still full, we filter out all other message and it it
     * this prioritizes graph messages, which are infrequent but critical
     *
     * @param content  object to send
     * @param receiver agent id of the target receiver (usually this.receiver)
     * @throws IOException throws IOException because serialization could fail
     */
    private void sendObjectMessage(Externalizable content, AID receiver) throws IOException, UnreadableException {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        content.writeExternal(out);
        out.close();
        msg.setByteSequenceContent(baos.toByteArray());

        msg.addReceiver(receiver);

        //Try to add the message to the send queue; if it doesn't work, the queue is full
        // if the queue is full, there's a problem. we're not gonna get all the data through
        // we know new data is the priority, so clear the queue and add our message
        if (!jadeMessageSendQueue.offer(msg)) {
            System.out.println("Warning: Sender had to dump send queue");
//            Collection<ACLMessage> graphMessages = new HashSet<ACLMessage>();
//            Iterator<ACLMessage> iter = jadeMessageSendQueue.iterator();
//            while (iter.hasNext()) {
//                ACLMessage queuedMessage = iter.next();
//                Serializable contentObject = queuedMessage.getContentObject();
//                if (contentObject instanceof MicrogridGraphMessage) {
//                    graphMessages.add(queuedMessage);
//                }
//            }
//            jadeMessageSendQueue.clear();
//            jadeMessageSendQueue.addAll(graphMessages);
//            if (!jadeMessageSendQueue.offer(msg)) {
                jadeMessageSendQueue.clear();
                jadeMessageSendQueue.add(msg);
//            }
        }
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * SendJadeMessagesBehavior
     * <p>
     * the behavior take "ACLMessage" elements from the jade message queue and sends them using jade messages
     * <p>
     * use sendObjectMessage() to add jade messages to this queue
     */
    private class SendJadeMessagesBehavior extends CyclicBehaviour {
        /**
         * blockingTime
         * <p>
         * long
         * <p>
         * the amount of time to block for between sending messages
         * lower = faster speed but higher risk of clogging receiving buffer
         * higher = less CPU usage on this machine but lower throughput/higher risk of buffer overflow
         */
        private final long blockingTime;

        /**
         * SendJadeMessagesBehavior()
         * <p>
         * constructor
         *
         * @param a            the agent (this)
         * @param blockingTime see blockingTime docs
         */
        public SendJadeMessagesBehavior(Agent a, long blockingTime) {
            super(a);
            this.blockingTime = blockingTime;
        }

        @Override
        public void action() {
            if (jadeMessageSendQueue.size() > 0) {
                send(jadeMessageSendQueue.remove());
            }
            block(this.blockingTime);
        }
    }
}
