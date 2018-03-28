package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Datum;

/**
 * Datum
 *
 * abstract class
 *
 * immutable
 *
 * This class is intended to represent one reading at one point in time from one instrument. Depending on the final
 *  protocol implementation, packets may contain multiple instances of data. Packets might only contain one datum.
 *
 * This class should fit into one INSERT call into a SQL database.
 *
 * The datum with the most recent timestamp should represent the most up-to-date reading of an instrument at a given
 *  time.
 *
 * This class is abstract because datum can have either float or boolean values. See the subclasses BooleanDatum and
 *  FloatDatum.
 *
 *  @author Jake Billings
 */
public class MicrogridDatum extends Datum {
    /**
     * node
     *
     * MicrogridNode
     *
     * the node at which this datum was recorded
     */
    private final MicrogridNode node;

    public MicrogridDatum(String _id, long timestamp, MicrogridNode node) {
        super(_id, timestamp);
        if (node == null) throw new IllegalArgumentException("node cannot be null when creating a MicrogridDatum");
        this.node = node;
    }

    public MicrogridNode getNode() {
        return node;
    }
}
