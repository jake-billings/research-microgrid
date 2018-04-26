package edu.ucdenver.park.microgrid.comporthandler2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handler2
 *
 * class
 *
 * the class manages listening for data from the communication port/serial bus, parsing packets received, and
 *  passing the received data on to registered listeners
 *
 * modified from the original comporthandler package
 *  modification includes formatting, access modifiers, and the addition of an event handler/listener registration for
 *  when data is received
 *
 * @author Amine Sasse
 * @author (modified by) Jake Billings
 * @author (modified by) Bhanu Babaiahgari
 */
public class Handler2 implements BufferReadyEvent {
    private Set<ControllerDataListener> dataListeners = new HashSet<ControllerDataListener>();

    Port port;
    LinkedBlockingQueue buffer;
    int data = 0;
    Packet packet;
    int delay;

    public Handler2(String comPort, boolean read, int i, int packetSize, int secs) {
        buffer = new LinkedBlockingQueue();
        port = new Port(comPort, buffer, read, packetSize);
        port.addListener(this);
        data = i;
        delay = secs * 1000;
    }

    /**
     * canSend()
     *
     * method
     *
     * returns "port.canSend"
     *
     * @return true if the port allows us to send a packet
     */
    public boolean canSend() {
        return port.canSend;
    }

    /**
     * sendDataRequestPacket()
     *
     * method
     *
     * sends a packet via the comm port requesting for the controller to send us more data
     */
    public void sendDataRequestPacket() {
        packet = new Packet((byte) 5, (byte) 2, (byte) 0, (byte) 1, (short) data);
        port.sendPacket(packet.make());
    }

    private void fireControllerDataEvent(int data) {
        Iterator<ControllerDataListener> iter = dataListeners.iterator();
        while (iter.hasNext()) {
            iter.next().onControllerData(data);
        }
    }

    /**
     * registerControllerDataListener()\
     *
     * method
     *
     * adds a listen to our list of registered listeners; this listen will now receive calls when we get data
     *
     * @param listener
     */
    public void registerControllerDataListener(ControllerDataListener listener) {
        this.dataListeners.add(listener);
    }

    /* This is the function that handles what the software does with the information extracted from the received packet.
       It first takes the Packet from the buffer and extracts the information from it, creating a new Packet object.
       If the header variable of the Packet = 6, then the packet is an acknowledgment packet and the canSend boolean is set to false, which means the Handler stops sending packets.
       If the header variable of the Packet = 5, then the packet is an enquiry packet and the Handler does the following:
            1. It begins by creating an acknowledgment packet and sending it to inform the other PC that it received the packet.
            2. It takes the data from the Packet and prints out what the data was, in this case it would be a number.
            3. It increments the data.
            4. It then sets the canSend boolean to true, which means the Handler will begin to create new packets (with the newly incremented data) and sending these new packets.
    */
    public void BufferReady() {
        if (!buffer.isEmpty()) {
            System.out.println("Handler got data");
            Packet input = (Packet) buffer.remove();
            if (input.header == 6) { //Acknowledgment packet
                port.setCan(false);
            } else if (input.header == 5) { //Enquiry packet
                packet = new Packet(input.header, input.from, input.id, input.type, input.data);
                port.sendPacket(packet.make());
                data = input.data;
                this.fireControllerDataEvent(data);
                System.out.println("Message Recieved in " + port.port.getPortName() + ": " + data);
                data++;
                port.setCan(true);
            }
        }
    }
}