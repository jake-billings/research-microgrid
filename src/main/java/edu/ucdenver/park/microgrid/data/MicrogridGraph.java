/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Graph;

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
    public MicrogridGraph(String _id) {
        super(_id, new HashSet<MicrogridEdge>(), new HashSet<MicrogridNode>());
    }

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
}
