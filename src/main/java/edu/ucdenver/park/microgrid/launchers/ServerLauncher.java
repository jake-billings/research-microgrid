package edu.ucdenver.park.microgrid.launchers;

import edu.ucdenver.park.microgrid.dummy.DummyMicrogrid;
import edu.ucdenver.park.microgrid.live.LiveMicrogridGraph;
import edu.ucdenver.park.microgrid.live.MicrogridGraphMessage;
import edu.ucdenver.park.microgrid.socketioserver.MicrogridSocketIOServer;

/**
 * ServerLauncher
 *
 * class: entry point
 *
 * can be used as a main class
 *
 * calling main() instantiates and starts a socketioserver
 */
public class ServerLauncher {
    public static void main(String[] args) {
        LiveMicrogridGraph live = new LiveMicrogridGraph();
        MicrogridSocketIOServer server = new MicrogridSocketIOServer(live, (short) 3006);
        server.init();
        live.receiveMessage(new MicrogridGraphMessage(new DummyMicrogrid(), System.currentTimeMillis()+60000));
    }
}
