package edu.ucdenver.park.microgrid.data.abs;

import java.util.HashSet;
import java.util.Set;

/**
 * Graph
 *
 * class
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
}