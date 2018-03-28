package edu.ucdenver.park.microgrid.live;

import edu.ucdenver.park.microgrid.data.abs.Datum;

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
 */
public class MicrogridDatumMessage extends Message {
    private final Datum datum;

    public MicrogridDatumMessage(Datum datum) {
        this.datum = datum;
    }

    public Datum getDatum() {
        return datum;
    }
}
