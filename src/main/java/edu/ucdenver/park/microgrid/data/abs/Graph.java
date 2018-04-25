/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data.abs;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
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
 *  However, throughout this application, we ignore the directed nature of this graph and treat it as undirected.
 *
 * A graph contains a set of edges and nodes. This class represents a node.
 *  http://mathworld.wolfram.com/Graph.html
 *
 * My intention is for a subclass to add microgrid-specific data to this class.
 *
 * @author Jake Billings
 */
public class Graph<E extends Edge, N extends Node> extends Entity {
    private Set<E> edges;
    private Set<N> nodes;

    /**
     * Graph
     *
     * constructor: empty
     *
     * for use with deserialization; do not use for instantiation
     */
    public Graph() {
        super();
    }

    public Graph(String _id) {
        super(_id);
        this.edges = new HashSet<E>();
        this.nodes = new HashSet<N>();
    }

    public Graph(String _id, Set<E> edges, Set<N> nodes) {
        super(_id);
        if (edges == null) throw new IllegalArgumentException("edges cannot be null when creating a graph");
        if (nodes == null) throw new IllegalArgumentException("nodes cannot be null when creating a graph");
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

    protected void setEdges(Set<E> edges) {
        this.edges = edges;
    }

    protected void setNodes(Set<N> nodes) {
        this.nodes = nodes;
    }

    /**
     * union()
     *
     * method
     *
     * graph operation
     *
     * returns a new Graph object that is the mathematical union of this graph with the graph other
     *  two new node and edge sets are created and populated with the contents of both graph's nodes and edges
     *
     * @param newId the new graph must have a unique _id, since everything is an Entity. This is the _id we give the new object
     * @param other the other graph object to union with this one
     * @returns a new Graph object that is the mathematical union of this graph with the graph other
     */
    public Graph<E, N> union(String newId, Graph<E, N> other) {
        Set<E> edges = new HashSet<E>();
        edges.addAll(this.getEdges());
        edges.addAll(other.getEdges());
        Set<N> nodes = new HashSet<N>();
        nodes.addAll(this.getNodes());
        nodes.addAll(other.getNodes());
        return new Graph<E, N>(newId, edges, nodes);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeInt(this.getNodes().size());
        for (N node : this.getNodes()) {
            node.writeExternal(out);
        }
        out.writeInt(this.getEdges().size());
        for (E edge : this.getEdges()) {
            edge.writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        int nodeCount = in.readInt();
        Set<N> nodes = new HashSet<N>();
        for (int i = 0; i<nodeCount; i++) {
            Node n = new Node();
            n.readExternal(in);
            nodes.add((N) n);
        }
        int edgeCount = in.readInt();
        Set<E> edges = new HashSet<E>();
        for (int i = 0; i<edgeCount; i++) {
            Edge<Node> e = new Edge<Node>();
            e.readExternal(in);
            edges.add((E) e);
        }
        this.setNodes(nodes);
        this.setEdges(edges);
    }
}