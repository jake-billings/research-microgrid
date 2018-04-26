package edu.ucdenver.park.microgrid.comporthandler2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Packet
 *
 * class
 *
 * represents one packet sent from the hardware controller, read via serial, and received by this application
 *  one packet contains one data point
 *
 * modified from original comporthandler package
 *  added class documentation similar to the rest of the repositoryHow to use.
 *
 * This class was made specifically to create and extract info from packets.
 * It was also made specifically with the idea that the packets would be 8 bytes and contain the following:
 *
 * ENQ(1 byte) TO(2 bytes) FROM(2 bytes) Number(2 byte, 0-FF) EOT(1 byte)
 *
 * In order to accept different packets you will need to modify this class and modify the handling of the packet
 * in the BufferReady() function in the Handler Class.
 *
 * @author Amine Sasse
 * @author (modified by) Jake Billings
 * @author (modified by) Bhanu Babaiahgari
 */
public class Packet {
    byte header;
    byte from;
    byte id;
    byte type;
    short data;
    byte end;

    Packet(byte h, byte f, byte i, byte t, short d) {
        header = h;
        from = f;
        id = i;
        type = t;
        data = d;
        end = (char) 59;
    }

    //Extracts each section of the packet (assuming the packet is 8 bytes total)
    //and construct packet out of received input for main to manage
    Packet(byte[] input) {
        ByteBuffer headerByte = ByteBuffer.wrap(input, 0, 1);
        ByteBuffer fromByte = ByteBuffer.wrap(input, 1, 1);
        fromByte.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer idByte = ByteBuffer.wrap(input, 2, 1);
        idByte.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer typeByte = ByteBuffer.wrap(input, 3, 1);
        typeByte.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer dataByte = ByteBuffer.wrap(input, 4, 2);
        dataByte.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer EOTByte = ByteBuffer.wrap(input, 6, 1);
        header = headerByte.get();
        from = fromByte.get();
        id = idByte.get();
        type = typeByte.get();
        data = dataByte.getShort();
        end = EOTByte.get();
    }

    byte[] make() {
        byte[] packet = new byte[7];

        packet[0] = header;
        packet[1] = (byte) (from & 0xff);
        packet[2] = (byte) (id & 0xff);
        packet[3] = (byte) (type & 0xff);
        packet[4] = (byte) ((data >> 8) & 0xff);
        packet[5] = (byte) (data & 0xff);
        packet[6] = end;

        return packet;
    }
}