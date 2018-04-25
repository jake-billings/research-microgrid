/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Edge;
import edu.ucdenver.park.microgrid.data.abs.Node;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * MicrogridEdge
 * <p>
 * class
 * <p>
 * immutable
 * <p>
 * subclass of Edge with microgrid-specific data
 * <p>
 * By design choice, this class shall represent a directed microgrid graph edge.
 *  However, throughout this application, we ignore the directed nature of this graph and treat it as undirected.
 *  The UI (in render.js) modifies the directionality of graphs based on nodeSnapshots (current measurements)
 *
 * @author Jake Billings
 */
public class MicrogridEdge extends Edge<MicrogridNode> {
    /**
     * microgridEdgeType
     * <p>
     * MicrogridEdgeType
     * <p>
     * "connection type"
     * <p>
     * Each microgrid edge will have a type such as "power connection" or "data connection"
     * Since these edges don't behave differently in code; only in rendering, they are represented using an
     * enum type field on the MircrogridEdge object (as opposed to subclasses)
     */
    private MicrogridEdgeType microgridEdgeType;

    /**
     * MicrogridEdge
     *
     * constructor: empty
     *
     * for use with deserialization; do not use for instantiation
     */
    public MicrogridEdge() {}

    public MicrogridEdge(String _id, MicrogridNode to, MicrogridNode from, MicrogridEdgeType microgridEdgeType) {
        super(_id, to, from);
        if (microgridEdgeType == null)
            throw new IllegalArgumentException("microgridEdgeType cannot be null when creating a MicrogridEdge");
        this.microgridEdgeType = microgridEdgeType;
    }

    //----Getters----
    public MicrogridEdgeType getMicrogridEdgeType() {
        return microgridEdgeType;
    }

    //----Setters----
    private void setMicrogridEdgeType(MicrogridEdgeType microgridEdgeType) {
        this.microgridEdgeType = microgridEdgeType;
    }

    //----Externalizers----
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.get_id());
        out.write(this.getMicrogridEdgeType().get_id());
        this.getTo().writeExternal(out);
        this.getFrom().writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.set_id(in.readUTF());
        this.setMicrogridEdgeType(MicrogridEdgeType.fromId(in.readByte()));
        MicrogridNode to = new MicrogridNode();
        to.readExternal(in);
        this.setTo(to);
        MicrogridNode from = new MicrogridNode();
        from.readExternal(in);
        this.setFrom(from);
    }
}

