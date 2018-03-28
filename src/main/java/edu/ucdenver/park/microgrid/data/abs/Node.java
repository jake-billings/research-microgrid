package edu.ucdenver.park.microgrid.data.abs;

/**
 * Node
 *
 * class
 *
 * immutable
 *
 * A graph contains a set of edges and nodes. This class represents a node.
 *  http://mathworld.wolfram.com/Graph.html
 *
 * My intention is for a subclass to add microgrid-specific data to this class.
 *
 * @author Jake Billings
 */
public class Node extends Entity {

    public Node(String _id) {
        super(_id);
    }
}
