package edu.ucdenver.park.microgrid.data.abs;

/**
 * Edge
 *
 * class
 *
 * A graph contains a set of edges and nodes. This class represents a Edge.
 *  http://mathworld.wolfram.com/Graph.html
 *
 * My intention is for a subclass to add microgrid-specific data to this class.
 *
 * @author Jake Billings
 */
public class Edge<N extends Node> extends Entity {
    /**
     * to
     *
     * N
     *
     * the node this edge points to
     */
    private final N to;

    /**
     * from
     *
     * N
     *
     * the node this edge is from
     */
    private final N from;

    public Edge(String _id, N to, N from) {
        super(_id);
        this.to = to;
        this.from = from;
    }
}
