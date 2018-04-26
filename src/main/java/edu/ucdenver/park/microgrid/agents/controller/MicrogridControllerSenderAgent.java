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

    //Neighborhood One
    private static MicrogridNode g = new MicrogridNode("microgrid-node-controller-generator", MicrogridNodeType.GENERATOR);

    private static MicrogridGraph makeMicrogridGraph() {
        //Declare two hash sets to hold our nodes and edges
        Set<MicrogridNode> dummyNodes = new HashSet<MicrogridNode>();
        Set<MicrogridEdge> dummyEdges = new HashSet<MicrogridEdge>();

        dummyNodes.add(g);

        return new MicrogridGraph("microgrid-controller", dummyEdges, dummyNodes);
    }

    private static long makeGridUpdatePeriod() {
        return 30000;
    }

    public MicrogridControllerSenderAgent() {

        //Call super with the parameters from above
        super(makeReceiverAID(), makeMicrogridGraph(), makeGridUpdatePeriod());

        handler = new Handler2("COM3", true, data, 8, 3);
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
