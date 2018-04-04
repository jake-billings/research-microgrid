/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.agents.core.example;

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
     *
     * @return a microgrid graph representing the subgraph we know about
     */
    private static MicrogridGraph makeMicrogridGraph() {
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
     * long gridUpdatePeriod = 5000;
     *
     * @return the number of milliseconds between grid updates
     */
    private static long makeGridUpdatePeriod() {
        return 5000;
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
                                //Generate a unique id relying on time and the uniqueness of the node id and measurement type
                                "datum-" + node.get_id() + "-voltage-" + System.currentTimeMillis(),
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
