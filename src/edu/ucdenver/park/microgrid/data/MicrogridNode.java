package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Node;

/**
 * MicrogridNode
 *
 * class
 *
 * subclass of Node with microgrid-specific data
 */
public class MicrogridNode extends Node {
    public MicrogridNode(String _id) {
        super(_id);
    }
}
