/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data.abs;

/**
 * Edge
 * <p>
 * class
 * <p>
 * immutable
 * <p>
 * A graph contains a set of edges and nodes. This class represents a Edge.
 * http://mathworld.wolfram.com/Graph.html
 * <p>
 * My intention is for a subclass to add microgrid-specific data to this class.
 * <p>
 * By design choice, this class shall represent a directed microgrid graph edge.
 * However, throughout this application, we ignore the directed nature of this graph and treat it as undirected.
 *
 * @author Jake Billings
 */
public class Edge<N extends Node> extends Entity {
    /**
     * to
     * <p>
     * N
     * <p>
     * the node this edge points to
     */
    private final N to;

    /**
     * from
     * <p>
     * N
     * <p>
     * <p>
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
