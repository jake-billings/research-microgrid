package edu.ucdenver.park.microgrid.data.abs;

import edu.ucdenver.park.microgrid.data.abs.Entity;

/**
 * Datum
 *
 * abstract class
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
 * This class also makes no claim to part of a microgrid system. See the subclass MicrogridDatum
 *
 *  @author Jake Billings
 */
public abstract class Datum extends Entity {
    /**
     * timestamp
     *
     * long
     *
     * the number of milliseconds from Jan 1, 1970 when this datum was recorded
     */
    private final long timestamp;

    public Datum(String _id, long timestamp) {
        super(_id);
        this.timestamp = timestamp;
    }
}