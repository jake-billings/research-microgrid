package edu.ucdenver.park.microgrid.live;

import edu.ucdenver.park.microgrid.data.MicrogridDatum;
import edu.ucdenver.park.microgrid.data.MicrogridNode;
import edu.ucdenver.park.microgrid.data.MicrogridNodeType;

import java.util.Set;

/**
 * MicrogridNodeSnapshot
 *
 * class
 *
 * immutable
 *
 * Represents a snapshot in time at a given microgrid node
 *  it has all the most recent measurements from the LiveMicrogridGraph's HashMap
 */
public class MicrogridNodeSnapshot extends MicrogridNode {
    /**
     * measurements
     *
     * Set<MicrogridDatum>
     *
     * most up to date measurements for this node
     */
    private final Set<MicrogridDatum> measurements;

    public MicrogridNodeSnapshot(String _id, MicrogridNodeType microgridNodeType, Set<MicrogridDatum> measurements) {
        super(_id, microgridNodeType);
        this.measurements = measurements;
    }

    public MicrogridNodeSnapshot(MicrogridNode node, Set<MicrogridDatum> measurements) {
        this(node.get_id(), node.getMicrogridNodeType(), measurements);
    }

    public Set<MicrogridDatum> getMeasurements() {
        return measurements;
    }
}
