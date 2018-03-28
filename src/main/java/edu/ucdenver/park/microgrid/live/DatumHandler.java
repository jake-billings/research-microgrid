package edu.ucdenver.park.microgrid.live;

import edu.ucdenver.park.microgrid.data.abs.Datum;

/**
 * DatumHandler
 *
 * interface
 *
 * implementations may be used as datum event handlers registered to LiveMicrogridGraph
 */
public interface DatumHandler {
    /**
     * onDatum()
     *
     * called when a datum is received from a message
     *
     * @param datum the datum received in the message
     */
    void onDatum(Datum datum);
}
