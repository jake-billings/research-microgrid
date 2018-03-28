package edu.ucdenver.park.microgrid.dummy;

import edu.ucdenver.park.microgrid.data.*;

import java.util.HashSet;
import java.util.Set;

/**
 * DummyMicrogrid
 * <p>
 * subclass of MicrogridGraph
 * <p>
 * Fills a microgrid graph with dummy data for testing purposes
 */
public class DummyMicrogrid extends MicrogridGraph {
    private static Set<MicrogridNode> dummyNodes = new HashSet<MicrogridNode>();
    private static Set<MicrogridEdge> dummyEdges = new HashSet<MicrogridEdge>();

    static {
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
    }
    public DummyMicrogrid() {
        super("microgrid-graph-dummy", dummyEdges, dummyNodes);
    }
}
