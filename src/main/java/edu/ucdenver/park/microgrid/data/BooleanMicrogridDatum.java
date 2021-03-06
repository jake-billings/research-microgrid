/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * BooleanMicrogridDatum
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
@Access(AccessType.PROPERTY)
@Entity
public class BooleanMicrogridDatum extends MicrogridDatum {
    /**
     * measurementType
     *
     * MicrogridBooleanMeasurementType
     *
     * see the MicrogridBooleanMeasurementType docs
     *
     * this is the type of measurement made (e.g. Fault, CircuitBreakerStatus)
     */
    @Transient
    @JsonInclude
    private MicrogridBooleanMeasurementType measurementType;

    /**
     * value
     *
     * float
     *
     * the actual float reading represented by this datum object
     */
    @Transient
    @JsonInclude
    private boolean value;

    /**
     * BooleanMicrogridDatum
     *
     * constructor: empty
     *
     * for use with deserialization; do not use for instantiation
     */
    public BooleanMicrogridDatum() {}

    /**
     * BooleanMicrogridDatum()
     *
     * constructor
     *
     * @param timestamp the unix timestamp this datum was recorded at
     * @param node the node object this datum was recorded at
     * @param measurementType the boolean measurement type of this datum
     * @param value the boolean value of this measurement
     */
    public BooleanMicrogridDatum(long timestamp, MicrogridNode node, MicrogridBooleanMeasurementType measurementType, boolean value) {
        super(timestamp, node, measurementType.get_id());
        this.value = value;
        this.measurementType = measurementType;
    }

    //----DB Property Getters----
    @Column(name="measurement_type_id")
    public byte getMeasurementTypeId() {
        return this.getMeasurementType().get_id();
    }
    public void setMeasurementTypeId(byte newId) {
        this.setMeasurementType(MicrogridBooleanMeasurementType.fromId(newId));
    }
    @Column(name = "value")
    public boolean getValue() {
        return value;
    }

    //----Other Getters----
    @Transient
    @JsonInclude
    public MicrogridBooleanMeasurementType getMeasurementType() {
        return measurementType;
    }

    //----Setters----
    private void setMeasurementType(MicrogridBooleanMeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    private void setValue(boolean value) {
        this.value = value;
    }

    //----Externalizers----
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeBoolean(this.getValue());
        out.writeByte(this.getMeasurementType().get_id());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        this.setValue(in.readBoolean());
        this.setMeasurementType(MicrogridBooleanMeasurementType.fromId(in.readByte()));
    }
}
