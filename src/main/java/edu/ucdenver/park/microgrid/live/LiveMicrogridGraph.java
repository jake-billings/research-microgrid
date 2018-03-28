package edu.ucdenver.park.microgrid.live;

import edu.ucdenver.park.microgrid.data.MicrogridGraph;

import java.util.HashMap;
import java.util.Map;

/**
 * LiveMicrogridGraph
 *
 * class
 *
 * mutable
 *
 * LiveMicrogridGraph receives MicrogridGraphMessages and assembles a snapshot of how the grid currently
 *  looks.
 *
 * The message contains a subgraph of the full grid that will displayed on the final map. This subgraph
 *  should contain all nodes and edges that the agent that sent it knows about. This will likely be
 *  statically configured in the agent.
 *
 * The message also contains an expiration. This is critical to maintaining an accurate picture of the grid
 *  in LiveMicroGridGraph. Graph information must expire eventually because the state of the grid will not
 *  be permanent. We place the rate of expiration in the message in order to force agents to choose how
 *  long the data lives for.
 *
 * Agents sending MicrogridGraphMessages should send the messages at regular intervals (instead of just once).
 *  Messages should be set to expire a few seconds after the next message is scheduled to be sent. As a result,
 *  if an agent goes offline, its subgraph will be removed from the grid state once its next scheduled message is
 *  missed. Once it comes back online, its subgraph is added to the main state immediately.
 */
public class LiveMicrogridGraph {
    /**
     * messages
     *
     * Map String, MicrogridGraphMessage
     *
     * this map stores the latest message for each individual subgraph id contained within that message
     *
     * we don't remove expired messages. we just ignore them when assembling the complete graph
     *
     * snapshots of state iterate through these values (ignoring expired message) and union all the graphs
     *
     * received events put() a message in this map based on the _id property of the graph in the message
     */
    private final Map<String, MicrogridGraphMessage> messages = new HashMap<String, MicrogridGraphMessage>();

    /**
     * receiveMessage()
     *
     * updates the messages map with this latest message
     *
     * this can be considered updating the state with the contents of the message
     *
     * future state snapshots will include this information until it expires
     *
     * @param message a MicrogridGraph message representing the complete current state of the subgraph of the agent that sent it
     */
    public void receiveMessage(MicrogridGraphMessage message) {
        messages.put(message.getSubgraph().get_id(), message);
    }

    /**
     * getCurrentState()
     *
     * iterates through the values of messages ignoring expired messages
     *
     * unions all of the graphs to create one large graph representing the entire known grid based on the emssages we have
     *
     * @return a MicrogridGraph object representing the known graph state at this point in time
     */
    public MicrogridGraph getCurrentState() {
        long now = System.currentTimeMillis();
        MicrogridGraph graph = new MicrogridGraph("live-microgrid-graph");
        for (MicrogridGraphMessage message : messages.values()) {
            if (message.getExpirationMillis() > now) {
                graph = graph.union(graph.get_id(), message.getSubgraph());
            }
        }
        return graph;
    }
}