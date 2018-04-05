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
 * DummyControllerAMicrogridSenderAgent
 * <p>
 * class: JADE agent
 * <p>
 * this is a MicrogridSenderAgent that uses dummy microgrid data
 * <p>
 * See ExampleMicrogridSenderAgent for more documentation
 *
 * @author Jake Billings
 */
public class DummyControllerAMicrogridSenderAgent extends MicrogridSenderAgent {
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
        MicrogridNode g = new MicrogridNode("microgrid-node-a-g", MicrogridNodeType.GENERATOR);
        MicrogridNode b = new MicrogridNode("microgrid-node-a-b", MicrogridNodeType.BATTERY);
        MicrogridNode l = new MicrogridNode("microgrid-node-a-l", MicrogridNodeType.LOAD);
        MicrogridNode gBreaker = new MicrogridNode("microgrid-node-a-g-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
        MicrogridNode bBreaker = new MicrogridNode("microgrid-node-a-b-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
        MicrogridNode lBreaker = new MicrogridNode("microgrid-node-a-l-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
        MicrogridNode ourHub = new MicrogridNode("micrgrid-node-a-hub", MicrogridNodeType.HUB);
        MicrogridNode ourBreaker = new MicrogridNode("microgrid-node-a-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
        MicrogridNode centralHub = new MicrogridNode("microgrid-node-central-a-hub", MicrogridNodeType.HUB);

        //Add nodes to set
        nodes.add(g);
        nodes.add(b);
        nodes.add(l);
        nodes.add(gBreaker);
        nodes.add(bBreaker);
        nodes.add(lBreaker);
        nodes.add(ourHub);
        nodes.add(ourBreaker);
        nodes.add(centralHub);

        //Edges
        edges.add(new MicrogridEdge("microgrid-edge-a-gb", g, gBreaker, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-a-bb", b, bBreaker, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-a-lb", l, lBreaker, MicrogridEdgeType.BUS));

        edges.add(new MicrogridEdge("microgrid-edge-a-gbh", gBreaker, ourHub, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-a-bbh", bBreaker, ourHub, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-a-lbh", lBreaker, ourHub, MicrogridEdgeType.BUS));

        edges.add(new MicrogridEdge("microgrid-edge-a-hb", ourHub, ourBreaker, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-a-bch", ourBreaker, centralHub, MicrogridEdgeType.BUS));

        return new MicrogridGraph("microgrid-graph-subgraph-a", edges, nodes);
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
     * ExampleMicrogridSenderAgent()
     * <p>
     * constructor
     */
    public DummyControllerAMicrogridSenderAgent() {
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
