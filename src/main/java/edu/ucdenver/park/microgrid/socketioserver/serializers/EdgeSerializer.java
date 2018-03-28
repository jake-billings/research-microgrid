package edu.ucdenver.park.microgrid.socketioserver.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.ucdenver.park.microgrid.data.abs.Edge;

import java.io.IOException;

public class EdgeSerializer extends StdSerializer<Edge> {
    public EdgeSerializer(Class<Edge> t) {
        super(t);
    }

    public void serialize(Edge edge, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("_id", edge.get_id());
        jsonGenerator.writeStringField("to", edge.getTo().get_id());
        jsonGenerator.writeStringField("from", edge.getFrom().get_id());
        jsonGenerator.writeEndObject();
    }
}
