/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.agents.example;

import edu.ucdenver.park.microgrid.agents.core.MicrogridSenderAgent;
import edu.ucdenver.park.microgrid.data.*;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import java.util.HashSet;
import java.util.Set;

/**
 * ExampleMicrogridSenderAgent
 * <p>
 * class: JADE agent
 * <p>
 * this is a MicrogridSenderAgent that uses dummy microgrid data
 * <p>
 * this is a good reference for writing agents that extend MicrogridSenderAgent
 * to send data to MicrogridReceiverAgent
 * <p>
 * this agent is used for testing/developing the frontend UI
 * if you're looking for a file to copy/paste this is it
 * <p>
 * see Dummy MicrogridSenderAgent if you need a functional dummy agent to test frontend or something
 * <p>
 * How to use:
 * 1. Copy this file
 * 2. Rename the class and file based on what the agent will be doing. (For instance, "ControllerAMicrogridSenderAgent")
 * 3. Change makeReceiverAID() to point at the actual receiver server you're using
 * 4. Change makeMicrogridGraph() to represent your actual electrical grid graph data
 * 5. Change makeGridUpdatePeriod() to represent a reasonable update period for your graph data (don't change it in development unless you know what you're doing)
 * 6. Change the TickerBehaviour in setup() to read the actual data you're reading and send it
 *
 * @author Jake Billings
 */
public class ExampleMicrogridSenderAgent extends MicrogridSenderAgent {
    /**
     * makeReceiverAID()
     * <p>
     * this is what points to the MicrogridReceiverAgent
     * use the local name of the agent as the first string parameter
     * AID aid = new AID("jake", AID.ISLOCALNAME);
     * <p>
     * Recommended action: Make sure this is up to date with the receiving agent you're using
     *
     * @return an AID pointing to the agent that will receive the data we send
     */
    private static AID makeReceiverAID() {
        AID aid = new AID("ReceiverAgent@10.20.102.203:1100/JADE", AID.ISGUID);
        aid.addAddresses("http://NC2611-PC-16.ucdenver.pvt:7778/acc");
        return aid;
    }

    /**
     * makeMicrogridGraph()
     * <p>
     * returns our subgraph
     * <p>
     * this is the local grid that this agent knows about
     * <p>
     * you will have to write code to initialize this grid in this constructor
     * when you copy it. I use the class DummyMicrogrid because this is a
     * Dummy agent
     * this is what describes the connections between generators, batteries
     * it tells the map what to draw
     * data we send will belong to nodes that are in this object
     * MicrogridGraph subgraph = new DummyMicrogrid();
     * <p>
     * Recommended action: Rewrite the Node and Edge creation calls to match your actual network
     *
     * @return a microgrid graph representing the subgraph we know about
     */
    private static MicrogridGraph makeMicrogridGraph() {
        //Declare two hash sets to hold our nodes and edges
        Set<MicrogridNode> dummyNodes = new HashSet<MicrogridNode>();
        Set<MicrogridEdge> dummyEdges = new HashSet<MicrogridEdge>();

        //Neighborhood One
        MicrogridNode n1Generator = new MicrogridNode("microgrid-node-n1-generator", MicrogridNodeType.GENERATOR);
        dummyNodes.add(n1Generator);
        MicrogridNode n1Battery = new MicrogridNode("microgrid-node-n1-battery", MicrogridNodeType.BATTERY);
        dummyNodes.add(n1Battery);
        dummyEdges.add(new MicrogridEdge("microgrid-edge-n1-gb", n1Generator, n1Battery, MicrogridEdgeType.BUS));

        //Neighborhood Two
        MicrogridNode n2Generator = new MicrogridNode("microgrid-node-n2-generator", MicrogridNodeType.GENERATOR);
        dummyNodes.add(n2Generator);
        MicrogridNode n2Battery = new MicrogridNode("microgrid-node-n2-battery", MicrogridNodeType.BATTERY);
        dummyNodes.add(n2Battery);
        dummyEdges.add(new MicrogridEdge("microgrid-edge-n2-gb", n2Generator, n2Battery, MicrogridEdgeType.BUS));

        //Connect the neighborhood
        dummyEdges.add(new MicrogridEdge("microgrid-edge-inter-n1-n2", n1Battery, n2Battery, MicrogridEdgeType.BUS));

        return new MicrogridGraph("example-microgrid-sender-subgraph", dummyEdges, dummyNodes);
    }

    /**
     * makeGridUpdatePeriod()
     * <p>
     * this is how often we update the map with what nodes we have
     * <p>
     * every gridUpdatePeriod milliseconds, we send another copy of subgraph to the
     * receiver agent. The data expires every gridUpdatePeriod + 1000 ms
     * as a result, when we stop this sender agent, it will take gridUpdatePeriod + 1000ms
     * to disappear from the map
     * <p>
     * I recommend setting this value to about five seconds while testing so that your
     * changes show up properly.
     * In a production environment, this could easily be set to hours since the graph
     * doesn't really change
     * long gridUpdatePeriod = 30000;
     * <p>
     * Recommended action: Don't change this unless this application moves from development in the lab to production in
     * the real world
     * <p>
     * If this number is too small, you may overwhelm the receiving server and cause graph expirations due to
     * processing delays
     *
     * @return the number of milliseconds between grid updates
     */
    private static long makeGridUpdatePeriod() {
        return 30000;
    }

    /**
     * ExampleMicrogridSenderAgent()
     * <p>
     * constructor
     * <p>
     * I don't think JADE likes for you to have params in your constructor. So, call super with everything
     * setup for your agent.
     * <p>
     * You need to call super with an agent id (AID) that points to the MicrogridReceiverAgent and a MicrogridGraph
     * that represents your graph structure
     * <p>
     * you do not need to edit this constructor
     * <p>
     * Recommended action: none
     */
    public ExampleMicrogridSenderAgent() {
        //Call super with the parameters from above
        super(makeReceiverAID(), makeMicrogridGraph(), makeGridUpdatePeriod());
    }

    /**
     * setup()
     * <p>
     * override setup function so that we can add behaviors
     * the super.setup() adds one behavior that handles updating the microgrid graph data
     * make sure this line is present at the start of the function
     * <p>
     * then, we can add additional behaviors
     * <p>
     * since this is a dummy old, I add a behavior that sends dummy data for one node every 2 seconds
     * <p>
     * in an actual
     * <p>
     * <p>
     * Recommended action: add data logging for every node you intend to measure
     */
    protected void setup() {
        super.setup();

        addBehaviour(new TickerBehaviour(this, 2000) {
            @Override
            protected void onTick() {
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
                                MicrogridMeasurementType.VOLTAGE,
                                //Provide a dummy value for the measurement
                                15.0F));
            }
        });
    }
}
