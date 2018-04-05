package edu.ucdenver.park.microgrid.live;

import java.util.Set;

/**
 * MicrogridNodeSnapshotHandler
 *
 * interface
 *
 * handler interface for a set of microgrid node snapshots
 *
 * implemented my MicrogridSocketIOServer
 * called in ReceiverAgent
 */
public interface MicrogridNodeSnapshotHandler {
    /**
     * onMicrogridNodeSnapshots
     *
     * method
     *
     * called when a set of snapshots is available
     *
     * @param snapshots a complete set of node snapshots for a current known grid state
     */
    public void onMicrogridNodeSnapshots(Set<MicrogridNodeSnapshot> snapshots);
}
