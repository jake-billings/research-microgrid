package edu.ucdenver.park.microgrid.data;

/**
 * MicrogridMeasurementType
 *
 * enum
 *
 * types of measurements MicrogridDatum can have and unit strings for them
 *
 * E.g. Voltage-Volts-V
 */
public enum MicrogridMeasurementType {
    VOLTAGE(1, "Voltage", "Volts", "V"),
    AMPERAGE(2, "Amperage", "Amps", "A"),
    WATTAGE(3, "Wattage", "Watts", "W");

    /**
     * _id
     *
     * int
     *
     * number representing the type of edge; will be in the data packet in the JADE transport protocol
     * should be declared in global protocol spec
     *
     * Ex: 2
     */
    private final int _id;

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
     * MicrogridMeasurementType
     *
     * constructor
     *
     * @param _id  the numeric id (decided in protocol spec) 1
     * @param name the human-readable name "Volatage"
     * @param unitName the human-readable unit name "Volts"
     * @param unitAbbreviation "V"
     */
    MicrogridMeasurementType(int _id, String name, String unitName, String unitAbbreviation) {
        this._id = _id;
        this.name = name;
        this.unitName = unitName;
        this.unitAbbreviation = unitAbbreviation;
    }


    public int get_id() {
        return this._id;
    }

    public String getName() {
        return name;
    }
}
