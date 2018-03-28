package edu.ucdenver.park.microgrid.launchers;

import edu.ucdenver.park.microgrid.dummy.DummyMicrogrid;
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
        MicrogridSocketIOServer server = new MicrogridSocketIOServer(new DummyMicrogrid(), (short) 3006);
        server.init();
    }
}
