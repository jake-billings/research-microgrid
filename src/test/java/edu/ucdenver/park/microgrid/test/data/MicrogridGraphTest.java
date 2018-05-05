package edu.ucdenver.park.microgrid.test.data;

import edu.ucdenver.park.microgrid.data.*;
import edu.ucdenver.park.microgrid.data.abs.Edge;
import edu.ucdenver.park.microgrid.data.abs.Graph;
import edu.ucdenver.park.microgrid.data.abs.Node;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MicrogridGraphTest {

    //Nodes
    private static MicrogridNode g = new MicrogridNode("microgrid-node-a-g", MicrogridNodeType.GENERATOR);
    private static MicrogridNode b = new MicrogridNode("microgrid-node-a-b", MicrogridNodeType.BATTERY);
    private static MicrogridNode l = new MicrogridNode("microgrid-node-a-l", MicrogridNodeType.LOAD);
    private static MicrogridNode gBreaker = new MicrogridNode("microgrid-node-a-g-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
    private static MicrogridNode bBreaker = new MicrogridNode("microgrid-node-a-b-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
    private static MicrogridNode lBreaker = new MicrogridNode("microgrid-node-a-l-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
    private static MicrogridNode ourHub = new MicrogridNode("micrgrid-node-a-hub", MicrogridNodeType.HUB);
    private static MicrogridNode ourBreaker = new MicrogridNode("microgrid-node-a-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
    private static MicrogridNode centralHub = new MicrogridNode("microgrid-node-central-a-hub", MicrogridNodeType.HUB);

    private static MicrogridEdge e = new MicrogridEdge("microgrid-edge-a-gb", g, gBreaker, MicrogridEdgeType.BUS);

    /**
     *
     * makeMicrogridGraph()
     *
     * @return a microgrid graph representing the subgraph we know about
     */
    private static MicrogridGraph makeMicrogridGraph() {
        //Declare two hash sets to hold our nodes and edges
        Set<MicrogridNode> nodes = new HashSet<MicrogridNode>();
        Set<MicrogridEdge> edges = new HashSet<MicrogridEdge>();

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
        edges.add(e);
        edges.add(new MicrogridEdge("microgrid-edge-a-bb", b, bBreaker, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-a-lb", l, lBreaker, MicrogridEdgeType.BUS));

        edges.add(new MicrogridEdge("microgrid-edge-a-gbh", gBreaker, ourHub, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-a-bbh", bBreaker, ourHub, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-a-lbh", lBreaker, ourHub, MicrogridEdgeType.BUS));

        edges.add(new MicrogridEdge("microgrid-edge-a-hb", ourHub, ourBreaker, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-a-bch", ourBreaker, centralHub, MicrogridEdgeType.BUS));

        return new MicrogridGraph("microgrid-graph-subgraph-a", edges, nodes);
    }

    @Test
    void shouldSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        MicrogridGraph graph = makeMicrogridGraph();

        assert graph.get_id().equals("microgrid-graph-subgraph-a");

        assert graph.getNodes().size() == 9;
        assert graph.getEdges().size() == 8;

        assert graph.getNodes().contains(g);
        assert graph.getNodes().contains(b);
        assert graph.getNodes().contains(l);
        assert graph.getNodes().contains(gBreaker);
        assert graph.getEdges().contains(e);
        assert !graph.getEdges().contains(g);
        assert !graph.getNodes().contains(e);

        //Serialize the object to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        graph.writeExternal(out);
        out.close();

        //Dump to a byte array and take note of the size
        byte[] rawBytes = baos.toByteArray();
        System.out.println("Size of microgrid graph is: " + rawBytes.length + " bytes");
        System.out.write(rawBytes);
        System.out.println();

        //Deserialize from the byte array
        ByteArrayInputStream bais = new ByteArrayInputStream(rawBytes);
        ObjectInputStream in = new ObjectInputStream(bais);
        MicrogridGraph graph2 = new MicrogridGraph();
        graph2.readExternal(in);

        assert graph2.get_id().equals("microgrid-graph-subgraph-a");

        assert graph2.getNodes().size() == 9;
        assert graph2.getEdges().size() == 8;

        assert graph2.getNodes().contains(g);
        assert graph2.getNodes().contains(b);
        assert graph2.getNodes().contains(l);
        assert graph2.getNodes().contains(gBreaker);
        assert graph2.getEdges().contains(e);
        assert !graph2.getEdges().contains(g);
        assert !graph2.getNodes().contains(e);
    }

}