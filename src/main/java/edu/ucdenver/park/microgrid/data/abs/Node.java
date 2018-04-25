/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data.abs;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Node
 * <p>
 * class
 * <p>
 * immutable
 * <p>
 * A graph contains a set of edges and nodes. This class represents a node.
 * http://mathworld.wolfram.com/Graph.html
 * <p>
 * My intention is for a subclass to add microgrid-specific data to this class.
 *
 * @author Jake Billings
 */
public class Node extends Entity {
    /**
     * Node
     *
     * constructor: empty
     *
     * for use with deserialization; do not use for instantiation
     */
    public Node() {
        super();
    }

    public Node(String _id) {
        super(_id);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
    }
}
