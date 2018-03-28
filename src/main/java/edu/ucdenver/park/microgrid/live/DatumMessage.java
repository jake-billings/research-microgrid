package edu.ucdenver.park.microgrid.live;

import edu.ucdenver.park.microgrid.data.abs.Datum;

/**
 * DatumMessage
 *
 * class
 *
 * message
 *
 * This message wraps a Datum object allowing it to be transported via the live message protocol
 *
 * It is received by LiveMicrogridGraph
 */
public class DatumMessage extends Message {
    private final Datum datum;

    public DatumMessage(Datum datum) {
        this.datum = datum;
    }

    public Datum getDatum() {
        return datum;
    }
}
