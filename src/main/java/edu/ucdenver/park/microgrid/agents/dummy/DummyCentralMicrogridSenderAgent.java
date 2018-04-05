/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.agents.dummy;

import edu.ucdenver.park.microgrid.agents.core.MicrogridSenderAgent;
import edu.ucdenver.park.microgrid.data.*;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import java.util.HashSet;
import java.util.Set;

/**
 * DummyCentralMicrogridSenderAgent
 * <p>
 * class: JADE agent
 * <p>
 * this is a MicrogridSenderAgent that uses dummy microgrid data
 * <p>
 * See ExampleMicrogridSenderAgent for more documentation
 *
 * @author Jake Billings
 */
public class DummyCentralMicrogridSenderAgent extends MicrogridSenderAgent {
    /**
     * makeReceiverAID()
     *
     * @return an AID pointing to the agent that will receive the data we send
     */
    private static AID makeReceiverAID() {
        return new AID("ReceiverAgent", AID.ISLOCALNAME);
    }

    /**
     * makeMicrogridGraph()
     *
     * @return a microgrid graph representing the subgraph we know about
     */
    private static MicrogridGraph makeMicrogridGraph() {
        //Declare two hash sets to hold our nodes and edges
        Set<MicrogridNode> nodes = new HashSet<MicrogridNode>();
        Set<MicrogridEdge> edges = new HashSet<MicrogridEdge>();

        //Nodes
        MicrogridNode centralHubA = new MicrogridNode("microgrid-node-central-a-hub", MicrogridNodeType.HUB);
        MicrogridNode centralHubB = new MicrogridNode("microgrid-node-central-b-hub", MicrogridNodeType.HUB);
        MicrogridNode centralHubC = new MicrogridNode("microgrid-node-central-c-hub", MicrogridNodeType.HUB);
        MicrogridNode centralHubD = new MicrogridNode("microgrid-node-central-d-hub", MicrogridNodeType.HUB);

        //Add nodes to set
        nodes.add(centralHubA);
        nodes.add(centralHubB);
        nodes.add(centralHubC);
        nodes.add(centralHubD);

        //Edges
        edges.add(new MicrogridEdge("microgrid-edge-central-ab", centralHubA, centralHubB, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-bc", centralHubB, centralHubC, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-cd", centralHubC, centralHubD, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-da", centralHubD, centralHubA, MicrogridEdgeType.BUS));

        return new MicrogridGraph("microgrid-graph-subgraph-central", edges, nodes);
    }

    /**
     * makeGridUpdatePeriod()
     *
     * @return the number of milliseconds between grid updates
     */
    private static long makeGridUpdatePeriod() {
        return 5000;
    }

    /**
     * DummyCentralMicrogridSenderAgent()
     * <p>
     * constructor
     */
    public DummyCentralMicrogridSenderAgent() {
        //Call super with the parameters from above
        super(makeReceiverAID(), makeMicrogridGraph(), makeGridUpdatePeriod());
    }

    /**
     * setup()
     * <p>
     * override setup function so that we can add behaviors
     */
    protected void setup() {
        super.setup();

        addBehaviour(new TickerBehaviour(this, 2000) {
            @Override
            protected void onTick() {
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
