/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.live;

import edu.ucdenver.park.microgrid.data.MicrogridDatum;
import edu.ucdenver.park.microgrid.data.MicrogridGraph;
import edu.ucdenver.park.microgrid.data.MicrogridNode;
import edu.ucdenver.park.microgrid.data.abs.Datum;
import edu.ucdenver.park.microgrid.message.MicrogridDatumMessage;
import edu.ucdenver.park.microgrid.message.MicrogridGraphMessage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LiveMicrogridGraph
 * <p>
 * class
 * <p>
 * mutable
 * <p>
 * LiveMicrogridGraph receives MicrogridGraphMessages and assembles a snapshot of how the grid currently
 * looks.
 * <p>
 * The message contains a subgraph of the full grid that will displayed on the final map. This subgraph
 * should contain all nodes and edges that the agent that sent it knows about. This will likely be
 * statically configured in the agent.
 * <p>
 * The message also contains an expiration. This is critical to maintaining an accurate picture of the grid
 * in LiveMicroGridGraph. Graph information must expire eventually because the state of the grid will not
 * be permanent. We place the rate of expiration in the message in order to force agents to choose how
 * long the data lives for.
 * <p>
 * Agents sending MicrogridGraphMessages should send the graphMessages at regular intervals (instead of just once).
 * Messages should be set to expire a few seconds after the next message is scheduled to be sent. As a result,
 * if an agent goes offline, its subgraph will be removed from the grid state once its next scheduled message is
 * missed. Once it comes back online, its subgraph is added to the main state immediately.
 *
 * @author Jake Billings
 */
public class LiveMicrogridGraph {
    /**
     * graphMessages
     * <p>
     * Map String, MicrogridGraphMessage
     * <p>
     * this map stores the latest message for each individual subgraph id contained within that message
     * <p>
     * we don't remove expired graphMessages. we just ignore them when assembling the complete graph
     * <p>
     * snapshots of state iterate through these values (ignoring expired message) and union all the graphs
     * <p>
     * received events put() a message in this map based on the _id property of the graph in the message
     */
    private final Map<String, MicrogridGraphMessage> graphMessages = new ConcurrentHashMap<String, MicrogridGraphMessage>();

    /**
     * datumMessages
     * <p>
     * Map String, MicrogridDatumMessage
     * <p>
     * this map stores the last datum received for each unique datum _id received
     * <p>
     * it will be used to build "snapshots" of the current state of all measurements for all nodes
     * <p>
     * these snapshots will be sent to frontend via socket io on a regular basis
     * <p>
     * this saves communication overhead with the web client when we start receiving a lot of data
     */
    private final Map<String, MicrogridDatumMessage> datumMessages = new ConcurrentHashMap<String, MicrogridDatumMessage>();

    /**
     * receiveMessage(MicrogridGraphMessage)
     * <p>
     * updates the graphMessages map with this latest message
     * <p>
     * this can be considered updating the state with the contents of the message
     * <p>
     * future state snapshots will include this information until it expires
     * <p>
     * fires the "onMicrogridGraph" event to MicrogridGraphHandlers
     *
     * @param message a MicrogridGraph message representing the complete current state of the subgraph of the agent that sent it
     */
    public void receiveMessage(MicrogridGraphMessage message) {
        graphMessages.put(message.getSubgraph().get_id(), message);
    }

    /**
     * receiveMessage(MicrogridDatumMessage)
     * <p>
     * updates the graphMessages map with this latest message
     * <p>
     * this can be considered updating the state with the contents of the message
     * <p>
     * future state snapshots will include this information until it expires
     * <p>
     * fires the "onDatum" event to DatumHandlers
     *
     * @param message a MicrogridDatumMessage message containing a datum
     */
    public void receiveMessage(MicrogridDatumMessage message) {
        datumMessages.put(message.getDatum().get_id(), message);
        fireDatumEvent(message.getDatum());
    }

    /**
     * getCurrentState()
     * <p>
     * iterates through the values of graphMessages ignoring expired graphMessages
     * <p>
     * unions all of the graphs to create one large graph representing the entire known grid based on the emssages we have
     *
     * @return a MicrogridGraph object representing the known graph state at this point in time
     */
    public MicrogridGraph getCurrentState() {
        long now = System.currentTimeMillis();
        MicrogridGraph graph = new MicrogridGraph("live-microgrid-graph");
        for (MicrogridGraphMessage message : graphMessages.values()) {
            if (message.getExpirationMillis() > now) {
                graph = graph.union(graph.get_id(), message.getSubgraph());
            } else {
                System.out.println(message.getSubgraph().get_id() + " is expired by " + (now - message.getExpirationMillis()) + " ms");
            }
        }
        return graph;
    }

    /**
     * getSnapshotForNode()
     * <p>
     * filters the "up-to-date" measurement set to just values that are for the node n
     * then, returns a MicrogridNodeSnapshot, which contains the node information AND
     * the measurement information
     *
     * @param n the node to search for
     * @return a snapshot of measruement values of node n
     */
    public MicrogridNodeSnapshot getCurrentNodeState(MicrogridNode n) {
        Set<MicrogridDatum> data = new HashSet<MicrogridDatum>();
        for (MicrogridDatumMessage message : datumMessages.values()) {
            MicrogridDatum datum = message.getDatum();
            if (datum.getNode().get_id().equals(n.get_id())) {
                data.add(datum);
            }
        }
        return new MicrogridNodeSnapshot(n, data);
    }

    /**
     * getCurrentStateForAllNodes()
     * <p>
     * calls getCurrentState() to get the nodes from the graph; then, calls getSnapshotForNode() on each node
     * yielding a set of microgrid snapshots representing the current measurement state at each node in
     * the graph
     * <p>
     * filters out nodes that don't have measurements
     *
     * @return a set of microgrid node snapshots representing the last known measurement state at each node
     */
    public Set<MicrogridNodeSnapshot> getCurrentStateForAllNodes() {
        MicrogridGraph grid = this.getCurrentState();
        Set<MicrogridNodeSnapshot> snapshots = new HashSet<MicrogridNodeSnapshot>();
        for (MicrogridNode n : grid.getNodes()) {
            MicrogridNodeSnapshot snapshot = getCurrentNodeState(n);
            if (snapshot.getMeasurements().size() > 0) {
                snapshots.add(snapshot);
            }
        }
        return snapshots;
    }

    //--Event Handler Sets--
    private final Set<DatumHandler> datumHandlers = new HashSet<DatumHandler>();

    //--Handler Registrations--
    public void registerDatumHadler(DatumHandler h) {
        datumHandlers.add(h);
    }

    //--Fire Methods--
    private void fireDatumEvent(Datum d) {
        for (DatumHandler h : datumHandlers) {
            h.onDatum(d);
        }
    }

}
