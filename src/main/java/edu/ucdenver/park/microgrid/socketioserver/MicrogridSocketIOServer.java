/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.socketioserver;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.corundumstudio.socketio.protocol.JsonSupport;
import edu.ucdenver.park.microgrid.data.MicrogridGraph;
import edu.ucdenver.park.microgrid.data.abs.Datum;
import edu.ucdenver.park.microgrid.live.DatumHandler;
import edu.ucdenver.park.microgrid.live.LiveMicrogridGraph;
import edu.ucdenver.park.microgrid.live.MicrogridGraphHandler;
import edu.ucdenver.park.microgrid.socketioserver.serializers.MicrogridJacksonModule;

/**
 * MicrogridSocketIOServer
 * <p>
 * class
 * <p>
 * Runs a Socket.io-compatible server that serves the information from a mircogrid to a WebSocket client
 * <p>
 * uses the netty-socketio library to implement the socket.io protocol
 * <p>
 * layers
 * Socket.io is a protocol that runs over either http or websocket that enables event-based client-server realtime communication
 * Socket.io is also a javascript library that implements this protocol. The JS library is used on the frontend. Since
 * this code is in Java, we use yet another library to implement the protocol. That library is netty-socketio, which
 * runs on top of netty. Netty is a library for event-based processing on servers.
 * Javascript uses a data format called JSON. It's easy to work with in javascript. It's kind of a pain in Java.
 * Jackson is a Java library for encoding/decoding objects to/from JSON. netty-socketio uses Jackson to encode/decode
 * objects before sending the to the javascript frontend via the socket.io protocol.
 *
 * @author Jake Billings
 */
public class MicrogridSocketIOServer implements MicrogridGraphHandler, DatumHandler {
    private final LiveMicrogridGraph grid;
    private final short port;
    private SocketIOServer server;

    public MicrogridSocketIOServer(LiveMicrogridGraph grid, short port) {
        this.grid = grid;
        this.port = port;
    }

    /**
     * init()
     * <p>
     * method
     * <p>
     * this is the function that actually sets up the service
     */
    public void init() {
        //---Netty-Socketio Configuration---
        //Netty-socketio uses the Configuration object to store settings
        //  for things like server port and hostname. We set these options here.
        Configuration config = new Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(this.getPort());

        //---Netty-Socketio Jackson Module Configuration---
        //Since the the default Jackson serializer doesn't work for all of our data objects,
        // we need to include a module containing our custom serializers
        JsonSupport jsonSupport = new JacksonJsonSupport(MicrogridJacksonModule.getInstance());
        config.setJsonSupport(jsonSupport);

        //---Netty-Socketio Instantiation---
        server = new SocketIOServer(config);

        //---Netty-Socketio Event Handlers---
        //Socket.io is an Event-Based protocol. To create the server, we register different event handlers.

        //Event: connect
        // whenever a client connects to the socket.io server, we send a "grid" event to the client
        // the "grid" event contains the grid graph data from this.grid (see MicrogridGraph)
        server.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient client) {
                client.sendEvent("grid", grid.getCurrentState());
            }
        });

        //---Netty-Socketio Start---
        server.start();

        //---Netty-Socketio Shutdown---
        //Register a shutdown hook to start free resources when killing the server
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.stop();
            }
        });
    }

    /**
     * onMicrogridGraph()
     * <p>
     * when we receive a new copy of the microgrid graph (from the receiver agent),
     * send the new graph to our clients
     *
     * @param graph a snapshot of the state of the LiveMicrogridGraph as a MicrogridGraph object
     */
    public void onMicrogridGraph(MicrogridGraph graph) {
        server.getBroadcastOperations().sendEvent("grid", graph);
    }

    /**
     * onDatum()
     * <p>
     * when we receive a new copy of a microgrid datum (from the receiver agent),
     * send the datum to our clients
     *
     * @param datum the datum received in the message
     */
    public void onDatum(Datum datum) {
        server.getBroadcastOperations().sendEvent("datum", datum);
    }

    //----Getters----
    // there are only getters; not setters because this object is intended to be nearly immutable
    public LiveMicrogridGraph getGrid() {
        return grid;
    }

    public short getPort() {
        return port;
    }
}
