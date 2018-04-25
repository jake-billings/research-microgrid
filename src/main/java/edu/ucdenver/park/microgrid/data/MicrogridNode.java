/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Node;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * MicrogridNode
 *
 * class
 *
 * immutable
 *
 * subclass of Node with microgrid-specific data
 *
 * @author Jake Billings
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
    private MicrogridNodeType microgridNodeType;

    /**
     * MicrogridGraph
     *
     * constructor: empty
     *
     * for use with deserialization; do not use for instantiation
     */
    public MicrogridNode() {}

    public MicrogridNode(String _id, MicrogridNodeType microgridNodeType) {
        super(_id);
        if (microgridNodeType == null) throw new IllegalArgumentException("microgridNodeType cannot be null when creating a MicrogridNode");
        this.microgridNodeType = microgridNodeType;
    }

    //----Getters----
    public MicrogridNodeType getMicrogridNodeType() {
        return microgridNodeType;
    }

    //----Setters----
    public void setMicrogridNodeType(MicrogridNodeType microgridNodeType) {
        this.microgridNodeType = microgridNodeType;
    }

    //----Externalizers----
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeByte(this.getMicrogridNodeType().get_id());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        this.setMicrogridNodeType(MicrogridNodeType.fromId(in.readByte()));
    }
}
