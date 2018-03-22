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
public class Edge extends Entity {

    public Edge(String _id) {
        super(_id);
    }
}
