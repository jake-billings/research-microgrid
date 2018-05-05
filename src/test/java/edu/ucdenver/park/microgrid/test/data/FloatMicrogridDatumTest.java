package edu.ucdenver.park.microgrid.test.data;

import edu.ucdenver.park.microgrid.data.FloatMicrogridDatum;
import edu.ucdenver.park.microgrid.data.MicrogridFloatMeasurementType;
import edu.ucdenver.park.microgrid.data.MicrogridNode;
import edu.ucdenver.park.microgrid.data.MicrogridNodeType;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.Arrays;

class FloatMicrogridDatumTest {
    private static MicrogridNode g = new MicrogridNode("microgrid-node-a-g", MicrogridNodeType.GENERATOR);

    @Test
    void shouldSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        long now = System.currentTimeMillis();
        float value = (float) 3.149159;

        FloatMicrogridDatum datum = new FloatMicrogridDatum(
                now,
                g,
                MicrogridFloatMeasurementType.VOLTAGE,
                value);

        assert datum.getTimestamp() == now;
        assert datum.getNode().equals(g);
        assert datum.getMeasurementType().equals(MicrogridFloatMeasurementType.VOLTAGE);
        assert datum.getValue() == value;


        //Serialize the object to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        datum.writeExternal(out);
        out.close();

        byte[] rawBytes = baos.toByteArray();
        System.out.println("Size of floatmicrogriddatum is: " + rawBytes.length + " bytes");
        System.out.write(rawBytes);
        System.out.println();

        //Deserialize from the byte array
        ByteArrayInputStream bais = new ByteArrayInputStream(rawBytes);
        ObjectInputStream in = new ObjectInputStream(bais);

        FloatMicrogridDatum datum2 = new FloatMicrogridDatum();
        datum2.readExternal(in);

        assert datum2.getTimestamp() == now;
        assert datum2.getNode().equals(g);
        assert datum2.getMeasurementType().equals(MicrogridFloatMeasurementType.VOLTAGE);
        assert datum2.getValue() == value;
    }

}