package edu.ucdenver.park.microgrid.socketioserver;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.corundumstudio.socketio.protocol.JsonSupport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.ucdenver.park.microgrid.data.abs.Edge;
import edu.ucdenver.park.microgrid.dummy.DummyMicrogrid;
import edu.ucdenver.park.microgrid.socketioserver.serializers.EdgeSerializer;

public class MicrogridSocketIOServer {

    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(3007);

        SimpleModule module = new SimpleModule("Microgrid", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Edge.class, new EdgeSerializer(Edge.class));
        JsonSupport jsonSupport = new JacksonJsonSupport(module);
        config.setJsonSupport(jsonSupport);

        final DummyMicrogrid grid = new DummyMicrogrid();

        final SocketIOServer server = new SocketIOServer(config);

        server.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient client) {
                client.sendEvent("grid", grid);
            }
        });

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                server.stop();
            }
        });
    }
}
