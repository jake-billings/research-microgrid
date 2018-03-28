package edu.ucdenver.park.microgrid.data.abs;

import java.util.HashSet;
import java.util.Set;

/**
 * Graph
 *
 * class
 *
 * immutable
 *
 * By design choice, this class shall represent a directed graph.
 *
 * A graph contains a set of edges and nodes. This class represents a node.
 *  http://mathworld.wolfram.com/Graph.html
 *
 * My intention is for a subclass to add microgrid-specific data to this class.
 *
 * @author Jake Billings
 */
public class Graph<E extends Edge, N extends Node> extends Entity {
    private final Set<E> edges;
    private final Set<N> nodes;

    public Graph(String _id, Set<E> edges, Set<N> nodes) {
        super(_id);
        this.edges = edges;
        this.nodes = nodes;
    }

    /**
     * getEdges()
     *
     * @return a Set representing the edges of this graph
     */
    public Set<E> getEdges() {
        return edges;
    }

    /**
     * getNodes()
     *
     * @return a Set representing the nodes of this graph
     */
    public Set<N> getNodes() {
        return nodes;
    }
}