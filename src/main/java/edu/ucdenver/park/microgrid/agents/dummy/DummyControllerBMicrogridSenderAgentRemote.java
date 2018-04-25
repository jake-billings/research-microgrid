/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.agents.dummy;

import edu.ucdenver.park.microgrid.agents.core.MicrogridSenderAgent;
import edu.ucdenver.park.microgrid.data.*;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import java.util.HashSet;
import java.util.Set;

/**
 * DummyControllerAMicrogridSenderAgent
 * <p>
 * class: JADE agent
 * <p>
 * this is a MicrogridSenderAgent that uses dummy microgrid data
 * <p>
 * See ExampleMicrogridSenderAgent for more documentation
 *
 * @author Jake Billings
 */
public class DummyControllerBMicrogridSenderAgentRemote extends MicrogridSenderAgent {
    /**
     * makeReceiverAID()
     *
     * @return an AID pointing to the agent that will receive the data we send
     */
    private static AID makeReceiverAID() {
        AID aid = new AID("ReceiverAgent@10.20.102.203:1100/JADE", AID.ISGUID);
        aid.addAddresses("http://NC2611-PC-16.ucdenver.pvt:7778/acc");
        return aid;
    }

    //Dummy Controller
    private static DummyPhysicalController controller = DummyPhysicalController.getInstance();

    //Nodes
    private static MicrogridNode g = new MicrogridNode("microgrid-node-b-g", MicrogridNodeType.GENERATOR);
    private static MicrogridNode b = new MicrogridNode("microgrid-node-b-b", MicrogridNodeType.BATTERY);
    private static MicrogridNode l = new MicrogridNode("microgrid-node-b-l", MicrogridNodeType.LOAD);
    private static MicrogridNode gBreaker = new MicrogridNode("microgrid-node-b-g-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
    private static MicrogridNode bBreaker = new MicrogridNode("microgrid-node-b-b-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
    private static MicrogridNode lBreaker = new MicrogridNode("microgrid-node-b-l-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
    private static MicrogridNode ourHub = new MicrogridNode("micrgrid-node-b-hub", MicrogridNodeType.HUB);
    private static MicrogridNode ourBreaker = new MicrogridNode("microgrid-node-b-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
    private static MicrogridNode centralHub = new MicrogridNode("microgrid-node-central-b-hub", MicrogridNodeType.HUB);

    /**
     *
     * makeMicrogridGraph()
     *
     * @return a microgrid graph representing the subgraph we know about
     */
    private static MicrogridGraph makeMicrogridGraph() {
        //Declare two hash sets to hold our nodes and edges
        Set<MicrogridNode> nodes = new HashSet<MicrogridNode>();
        Set<MicrogridEdge> edges = new HashSet<MicrogridEdge>();

        //Add nodes to set
        nodes.add(g);
        nodes.add(b);
        nodes.add(l);
        nodes.add(gBreaker);
        nodes.add(bBreaker);
        nodes.add(lBreaker);
        nodes.add(ourHub);
        nodes.add(ourBreaker);
        nodes.add(centralHub);

        //Edges
        edges.add(new MicrogridEdge("microgrid-edge-b-gb", g, gBreaker, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-b-bb", b, bBreaker, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-b-lb", l, lBreaker, MicrogridEdgeType.BUS));

        edges.add(new MicrogridEdge("microgrid-edge-b-gbh", gBreaker, ourHub, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-b-bbh", bBreaker, ourHub, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-b-lbh", lBreaker, ourHub, MicrogridEdgeType.BUS));

        edges.add(new MicrogridEdge("microgrid-edge-b-hb", ourHub, ourBreaker, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-b-bch", ourBreaker, centralHub, MicrogridEdgeType.BUS));

        return new MicrogridGraph("microgrid-graph-subgraph-b", edges, nodes);
    }

    /**
     * makeGridUpdatePeriod()
     *
     * @return the number of milliseconds between grid updates
     */
    private static long makeGridUpdatePeriod() {
        return 5000;
    }

    /**
     * ExampleMicrogridSenderAgent()
     * <p>
     * constructor
     */
    public DummyControllerBMicrogridSenderAgentRemote() {
        //Call super with the parameters from above
        super(makeReceiverAID(), makeMicrogridGraph(), makeGridUpdatePeriod());
    }

    

    /**
     * setup()
     * <p>
     * override setup function so that we can add behaviors
     * <p>
     * a note on period:
     * when generating a dummy 1HZ sine wave and sampling at 1/2000ms, I found that the sine value appeared
     * to stay the same due to the harmonic interaction of the TickerBehavior and the wave. If  you are sampling waves,
     * I recommend picking a prime number that is far smaller than the period of the wave.
     * <p>
     * However, keep in mind that this period must also be much lower than the blocking time of the receiver agent, or
     * you will find errors due to the queue filling up
     */
    protected void setup() {
        super.setup();

        addBehaviour(new TickerBehaviour(this, 100) {
            @Override
            protected void onTick() {
                MicrogridNode node = getSubgraph().getNodes().iterator().next();

                sendDatum(
                        new FloatMicrogridDatum(
                                System.currentTimeMillis(),
                                g,
                                MicrogridFloatMeasurementType.VOLTAGE,
                                controller.getVoltage()));
                sendDatum(
                        new FloatMicrogridDatum(
                                System.currentTimeMillis(),
                                g,
                                MicrogridFloatMeasurementType.AMPERAGE,
                                controller.getAmperage()));
                sendDatum(
                        new FloatMicrogridDatum(
                                System.currentTimeMillis(),
                                gBreaker,
                                MicrogridFloatMeasurementType.AMPERAGE,
                                controller.getAmperage()));
                sendDatum(
                        new FloatMicrogridDatum(
                                System.currentTimeMillis(),
                                g,
                                MicrogridFloatMeasurementType.WATTAGE,
                                controller.getWattage()));
                sendDatum(
                        new BooleanMicrogridDatum(
                                System.currentTimeMillis(),
                                g,
                                MicrogridBooleanMeasurementType.FAULT,
                                controller.isFault()));
                sendDatum(
                        new BooleanMicrogridDatum(
                                System.currentTimeMillis(),
                                g,
                                MicrogridBooleanMeasurementType.WARNING,
                                controller.isWarning()));
                sendDatum(
                        new BooleanMicrogridDatum(
                                System.currentTimeMillis(),
                                gBreaker,
                                MicrogridBooleanMeasurementType.CIRCUIT_BREAKER_TRIPPED,
                                controller.isCircuitBreakerOpen()));
            }
        });
    }
}
