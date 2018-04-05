/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
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
 * Important: the _id field of a Datum should be unique to the combination of its type and measurement location
 *  as a result, data from the same measurement location and type will have the same _id but different
 *  timestamp and value; this is important for data caching in LiveMicrogrid
 *
 * A unique identifier for an individual datum object should contain its _id AND its timestamp
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

    /**
     * measurementType
     *
     * MicrogridMeasurementType
     *
     * see the MicrogridMeasurementType docs
     *
     * this is the type of measurement made (e.g. Volts, Amps, Watts)
     */
    private final MicrogridMeasurementType measurementType;

    public MicrogridDatum(long timestamp, MicrogridNode node, MicrogridMeasurementType measurementType) {
        super(node.get_id() + "-" + measurementType.get_id(), timestamp);
        this.node = node;
        this.measurementType = measurementType;
    }

    public MicrogridNode getNode() {
        return node;
    }

    public MicrogridMeasurementType getMeasurementType() {
        return measurementType;
    }
}
