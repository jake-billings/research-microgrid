package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Graph;

/**
 * MicrogridGraph
 *
 * class
 *
 * mutable
 *
 * subclass of Edge with microgrid-specific data
 */
public class MicrogridGraph extends Graph<MicrogridEdge, MicrogridNode> {
    public MicrogridGraph(String _id) {
        super(_id);
    }
}
