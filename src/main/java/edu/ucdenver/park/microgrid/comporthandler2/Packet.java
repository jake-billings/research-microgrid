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
 */
public class Packet {
    byte header;
    short to;
    short from;
    short data;
    byte end;

    Packet(byte h, short t, short f, short d) {
        header = h;
        to = t;
        from = f;
        data = d;
        end = (byte) 4;
    }

    //Extracts each section of the packet (assuming the packet is 8 bytes total)
    //and construct packet out of received input for main to manage
    Packet(byte[] input) {
        ByteBuffer headerByte = ByteBuffer.wrap(input, 0, 1);
        ByteBuffer toByte = ByteBuffer.wrap(input, 1, 2);
        toByte.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer fromByte = ByteBuffer.wrap(input, 3, 2);
        fromByte.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer dataByte = ByteBuffer.wrap(input, 5, 2);
        dataByte.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer EOTByte = ByteBuffer.wrap(input, 7, 1);
        header = headerByte.get();
        to = toByte.getShort();
        from = fromByte.getShort();
        data = dataByte.getShort();
        end = EOTByte.get();
    }

    byte[] make() {
        byte[] packet = new byte[8];

        packet[0] = header;
        packet[1] = (byte) (to & 0xff);
        packet[2] = (byte) ((to >> 8) & 0xff);
        packet[3] = (byte) (from & 0xff);
        packet[4] = (byte) ((from >> 8) & 0xff);
        packet[5] = (byte) (data & 0xff);
        packet[6] = (byte) ((data >> 8) & 0xff);
        packet[7] = end;

        return packet;
    }
}