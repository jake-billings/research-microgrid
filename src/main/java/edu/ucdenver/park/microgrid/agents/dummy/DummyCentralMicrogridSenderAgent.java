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

        //Add nodes to set
        nodes.add(centralHubA);
        nodes.add(centralHubB);
        nodes.add(centralHubC);

        //Edges
        edges.add(new MicrogridEdge("microgrid-edge-central-ab", centralHubA, centralHubB, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-bc", centralHubB, centralHubC, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-ca", centralHubC, centralHubA, MicrogridEdgeType.BUS));

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
    }
}
