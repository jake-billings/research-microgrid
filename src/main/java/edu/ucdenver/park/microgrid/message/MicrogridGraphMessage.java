/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.message;

import edu.ucdenver.park.microgrid.data.MicrogridGraph;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * MicrogridGraphMessage
 *
 * class
 *
 * immutable
 *
 * The MicrogridGraphMessage represents a message that will be sent via Jade from a controller agent
 *  to the receiver agent in this project.
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
 *
 * @author Jake Billings
 */
public class MicrogridGraphMessage extends Message {
    public MicrogridGraphMessage() {}

    /**
     * subgraph
     *
     * MicrogridGraph
     *
     * this graph should contain all nodes and edges that the sending agent is aware of
     *
     * for instance, an agent may know about a battery, load, generator, and link to another neighborhood.
     *  the subgraph it sends should include nodes for the battery, load, generator. it should include
     *  edges between them where appropriate. it should include the edge linking it to the other neighborhood
     *
     * the graph need not contain information about nodes controlled by other agents.
     */
    private MicrogridGraph subgraph;

    /**
     * expirationMillis
     *
     * long
     *
     * the time the subgraph data in this message should expire given in milliseconds since Jan 1, 1970
     *  (see System.currentTimeMillis())
     *
     * agents sending microgridgraphmessages should arrange to send those messages at regular intervals
     *  each message should be set to expire just after the next one is scheduled to send
     *
     * for instance, send the graph data every ten seconds, and schedule each message to expire twelve seconds
     *  after it is sent
     *
     * the expiration allows us to keep an up-to-date picture of the complete grid; if one part goes offline, it is
     *  removed within a few seconds
     */
    private long expirationMillis;

    public MicrogridGraphMessage(MicrogridGraph subgraph, long expirationMillis) {
        this.subgraph = subgraph;
        this.expirationMillis = expirationMillis;
    }

    //-----Getters-----
    public MicrogridGraph getSubgraph() {
        return subgraph;
    }
    public long getExpirationMillis() {
        return expirationMillis;
    }

    //----Setters----
    private void setSubgraph(MicrogridGraph subgraph) {
        this.subgraph = subgraph;
    }

    private void setExpirationMillis(long expirationMillis) {
        this.expirationMillis = expirationMillis;
    }

    //----Externalizers----
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(2);
        out.writeLong(this.getExpirationMillis());
        this.getSubgraph().writeExternal(out);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setExpirationMillis(in.readLong());
        MicrogridGraph g = new MicrogridGraph();
        g.readExternal(in);
        this.setSubgraph(g);
    }
}
