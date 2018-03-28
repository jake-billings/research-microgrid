package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Node;

/**
 * MicrogridNode
 *
 * class
 *
 * immutable
 *
 * subclass of Node with microgrid-specific data
 */
public class MicrogridNode extends Node {
    /**
     * microgridNodeType
     *
     * MicrogridNodeType
     *
     * Each node in the network has a type that does not affect its behavior in this code. For instance, a node can be a
     *  battery, controller, or generator. The purpose of this section of code is to render the network, so we don't
     *  treat these elements with different logic. Thus, we implement their type as an enum.
     */
    private final MicrogridNodeType microgridNodeType;


    public MicrogridNode(String _id, MicrogridNodeType microgridNodeType) {
        super(_id);
        this.microgridNodeType = microgridNodeType;
    }

    public MicrogridNodeType getMicrogridNodeType() {
        return microgridNodeType;
    }
}
