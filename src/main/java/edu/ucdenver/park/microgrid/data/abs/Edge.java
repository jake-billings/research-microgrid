package edu.ucdenver.park.microgrid.data.abs;

/**
 * Edge
 *
 * class
 *
 * immutable
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
        if (to == null) throw new IllegalArgumentException("to cannot be null when creating an edge");
        if (from == null) throw new IllegalArgumentException("to cannot be null when creating an edge");
        this.to = to;
        this.from = from;
    }

    public N getTo() {
        return to;
    }

    public N getFrom() {
        return from;
    }
}
