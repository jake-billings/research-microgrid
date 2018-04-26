/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.agents.core;

import edu.ucdenver.park.microgrid.live.LiveMicrogridGraph;
import edu.ucdenver.park.microgrid.message.Message;
import edu.ucdenver.park.microgrid.message.MicrogridDatumMessage;
import edu.ucdenver.park.microgrid.message.MicrogridGraphMessage;
import edu.ucdenver.park.microgrid.socketioserver.MicrogridSocketIOServer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
    private LiveMicrogridGraph liveGrid = new LiveMicrogridGraph();

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
    private MicrogridSocketIOServer server = new MicrogridSocketIOServer(liveGrid, (short) 4000);

    /**
     * jadeMessageReceiveQueue
     * <p>
     * BlockingQueue<ACLMessage>
     * <p>
     * when we receive message from JADE, we add them to this queue for processing
     * <p>
     * they are processed in a separate behavior
     * <p>
     * the parameter to the LinkedBlockingQueue constructor is the queue capacity; if the number of queued messages
     * for processing exceeds this amount, we give up, clear the queue, and start over
     * <p>
     * this prevents us from getting behind on our processing and lagging farther and farther behind from reality
     * <p>
     * I chose 20 arbitrarily; this number can be tuned to optimize performance
     */
    private final BlockingQueue<ACLMessage> jadeMessageReceiveQueue = new LinkedBlockingQueue<ACLMessage>(20);

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
        addBehaviour(new ProcessIncomingMessageBehavior(this, 1));
        addBehaviour(new ProcessGridGraphStateBehavior(this, 5000));
        addBehaviour(new ProcessGridMeasurementStateBehavior(this, 701)); //make sure this period is prime and small (see not in constructor docs)

        //---Final Linking---
        //Pass datum events from the liveGrid to the SocketIO server
        liveGrid.registerDatumHadler(server);

        //---Completion Message---
        //Print a message that we finished setting up
        System.out.println("Done.");
        System.out.println("MicrogridReceiverAgent running as: " + getLocalName());
    }

    /**
     * ReceiveBehavior
     * <p>
     * class
     * private internal class: this class exists INSIDE the MicrogridReceiverAgent class and is private to it (it is used only in setup())
     * behavior: this a JADE behavior class
     * <p>
     * This CyclicBehavior continuously reads messages via JADE's messaging system and passes the messages to our queue,
     * which we manage; if the queue fills up, we clear it and start adding new data
     * <p>
     * the behavior ProcessIncomingMessageBehavior manages messages from the incoming message queue
     */
    private class ReceiveBehavior extends CyclicBehaviour {

        public void action() {
            ACLMessage msg;
            while ((msg = receive()) != null) {
                if (!jadeMessageReceiveQueue.offer(msg)) {
                    System.out.println("Warning: Receiver had to dump incoming message queue");
                    jadeMessageReceiveQueue.clear();
                    jadeMessageReceiveQueue.add(msg);
                }
            }
            block(1);
        }
    }

    /**
     * ProcessIncomingMessageBehavior
     * <p>
     * class
     * private internal class: this class exists
     * <p>
     * this behavior continuously processes the top element of the incoming messaging queue
     * maintaining our own queue prevents us from falling behind on processing
     *
     * this behavior can be "tuned" for CPU performance
     */
    private class ProcessIncomingMessageBehavior extends CyclicBehaviour {
        /**
         * blockTime
         * <p>
         * long
         * <p>
         * this is the parameter to block() that dictates how long we wait between reading messages
         * this helps us not peg the CPU while reading our messages but slows down our message
         * processing ability
         * <p>
         * higher blockTime: better cpu performance/slower message processing/higher risk of queue overflow
         * lower blockTime: worse cpu performance/faster message processing/lower risk of queue overflow
         * <p>
         * this number should be significantly lower than the time interval between repeating messages
         * that the sender will receive
         * <p>
         * E.g. if message send ever 10,000 ms, this should be 1,000 ms or less
         */
        private final long blockTime;

        ProcessIncomingMessageBehavior(Agent a, long blockTime) {
            super(a);
            this.blockTime = blockTime;
        }

        public void action() {
            ACLMessage msg;

            while ((msg = jadeMessageReceiveQueue.poll()) != null) {
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(msg.getByteSequenceContent());
                    ObjectInputStream in = new ObjectInputStream(bais);
                    Message contentObject = Message.read(in);

                    if (contentObject instanceof MicrogridGraphMessage) {
                        liveGrid.receiveMessage((MicrogridGraphMessage) contentObject);
                    } else if (contentObject instanceof MicrogridDatumMessage) {
                        liveGrid.receiveMessage((MicrogridDatumMessage) contentObject);
                    } else {
                        System.err.println("WARNING: Unknown message received in MicrogridReceiverAgent");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            block(this.blockTime);
        }
    }

    /**
     * ProcessGridGraphStateBehavior
     * <p>
     * class
     * private internal class: this class exists INSIDE the MicrogridReceiverAgent class and is private to it (it is used only in setup())
     * behavior: this a JADE behavior class
     * <p>
     * This TickerBehavior runs on a regular interval
     * <p>
     * the purpose of this behavior is to recompute the current microgrid graph state and send it to SocketIO clients at a regular
     * interval instead of immediately when receiving updates
     * this reduces CPU load when we have many sender agents sending us data all at the same time
     */
    private class ProcessGridGraphStateBehavior extends TickerBehaviour {
        ProcessGridGraphStateBehavior(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            server.onMicrogridGraph(liveGrid.getCurrentState());
        }
    }

    /**
     * ProcessGridMeasurementStateBehavior
     * <p>
     * class
     * private internal class: this class exists INSIDE the MicrogridReceiverAgent class and is private to it (it is used only in setup())
     * behavior: this a JADE behavior class
     * <p>
     * This TickerBehavior runs on a regular interval
     * <p>
     * the purpose of this behavior is to recompute the current microgrid graph measurement state state and send it to
     * SocketIO clients at a regular interval instead of immediately when receiving updates
     * this reduces CPU load when we have many sender agents sending us data all at the same time
     * <p>
     * Node on period:
     * this process runs every "period" milliseconds. this is configured in the constructor that creates this behavior
     * if you are measuring wave forms, make sure to use a prime number that is far smaller than the wavelength of
     * the wave if you wish to see changes in the values in the client display. if the wave period and the measurement
     * period share factors, you may see misleading values on the client
     */
    private class ProcessGridMeasurementStateBehavior extends TickerBehaviour {
        ProcessGridMeasurementStateBehavior(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            server.onMicrogridNodeSnapshots(liveGrid.getCurrentStateForAllNodes());
        }
    }
}
