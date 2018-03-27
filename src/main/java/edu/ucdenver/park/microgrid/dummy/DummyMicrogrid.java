package edu.ucdenver.park.microgrid.dummy;

import edu.ucdenver.park.microgrid.data.*;

/**
 * DummyMicrogrid
 * <p>
 * subclass of MicrogridGraph
 * <p>
 * Fills a microgrid graph with dummy data for testing purposes
 */
public class DummyMicrogrid extends MicrogridGraph {
    public DummyMicrogrid() {
        super("microgrid-graph-dummy");

        //Neighborhood One
        MicrogridNode n1Generator = new MicrogridNode("microgrid-node-n1-generator", MicrogridNodeType.GENERATOR);
        this.addNode(n1Generator);
        MicrogridNode n1Battery = new MicrogridNode("microgrid-node-n1-battery", MicrogridNodeType.BATTERY);
        this.addNode(n1Battery);
        this.addEdge(new MicrogridEdge("microgrid-edge-n1-gb", n1Generator, n1Battery, MicrogridEdgeType.BUS));

        //Neighborhood Two
        MicrogridNode n2Generator = new MicrogridNode("microgrid-node-n2-generator", MicrogridNodeType.GENERATOR);
        this.addNode(n2Generator);
        MicrogridNode n2Battery = new MicrogridNode("microgrid-node-n2-battery", MicrogridNodeType.BATTERY);
        this.addNode(n2Battery);
        this.addEdge(new MicrogridEdge("microgrid-edge-n2-gb", n2Generator, n2Battery, MicrogridEdgeType.BUS));

        //Connect the neighborhood
        this.addEdge(new MicrogridEdge("microgrid-edge-inter-n1-n2", n1Battery, n2Battery, MicrogridEdgeType.BUS));
    }
}
