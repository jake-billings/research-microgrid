package edu.ucdenver.park.microgrid.agents.controller;

import edu.ucdenver.park.microgrid.agents.core.MicrogridSenderAgent;
import edu.ucdenver.park.microgrid.comporthandler2.ControllerDataListener;
import edu.ucdenver.park.microgrid.comporthandler2.Handler2;
import edu.ucdenver.park.microgrid.data.*;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import java.util.HashSet;
import java.util.Set;

public class MicrogridControllerSenderAgent extends MicrogridSenderAgent {
    static int data;
    private Handler2 handler;

    private static AID makeReceiverAID() {
        AID aid = new AID("ReceiverAgent@10.20.102.203:1100/JADE", AID.ISGUID);
        aid.addAddresses("http://NC2611-PC-16.ucdenver.pvt:7778/acc");
        return aid;
    }

    private static MicrogridGraph makeMicrogridGraph() {
        //Declare two hash sets to hold our nodes and edges
        Set<MicrogridNode> dummyNodes = new HashSet<MicrogridNode>();
        Set<MicrogridEdge> dummyEdges = new HashSet<MicrogridEdge>();

        //Neighborhood One
        MicrogridNode n1Generator = new MicrogridNode("microgrid-node-n1-generator", MicrogridNodeType.GENERATOR);
        dummyNodes.add(n1Generator);
        MicrogridNode n1Battery = new MicrogridNode("microgrid-node-n1-battery", MicrogridNodeType.BATTERY);
        dummyNodes.add(n1Battery);
        dummyEdges.add(new MicrogridEdge("microgrid-edge-n1-gb", n1Generator, n1Battery, MicrogridEdgeType.BUS));

        //Neighborhood Two
        MicrogridNode n2Generator = new MicrogridNode("microgrid-node-n2-generator", MicrogridNodeType.GENERATOR);
        dummyNodes.add(n2Generator);
        MicrogridNode n2Battery = new MicrogridNode("microgrid-node-n2-battery", MicrogridNodeType.BATTERY);
        dummyNodes.add(n2Battery);
        dummyEdges.add(new MicrogridEdge("microgrid-edge-n2-gb", n2Generator, n2Battery, MicrogridEdgeType.BUS));

        //Connect the neighborhood
        dummyEdges.add(new MicrogridEdge("microgrid-edge-inter-n1-n2", n1Battery, n2Battery, MicrogridEdgeType.BUS));

        return new MicrogridGraph("example-microgrid-sender-subgraph", dummyEdges, dummyNodes);
    }

    private static long makeGridUpdatePeriod() {
        return 30000;
    }

    public MicrogridControllerSenderAgent() {

        //Call super with the parameters from above
        super(makeReceiverAID(), makeMicrogridGraph(), makeGridUpdatePeriod());

        handler = new Handler2("COM1", true, data, 8, 3);
    }

    protected void setup() {
        super.setup();

        addBehaviour(new TickerBehaviour(this, 2000) {
            @Override
            protected void onTick() {
                if (handler.canSend()) {
                    handler.sendDataRequestPacket();
                }
            }
        });

        handler.registerControllerDataListener(new ControllerDataListener() {
            public void onControllerData(int data) {
                System.out.println("data: "+data);

                //Don't ever use this method to get nodes in a real agent. Store the important nodes
                // as instance variables. This is acceptable because this is a dummy and I used DummyMicrogridGraph
                MicrogridNode node = getSubgraph().getNodes().iterator().next();


                //This is an old of how to properly send a measurement to the server assuming you have a node
                sendDatum(
                        //Create a FloatMicrogridDatum object to hold the measurement
                        new FloatMicrogridDatum(
                                //Timestamp the datum with the current number of milliseconds from Jan 1st, 1970
                                System.currentTimeMillis(),
                                //Tell the Datum which node measured it
                                node,
                                //Pretend we're measuring voltage
                                MicrogridFloatMeasurementType.VOLTAGE,
                                //Provide a dummy value for the measurement
                                (float) data));
            }
        });
    }
}
