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

        //Breakers
        MicrogridNode breakerAL = new MicrogridNode("microgrid-node-central-a-b-l", MicrogridNodeType.CIRCUIT_BREAKER);
        MicrogridNode breakerAR = new MicrogridNode("microgrid-node-central-a-b-r", MicrogridNodeType.CIRCUIT_BREAKER);
        MicrogridNode breakerBL = new MicrogridNode("microgrid-node-central-b-b-l", MicrogridNodeType.CIRCUIT_BREAKER);
        MicrogridNode breakerBR = new MicrogridNode("microgrid-node-central-b-b-r", MicrogridNodeType.CIRCUIT_BREAKER);
        MicrogridNode breakerCL = new MicrogridNode("microgrid-node-central-c-b-l", MicrogridNodeType.CIRCUIT_BREAKER);
        MicrogridNode breakerCR = new MicrogridNode("microgrid-node-central-c-b-r", MicrogridNodeType.CIRCUIT_BREAKER);

        //Add nodes to set
        nodes.add(centralHubA);
        nodes.add(centralHubB);
        nodes.add(centralHubC);
        nodes.add(breakerAL);
        nodes.add(breakerAR);
        nodes.add(breakerBL);
        nodes.add(breakerBR);
        nodes.add(breakerCL);
        nodes.add(breakerCR);

        //Edges
        edges.add(new MicrogridEdge("microgrid-edge-central-abl", centralHubA, breakerAL, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-abr", centralHubA, breakerAR, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-bbl", centralHubB, breakerBL, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-bbr", centralHubB, breakerBR, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-cbl", centralHubC, breakerCL, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-cbr", centralHubC, breakerCR, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-abrbbl", breakerAR, breakerBL, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-bbrcbl", breakerBR, breakerCL, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-central-cblabl", breakerCR, breakerAL, MicrogridEdgeType.BUS));

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
