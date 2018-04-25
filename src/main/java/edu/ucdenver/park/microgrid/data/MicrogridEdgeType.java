/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

/**
 * MicrogridEdgeType
 * 
 * enum
 * 
 * "connection type"
 * 
 * Each microgrid edge will have a type such as "power connection" or "data connection"
 * Since these edges don't behave differently in code; only in rendering, they are represented using an
 * enum type field on the MircrogridEdge object (as opposed to subclasses)
 *
 * @author Jake Billings
 */
public enum MicrogridEdgeType {
    //todo actual connection types
    BUS((byte) 1, "Bus");

    /**
     * _id
     * 
     * byte
     * 
     * number representing the type of edge; will be in the data packet in the JADE transport protocol
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
     * the human-readable name of this type of microgrid edge
     * 
     * Ex: "Power Connection", "Bus"
     */
    private final String name;

    /**
     * MicrogridEdgeType
     *
     * @param _id  the numeric id (decided in protocol spec)
     * @param name the human-readable name
     */
    MicrogridEdgeType(byte _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public int get_id() {
        return this._id;
    }

    public String getName() {
        return name;
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
    public static MicrogridEdgeType fromId(byte _id) {
        for (MicrogridEdgeType a : MicrogridEdgeType.class.getEnumConstants()) {
            if (a.get_id() == _id) {
                return a;
            }
        }
        throw new IllegalArgumentException("No MicrogridEdgeType with that _id");
    }
}
