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
 * DummyTestScenario
 * <p>
 * class: JADE agent
 * <p>
 * this is a MicrogridSenderAgent that uses dummy microgrid data
 * <p>
 * this agent implements a specific test case from Dr. Park (180406 Data definition.docx)
 * <p>
 * See ExampleMicrogridSenderAgent for more documentation
 *
 * @author Jake Billings
 */
public class DummyTestScenarioC extends MicrogridSenderAgent {
    /**
     * makeReceiverAID()
     *
     * @return an AID pointing to the agent that will receive the data we send
     */
    private static AID makeReceiverAID() {
        return new AID("ReceiverAgent", AID.ISLOCALNAME);
    }


    //Nodes
    //Declared statically so that they can be accessed by the constructor and by the data sending behavior
    private static MicrogridNode g = new MicrogridNode("microgrid-node-test-c-g", MicrogridNodeType.GENERATOR);
    private static MicrogridNode gBreaker = new MicrogridNode("microgrid-node-test-c-g-breaker", MicrogridNodeType.CIRCUIT_BREAKER);
    private static MicrogridNode ourHub = new MicrogridNode("micrgrid-node-c-hub", MicrogridNodeType.HUB);

    /**
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
        nodes.add(gBreaker);
        nodes.add(ourHub);
        //Edges
        edges.add(new MicrogridEdge("microgrid-edge-test-c-gb", gBreaker, g, MicrogridEdgeType.BUS));
        edges.add(new MicrogridEdge("microgrid-edge-test-c-gbh", ourHub, gBreaker, MicrogridEdgeType.BUS));

        return new MicrogridGraph("microgrid-graph-subgraph-test-c", edges, nodes);
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
    public DummyTestScenarioC() {
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

        addBehaviour(new TickerBehaviour(this, 311) {
            @Override
            protected void onTick() {
                MicrogridNode node = getSubgraph().getNodes().iterator().next();

                sendDatum(
                        new FloatMicrogridDatum(
                                System.currentTimeMillis(),
                                g,
                                MicrogridFloatMeasurementType.VOLTAGE,
                                DummyPhysicalController.getVoltage()));
                sendDatum(
                        new FloatMicrogridDatum(
                                System.currentTimeMillis(),
                                g,
                                MicrogridFloatMeasurementType.AMPERAGE,
                                DummyPhysicalController.getAmperage()));
                sendDatum(
                        new FloatMicrogridDatum(
                                System.currentTimeMillis(),
                                g,
                                MicrogridFloatMeasurementType.WATTAGE,
                                DummyPhysicalController.getWattage()));
                sendDatum(
                        new BooleanMicrogridDatum(
                                System.currentTimeMillis(),
                                g,
                                MicrogridBooleanMeasurementType.FAULT,
                                DummyPhysicalController.isFault()));
                sendDatum(
                        new BooleanMicrogridDatum(
                                System.currentTimeMillis(),
                                g,
                                MicrogridBooleanMeasurementType.WARNING,
                                DummyPhysicalController.isWarning()));
                sendDatum(
                        new BooleanMicrogridDatum(
                                System.currentTimeMillis(),
                                gBreaker,
                                MicrogridBooleanMeasurementType.CIRCUIT_BREAKER_TRIPPED,
                                DummyPhysicalController.isCircuitBreakerOpen()));
            }
        });
    }

    /**
     * DummyPhysicalController
     * <p>
     * class
     * <p>
     * Dummy implementations of functions such as getVoltage()
     * <p>
     * this separates out the dummy measurement code from real, re-usable measurement sending code
     */
    private static class DummyPhysicalController {
        public static float getVoltage() {
            return 500L;
        }

        public static float getAmperage() {
            return (float) Math.sin(System.currentTimeMillis() * 6.283185 / 1000) * 10L;
        }

        public static float getWattage() {
            return getVoltage() * getAmperage();
        }

        public static boolean isFault() {
            return getWattage() > 0;
        }

        public static boolean isWarning() {
            return getWattage() < 0;
        }

        public static boolean isCircuitBreakerOpen() {
            return isFault();
        }
    }
}
