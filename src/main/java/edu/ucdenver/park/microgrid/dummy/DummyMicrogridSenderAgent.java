/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.dummy;

import edu.ucdenver.park.microgrid.agents.core.MicrogridSenderAgent;
import edu.ucdenver.park.microgrid.data.FloatMicrogridDatum;
import edu.ucdenver.park.microgrid.data.MicrogridFloatMeasurementType;
import edu.ucdenver.park.microgrid.data.MicrogridNode;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

/**
 * DummyMicrogridSenderAgent
 * <p>
 * class: JADE agent
 * <p>
 * this is a MicrogridSenderAgent that uses dummy microgrid data
 * <p>
 * this is a good reference for writing agents that extend MicrogridSenderAgent
 * to send data to MicrogridReceiverAgent
 * <p>
 * this agent is used for testing/developing the frontend UI
 * if you're looking for a file to copy/paste, use ExampleMicrogridSenderAgent
 *
 * @author Jake Billings
 */
public class DummyMicrogridSenderAgent extends MicrogridSenderAgent {
    /**
     * DummyMicrogridSenderAgent()
     * <p>
     * constructor
     * <p>
     * I don't think JADE likes for you to have params in your constructor. So, call super with everything
     * setup for your agent.
     * <p>
     * You need to call super with an agent id (AID) that points to the MicrogridReceiverAgent and a MicrogridGraph
     * that represents your graph structure
     */
    public DummyMicrogridSenderAgent() {
        //AID: this is what points to the MicrogridReceiverAgent
        // use the local name of the agent as the first string parameter
        // AID aid = new AID("jake", AID.ISLOCALNAME);

        //SUBGRAPH: this is the local grid that this agent knows about.
        // you will have to write code to initialize this grid in this constructor
        // when you copy it. I use the class DummyMicrogrid because this is a
        // Dummy agent
        // this is what describes the connections between generators, batteries
        // it tells the map what to draw
        // data we send will belong to nodes that are in this object
        // MicrogridGraph subgraph = new DummyMicrogrid();

        //GRIDUPDATEPERIOD: this is how often we update the map with what nodes we have
        // every gridUpdatePeriod milliseconds, we send another copy of subgraph to the
        // receiver agent. The data expires every gridUpdatePeriod + 1000 ms
        // as a result, when we stop this sender agent, it will take gridUpdatePeriod + 1000ms
        // to disappear from the map
        //
        // I recommend setting this value to about five seconds while testing so that your
        // changes show up properly.
        // In a production environment, this could easily be set to hours since the graph
        // doesn't really change
        // long gridUpdatePeriod = 5000;

        //Call super with the parameters from above
        super(new AID("ReceiverAgent", AID.ISLOCALNAME), new DummyMicrogrid(), 5000);
    }

    /**
     * setup()
     * <p>
     * override setup function so that we can add behaviors
     * the super.setup() adds one behavior that handles updating the microgrid graph data
     * make sure this line is present at the start of the function
     * <p>
     * then, we can add additional behaviors
     * <p>
     * since this is a dummy old, I add a behavior that sends dummy data for one node every 2 seconds
     * <p>
     * in an actual
     */
    protected void setup() {
        super.setup();

        addBehaviour(new TickerBehaviour(this, 2000) {
            @Override
            protected void onTick() {
                //Don't ever use this method to get nodes in a real agent. Store the important nodes
                // as instance variables. This is acceptable because this is a dummy and I used DummyMicrogridGraph
                MicrogridNode node = getSubgraph().getNodes().iterator().next();

                //This is an example of how to properly send a measurement to the server assuming you have a node
                sendDatum(
                        //Create a FloatMicrogridDatum object to hold the measurement
                        new FloatMicrogridDatum(
                                //Timestamp the datum with the current number of milliseconds from Jan 1st, 1970
                                System.currentTimeMillis(),
                                //Tell the Datum which node measured it
                                node,
                                //Pretend we're measuring voltage
                               MicrogridFloatMeasurementType.POTENTIAL,
                                //Provide a dummy value for the measurement
                                15.0F));
            }
        });
    }
}
