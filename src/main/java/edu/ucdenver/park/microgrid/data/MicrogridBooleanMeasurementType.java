/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * MicrogridBooleanMeasurementType
 *
 * enum
 *
 * types of measurements MicrogridBooleamDatum can have and unit strings for them
 *
 * E.g. Voltage-Volts-V
 *
 * @author Jake Billings
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MicrogridBooleanMeasurementType {
    FAULT((byte) -1, "Fault", "Fault", "Nominal"),
    WARNING((byte) -2, "Warning", "Warning", "Nominal"),
    CIRCUIT_BREAKER_TRIPPED((byte) -3, "Circuit Breaker", "Open", "Nominal (Closed)");

    /**
     * _id
     *
     * byte
     *
     * number representing the type of edge; will be in the data packet in the JADE transport protocol
     * should be declared in global protocol spec
     *
     * by an arbitrary convention I made up, these are negative for boolean measurement types
     *  (as opposed to positive for float types)
     *
     * See render.js
     *
     * Ex: -2
     */
    private final byte _id;

    /**
     * name
     *
     * String
     *
     * the human-readable name of this type of microgrid edge
     *
     * Ex: "Power Connection", "Bus"
     */
    private final String name;

    /**
     * trueName
     *
     * String
     *
     * the human-readable value if this property is true
     *
     * E.g. "No Fault" or "Closed"
     */
    private final String trueName;

    /**
     * trueName
     *
     * String
     *
     * the human-readable value if this property is true
     *
     * E.g. "Fault" or "Open"
     */
    private final String falseName;

    /**
     * MicrogridBooleanMeasurementType
     *
     * enum constructor
     *
     * @param _id the unique negative _id
     * @param name the name of the measurement type
     * @param trueName the name if it's true (e.g. closed)
     * @param falseName the name if it's false (e.g. open)
     */
    MicrogridBooleanMeasurementType(byte _id, String name, String trueName, String falseName) {
        this._id = _id;
        this.name = name;
        this.trueName = trueName;
        this.falseName = falseName;
    }

    /**
     * fromId()
     *
     * returns the enum item that has the provided _id
     *
     * throws IllegalArgumentException if there is no item with that _id
     *
     * @param _id the _id to match
     * @return the enum item
     */
    public static MicrogridBooleanMeasurementType fromId(byte _id) {
        for (MicrogridBooleanMeasurementType a : MicrogridBooleanMeasurementType.class.getEnumConstants()) {
            if (a.get_id() == _id) {
                return a;
            }
        }
        throw new IllegalArgumentException("No MicrogridBooleanMeasurementType with that _id");
    }

    //----Getters----
    public byte get_id() {
        return _id;
    }
    public String getName() {
        return name;
    }
    public String getTrueName() {
        return trueName;
    }
    public String getFalseName() {
        return falseName;
    }
}
