package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Graph;

import java.util.Set;

/**
 * MicrogridGraph
 *
 * class
 *
 * immutable
 *
 * subclass of Edge with microgrid-specific data
 */
public class MicrogridGraph extends Graph<MicrogridEdge, MicrogridNode> {
    public MicrogridGraph(String _id, Set<MicrogridEdge> edges, Set<MicrogridNode> nodes) {
        super(_id, edges, nodes);
    }
}
