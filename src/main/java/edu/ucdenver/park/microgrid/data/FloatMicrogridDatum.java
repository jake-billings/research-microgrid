/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
     * measurementType
     *
     * MicrogridFloatMeasurementType
     *
     * see the MicrogridFloatMeasurementType docs
     *
     * this is the type of measurement made (e.g. Volts, Amps, Watts)
     */
    private MicrogridFloatMeasurementType measurementType;

    /**
     * value
     *
     * float
     *
     * the actual float reading represented by this datum object
     */
    private float value;

    /**
     * FloatMicrogridDatum
     *
     * constructor: empty
     *
     * for use with deserialization; do not use for instantiation
     */
    public FloatMicrogridDatum() {
        super();
    }

    /**
     * FloatMicrogridDatum
     *
     * constructor
     *
     * @param timestamp the unix time this datum was recorded at
     * @param node the node object the datum was recorded at
     * @param measurementType the MicrogridFloatMeasurementType of this measurement
     * @param value the floating point value of this measurement
     */
    public FloatMicrogridDatum(long timestamp, MicrogridNode node, MicrogridFloatMeasurementType measurementType, float value) {
        super(timestamp, node, measurementType.get_id());
        this.value = value;
        this.measurementType = measurementType;
    }

    //----Getters----
    public MicrogridFloatMeasurementType getMeasurementType() {
        return measurementType;
    }
    public float getValue() {
        return value;
    }

    //----Setters----
    private void setMeasurementType(MicrogridFloatMeasurementType measurementType) {
        this.measurementType = measurementType;
    }
    private void setValue(float value) {
        this.value = value;
    }
    //----Externalizers----
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeFloat(this.getValue());
        out.writeByte(this.getMeasurementType().get_id());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        this.setValue(in.readFloat());
        this.setMeasurementType(MicrogridFloatMeasurementType.fromId(in.readByte()));
    }
}
