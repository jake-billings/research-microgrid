package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Graph;

import java.util.Set;

/**
 * MicrogridGraph
 * <p>
 * class
 * <p>
 * immutable
 * <p>
 * subclass of Edge with microgrid-specific data
 */
public class MicrogridGraph extends Graph<MicrogridEdge, MicrogridNode> {
    public MicrogridGraph(String _id, Set<MicrogridEdge> edges, Set<MicrogridNode> nodes) {
        super(_id, edges, nodes);
    }

    /**
     * union()
     *
     * overrides the union function with a typecast for all specific MicrogridGraph objects
     *
     * @param newId the new graph must have a unique _id, since everything is an Entity. This is the _id we give the new object
     * @param other the other graph object to union with this one
     * @returns a new Graph object that is the mathematical union of this graph with the graph other
     */
    public MicrogridGraph union(String newId, MicrogridGraph other) {
        return (MicrogridGraph) super.union(newId, other);
    }
}
