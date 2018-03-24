package edu.ucdenver.park.microgrid.data.abs;

import java.util.HashSet;
import java.util.Set;

/**
 * Graph
 *
 * class
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
    private Set<E> edges = new HashSet<E>();
    private Set<N> nodes = new HashSet<N>();

    public Graph(String _id) {
        super(_id);
    }

    /**
     * addNode()
     *
     * adds a node to this graph
     *
     * @param node the node to add to this graph
     */
    public void addNode(N node) {
        nodes.add(node);
    }

    /**
     * addEdge()
     *
     * adds to edge to this graph
     *
     * @param edge the edge to add to this graph
     */
    public void addEdge(E edge) {
        edges.add(edge);
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