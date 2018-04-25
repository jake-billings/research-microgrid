/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Edge;
import edu.ucdenver.park.microgrid.data.abs.Graph;
import edu.ucdenver.park.microgrid.data.abs.Node;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashSet;
import java.util.Set;

/**
 * MicrogridGraph
 * <p>
 * class
 * <p>
 * By design choice, this class shall represent a directed microgrid graph.
 *  However, throughout this application, we ignore the directed nature of this graph and treat it as undirected.
 *  The UI (in render.js) modifies the directionality of graphs based on nodeSnapshots (current measurements)
 * <p>
 * immutable
 * <p>
 * subclass of Edge with microgrid-specific data
 *
 * @author Jake Billings
 */
public class MicrogridGraph extends Graph<MicrogridEdge, MicrogridNode> {
    /**
     * MicrogridGraph
     *
     * constructor: empty
     *
     * for use with deserialization; do not use for instantiation
     */
    public MicrogridGraph() {}

    /**
     * MicrogridGraph()
     *
     * instantiates a graph with HashSets as the Set implementation
     *
     * @param _id the _id to use
     */
    public MicrogridGraph(String _id) {
        super(_id, new HashSet<MicrogridEdge>(), new HashSet<MicrogridNode>());
    }

    /**
     * MicrogridGraph()
     *
     * constructor
     *
     * properly instantiates a MicrogridGraph
     *
     * @param _id unique string _id to use
     * @param edges edge set implementation
     * @param nodes node set implementation
     */
    public MicrogridGraph(String _id, Set<MicrogridEdge> edges, Set<MicrogridNode> nodes) {
        super(_id, edges, nodes);
    }

    /**
     * union()
     * <p>
     * overrides the union function with a typecast for all specific MicrogridGraph objects
     *
     * @param newId the new graph must have a unique _id, since everything is an Entity. This is the _id we give the new object
     * @param other the other graph object to union with this one
     * @returns a new Graph object that is the mathematical union of this graph with the graph other
     */
    public MicrogridGraph union(String newId, MicrogridGraph other) {
        Graph rawUnion = super.union(newId, other);
        return new MicrogridGraph(newId, (Set<MicrogridEdge>) rawUnion.getEdges(), (Set<MicrogridNode>) rawUnion.getNodes());
    }

    //----Externalizers----
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.get_id());
        out.writeInt(this.getNodes().size());
        for (MicrogridNode node : this.getNodes()) {
            node.writeExternal(out);
        }
        out.writeInt(this.getEdges().size());
        for (MicrogridEdge edge : this.getEdges()) {
            edge.writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.set_id(in.readUTF());
        int nodeCount = in.readInt();
        Set<MicrogridNode> nodes = new HashSet<MicrogridNode>();
        for (int i = 0; i<nodeCount; i++) {
            MicrogridNode n = new MicrogridNode();
            n.readExternal(in);
            nodes.add(n);
        }
        int edgeCount = in.readInt();
        Set<MicrogridEdge> edges = new HashSet<MicrogridEdge>();
        for (int i = 0; i<edgeCount; i++) {
            MicrogridEdge e = new MicrogridEdge();
            e.readExternal(in);
            edges.add(e);
        }
        this.setNodes(nodes);
        this.setEdges(edges);
    }
}
