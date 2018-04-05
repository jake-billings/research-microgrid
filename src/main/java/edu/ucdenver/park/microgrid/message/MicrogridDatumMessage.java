/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.message;

import edu.ucdenver.park.microgrid.data.MicrogridDatum;
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
 *
 * @author Jake Billings
 */
public class MicrogridDatumMessage extends Message {
    private final MicrogridDatum datum;

    public MicrogridDatumMessage(MicrogridDatum datum) {
        this.datum = datum;
    }

    public MicrogridDatum getDatum() {
        return datum;
    }
}
