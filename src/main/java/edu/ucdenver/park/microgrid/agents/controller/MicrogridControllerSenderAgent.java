package edu.ucdenver.park.microgrid.agents.controller;

import edu.ucdenver.park.microgrid.agents.core.MicrogridSenderAgent;
import edu.ucdenver.park.microgrid.comporthandler2.ControllerDataListener;
import edu.ucdenver.park.microgrid.comporthandler2.Handler2;
import edu.ucdenver.park.microgrid.data.*;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import java.util.HashSet;
import java.util.Set;

/**
 * MicrogridControllerSenderAgent
 * <p>
 * class
 * <p>
 * JADE agent
 * <p>
 * this is the real sender agent that sends real data from a real controller
 * <p>
 * this class interfaces the modified version of Amine's code from the "comporthandler2" package and the Jake's
 * MicrogridSenderAgent to send real data via JADE.
 *
 * @author Jake Billings
 */
public class MicrogridControllerSenderAgent extends MicrogridSenderAgent {
    /**
     * data
     * <p>
     * int
     * <p>
     * this is necessary to make the comporthandler library work
     * in my opinion, it's bad practice; however, I didn't write the code, and I don't want to rewrite it
     */
    static int data;

    /**
     * handler
     * <p>
     * Handler2
     * <p>
     * instance of the handler class from the comporthandler2 package
     * this is our interface with the hardware controller
     */
    private Handler2 handler;

    /**
     * makeReceiverAID()
     *
     * @return an AID where a "ReceiverAgent" from the core package is located
     */
    private static AID makeReceiverAID() {
        AID aid = new AID("ReceiverAgent@10.20.102.203:1100/JADE", AID.ISGUID);
        aid.addAddresses("http://NC2611-PC-16.ucdenver.pvt:7778/acc");
        return aid;
    }

    //Neighborhood One
    // this is the node that we say we're sending data for
    private static MicrogridNode g = new MicrogridNode("microgrid-node-controller-generator", MicrogridNodeType.GENERATOR);

    /**
     * makeMicrogridGraph()
     *
     * @return Microgrid graph data representing the physical power grid we are measuring (currently it's a 1 node graph)
     */
    private static MicrogridGraph makeMicrogridGraph() {
        //Declare two hash sets to hold our nodes and edges
        Set<MicrogridNode> dummyNodes = new HashSet<MicrogridNode>();
        Set<MicrogridEdge> dummyEdges = new HashSet<MicrogridEdge>();

        dummyNodes.add(g);

        return new MicrogridGraph("microgrid-controller", dummyEdges, dummyNodes);
    }

    /**
     * makeGridUpdatePeriod()
     *
     * @return the time between graph data updates that we send in ms
     */
    private static long makeGridUpdatePeriod() {
        return 30000;
    }

    /**
     * makeHandler()
     * <p>
     * instantiates a handler, which interfaces between us and the hardware controller
     * I copied this line of code from Amine, and Bhanu edited it
     * <p>
     * I don't have documentation available for how/when to modify the numbers 8 and 3, which
     * respectively represent "packetSize" and "secs"
     * <p>
     * the string arg (comPort) should be the comport the controller is connected to
     *
     * @return the handler instance to use
     */
    private static Handler2 makeHandler() {
        return new Handler2("COM3", true, data, 8, 3);
    }

    /**
     * MicrogridControllerSenderAgent
     */
    public MicrogridControllerSenderAgent() {
        //Call super with the parameters from above
        super(makeReceiverAID(), makeMicrogridGraph(), makeGridUpdatePeriod());
        handler = makeHandler();
    }

    /**
     * setup()
     * <p>
     * method
     * <p>
     * called by JADE for agent startup
     * <p>
     * this is where we create and register our agent behaviors
     */
    protected void setup() {
        super.setup();

        //Using a JADE behavior as a timer, query the controller for new data every two seconds
        addBehaviour(new TickerBehaviour(this, 2000) {
            @Override
            protected void onTick() {
                if (handler.canSend()) {
                    handler.sendDataRequestPacket();
                }
            }
        });

        //Register with the handler to receive updates whenever we receive data from the controller via the commport
        // when we do, instantiate a Java object (FloatMicrogridDatum) and send the data to the receiver
        handler.registerControllerDataListener(new ControllerDataListener() {
            public void onControllerData(int data) {
                //Log the data to the console for easy debugging
                // todo remove this println statement
                System.out.println("data: " + data);

                //Don't ever use this method to get nodes in a real agent. Store the important nodes
                // as instance variables. This is acceptable because this is a dummy and I used DummyMicrogridGraph
                MicrogridNode node = getSubgraph().getNodes().iterator().next();


                //This is an old of how to properly send a measurement to the server assuming you have a node
                sendDatum(
                        //Create a FloatMicrogridDatum object to hold the measurement
                        new FloatMicrogridDatum(
                                //Timestamp the datum with the current number of milliseconds from Jan 1st, 1970
                                System.currentTimeMillis(),
                                //Tell the Datum which node measured it
                                node,
                                //Pretend we're measuring voltage
                                MicrogridFloatMeasurementType.VOLTAGE,
                                //Provide a dummy value for the measurement
                                (float) data));
            }
        });
    }
}
