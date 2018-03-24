package edu.ucdenver.park.microgrid.data;

/**
 * MicrogridNodeType
 *
 * enum
 *
 * Each node in the network has a type that does not affect its behavior in this code. For instance, a node can be a
 *  battery, controller, or generator. The purpose of this section of code is to render the network, so we don't
 *  treat these elements with different logic. Thus, we implement their type as an enum.
 */
public enum MicrogridNodeType {
    GENERATOR(1, "Generator"),
    BATTERY(2, "Battery"),
    CONTROLLER(3, "Controller");

    /**
     * _id
     *
     * int
     *
     * number representing the type of node; will be in the data packet in the JADE transport protocol
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
     * the human-readable name of this type of microgrid node
     *
     * Ex: "Generator", "Battery"
     */
    private final String name;

    MicrogridNodeType(int _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }
}
