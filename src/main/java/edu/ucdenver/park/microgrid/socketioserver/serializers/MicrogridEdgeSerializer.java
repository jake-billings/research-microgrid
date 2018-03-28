package edu.ucdenver.park.microgrid.socketioserver.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.ucdenver.park.microgrid.data.MicrogridEdge;
import edu.ucdenver.park.microgrid.data.abs.Edge;

import java.io.IOException;

/**
 * MicrogridEdgeSerializer
 *
 * class: serializer
 *
 * This class implements StdSerializer specifically for the Edge object. The default serializer didn't work because
 *  each edge needs information from the to/from nodes. The default serializer did not provide this information, so
 *  this class does.
 *
 * This serializer is part of the Microgrid Jackson module and used when sending edges to the client while serializing
 *  graphs in the MicrogridSocketIOServer
 *
 * See Edge, MicrogridSocketIOServer
 * See Jackson Serializers/Modules
 * See JacksonJsonSupport
 */
public class MicrogridEdgeSerializer extends StdSerializer<MicrogridEdge> {
    MicrogridEdgeSerializer(Class<MicrogridEdge> t) {
        super(t);
    }

    public void serialize(MicrogridEdge edge, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("_id", edge.get_id());
        jsonGenerator.writeStringField("to", edge.getTo().get_id());
        jsonGenerator.writeStringField("from", edge.getFrom().get_id());
        jsonGenerator.writeStringField("edgeType", edge.getMicrogridEdgeType().getName());
        jsonGenerator.writeEndObject();
    }
}
