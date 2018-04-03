/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Edge;

/**
 * MicrogridEdge
 *
 * class
 *
 * immutable
 *
 * subclass of Edge with microgrid-specific data
 *
 * @author Jake Billings
 */
public class MicrogridEdge extends Edge<MicrogridNode> {
    /**
     * microgridEdgeType
     *
     * MicrogridEdgeType
     *
     * "connection type"
     *
     * Each microgrid edge will have a type such as "power connection" or "data connection"
     * Since these edges don't behave differently in code; only in rendering, they are represented using an
     * enum type field on the MircrogridEdge object (as opposed to subclasses)
     */
    private final MicrogridEdgeType microgridEdgeType;

    public MicrogridEdge(String _id, MicrogridNode to, MicrogridNode from, MicrogridEdgeType microgridEdgeType) {
        super(_id, to, from);
        if (microgridEdgeType == null) throw new IllegalArgumentException("microgridEdgeType cannot be null when creating a MicrogridEdge");
        this.microgridEdgeType = microgridEdgeType;
    }

    public MicrogridEdgeType getMicrogridEdgeType() {
        return microgridEdgeType;
    }
}

