package edu.ucdenver.park.microgrid.data;

/**
 * FloatMicrogridDatum
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
 *  @author Jake Billings
 */
public class FloatMicrogridDatum extends MicrogridDatum {
    /**
     * value
     *
     * float
     *
     * the actual float reading represented by this datum object
     */
    private final float value;

    public FloatMicrogridDatum(String _id, long timestamp, MicrogridNode node, float value) {
        super(_id, timestamp, node);
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
