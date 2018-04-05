/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
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
 * Important: the _id field of a Datum should be unique to the combination of its type and measurement location
 *  as a result, data from the same measurement location and type will have the same _id but different
 *  timestamp and value; this is important for data caching in LiveMicrogrid
 *
 * A unique identifier for an individual datum object should contain its _id AND its timestamp
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

    public FloatMicrogridDatum(long timestamp, MicrogridNode node, MicrogridMeasurementType measurementType, float value) {
        super(timestamp, node, measurementType);
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
