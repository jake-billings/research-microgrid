/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * MicrogridFloatMeasurementType
 *
 * enum
 *
 * types of measurements MicrogridFloatDatum can have and unit strings for them
 *
 * E.g. Voltage-Volts-V
 *
 * @author Jake Billings
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MicrogridFloatMeasurementType {
    VOLTAGE(1, "Voltage", "Volts", "V", "Potential Difference"),
    AMPERAGE(2, "Amperage", "Amps", "A", "Current"),
    WATTAGE(3, "Wattage", "Watts", "W", "Power");

    /**
     * _id
     *
     * int
     *
     * number representing the type of edge; will be in the data packet in the JADE transport protocol
     * should be declared in global protocol spec
     *
     * by an arbitrary convention I made up, these are positive for float measurement types
     *  (as opposed to negative for boolean types)
     *
     * See render.js
     *
     * Ex: 2
     */
    private final int _id;

    /**
     * name
     *
     * String
     *
     * the human-readable name of this measurement
     *
     * Ex: "Voltage", "Amperage"
     */
    private final String name;

    /**
     *  baseUnitType
     *
     * String
     *
     * the human-readable name of this measurement
     *
     * Ex: "Current", "Power"
     */
    private final String baseUnitType;

    /**
     * unitName
     *
     * String
     *
     * the human-readable unit name of this type of microgrid edge
     *
     * Ex: "Volts", "Amps"
     */
    private final String unitName;

    /**
     * unitAbbreviation
     *
     * String
     *
     * the human-readable unit name of this type of microgrid edge
     *
     * Ex: "V", "A"
     */
    private final String unitAbbreviation;

    /**
     * MicrogridFloatMeasurementType
     *
     * constructor
     *
     * @param _id  the numeric id (decided in protocol spec) 1
     * @param name the human-readable name "Volatage"
     * @param unitName the human-readable unit name "Volts"
     * @param unitAbbreviation "V"
     */
    MicrogridFloatMeasurementType(int _id, String name, String unitName, String unitAbbreviation, String baseUnitType) {
        this._id = _id;
        this.name = name;
        this.unitName = unitName;
        this.unitAbbreviation = unitAbbreviation;
        this.baseUnitType = baseUnitType;
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
    public static MicrogridFloatMeasurementType fromId(byte _id) {
        for (MicrogridFloatMeasurementType a : MicrogridFloatMeasurementType.class.getEnumConstants()) {
            if (a.get_id() == _id) {
                return a;
            }
        }
        throw new IllegalArgumentException("No MicrogridFloatMeasurementType with that _id");
    }

    //----Getters----+
    public int get_id() {
        return this._id;
    }
    public String getName() {
        return name;
    }
    public String getUnitName() {
        return unitName;
    }
    public String getUnitAbbreviation() {
        return unitAbbreviation;
    }
    public String getBaseUnitType() {
        return baseUnitType;
    }
}
