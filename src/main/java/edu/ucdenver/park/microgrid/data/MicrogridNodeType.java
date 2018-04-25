/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

/**
 * MicrogridNodeType
 *
 * enum
 *
 * Each node in the network has a type that does not affect its behavior in this code. For instance, a node can be a
 *  battery, controller, or generator. The purpose of this section of code is to render the network, so we don't
 *  treat these elements with different logic. Thus, we implement their type as an enum.
 *
 * @author Jake Billings
 */
public enum MicrogridNodeType {
    GENERATOR((byte) 1, "Generator"),
    BATTERY((byte) 2, "Battery"),
    LOAD((byte) 3, "Load"),
    CIRCUIT_BREAKER((byte) 4, "Circuit Breaker"),
    HUB((byte) 5, "Hub");

    /**
     * _id
     *
     * byte
     *
     * number representing the type of node; will be in the data packet in the JADE transport protocol
     * should be declared in global protocol spec
     *
     * Ex: 2
     */
    private final byte _id;

    /**
     * name
     *
     * String
     *
     * the human-readable name of this type of microgrid node
     *
     * Ex: "Generator", "Battery"
     */
    private final String name;

    MicrogridNodeType(byte _id, String name) {
        this._id = _id;
        this.name = name;
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
    public static MicrogridNodeType fromId(byte _id) {
        for (MicrogridNodeType a : MicrogridNodeType.class.getEnumConstants()) {
            if (a.get_id() == _id) {
                return a;
            }
        }
        throw new IllegalArgumentException("No MicrogridNodeType with that _id");
    }

    //----Getters----
    public byte get_id() {
        return _id;
    }
    public String getName() {
        return name;
    }
}
