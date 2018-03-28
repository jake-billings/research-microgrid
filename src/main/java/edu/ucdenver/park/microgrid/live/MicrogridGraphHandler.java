package edu.ucdenver.park.microgrid.live;

import edu.ucdenver.park.microgrid.data.MicrogridGraph;

/**
 * MicrogridGraphHandler
 *
 * interface
 *
 * implementations may be used as microgrid graph event handlers registered to LiveMicrogridGraph
 */
public interface MicrogridGraphHandler {

    /**
     * onMicrogridGraph()
     *
     * called when there is an updated microgrid graph available
     *
     * @param graph a snapshot of the state of the LiveMicrogridGraph as a MicrogridGraph object
     */
    void onMicrogridGraph(MicrogridGraph graph);
}
