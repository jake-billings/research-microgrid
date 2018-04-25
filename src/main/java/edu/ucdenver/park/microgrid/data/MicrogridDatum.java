/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data;

import edu.ucdenver.park.microgrid.data.abs.Datum;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
public abstract class MicrogridDatum extends Datum {
    /**
     * node
     *
     * MicrogridNode
     *
     * the node at which this datum was recorded
     */
    private MicrogridNode node;

    /**
     * MicrogridDatum
     *
     * constructor: empty
     *
     * for use with deserialization; do not use for instantiation
     */
    public MicrogridDatum() {
        super();
    }

    /**
     * MicrogridDatum
     *
     * constructor
     *
     * @param timestamp timestamp when datum was created
     * @param node node the datum was recorded at
     * @param measurementTypeId the id of the measurement type
     */
    public MicrogridDatum(long timestamp, MicrogridNode node, int measurementTypeId) {
        super(node.get_id() + "-" + measurementTypeId, timestamp);
        this.node = node;
    }

    //----Getters----
    public MicrogridNode getNode() {
        return node;
    }

    //----Setters----
    private void setNode(MicrogridNode node) {
        this.node = node;
    }

    //----Externalizers----
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        this.getNode().writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        MicrogridNode node = new MicrogridNode();
        node.readExternal(in);
        this.setNode(node);
    }
}
