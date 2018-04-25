/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.message;

import edu.ucdenver.park.microgrid.data.BooleanMicrogridDatum;
import edu.ucdenver.park.microgrid.data.FloatMicrogridDatum;
import edu.ucdenver.park.microgrid.data.MicrogridDatum;
import edu.ucdenver.park.microgrid.data.abs.Datum;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * MicrogridDatumMessage
 *
 * class
 *
 * message
 *
 * This message wraps a Datum object allowing it to be transported via the live message protocol
 *
 * It is received by LiveMicrogridGraph
 *
 * @author Jake Billings
 */
public class MicrogridDatumMessage extends Message {
    /**
     * MicrogridGraph
     *
     * constructor: empty
     *
     * for use with deserialization; do not use for instantiation
     */
    public MicrogridDatumMessage() {}

    private MicrogridDatum datum;

    public MicrogridDatumMessage(MicrogridDatum datum) {
        this.datum = datum;
    }

    //----Getters----
    public MicrogridDatum getDatum() {
        return datum;
    }

    //----Setters----
    private void setDatum(MicrogridDatum datum) {
        this.datum = datum;
    }

    //----Externalizers----
    public void writeExternal(ObjectOutput out) throws IOException {
        if (this.getDatum() instanceof FloatMicrogridDatum) {
            out.writeByte(0x01);
        } else if (this.getDatum() instanceof BooleanMicrogridDatum) {
            out.writeByte(0x02);
        } else {
            throw new IllegalArgumentException("invalid datum type in MicrogridDatumMessage");
        }
        this.getDatum().writeExternal(out);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        byte typeIndicator = in.readByte();
        MicrogridDatum m;
        if (typeIndicator == 0x01) {
            m = new FloatMicrogridDatum();
        } else if (typeIndicator == 0x02) {
            m = new BooleanMicrogridDatum();
        } else {
            throw new IllegalArgumentException("invalid datum type indicator");
        }
        m.readExternal(in);
        this.setDatum(m);
    }
}
