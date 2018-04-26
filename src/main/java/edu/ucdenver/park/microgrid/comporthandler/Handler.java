//copied directly from project "comporthandler" by Amine Sasse
// see package comporthandler2 for a modified version of this file that is easier to integrate with JADE agents
package edu.ucdenver.park.microgrid.comporthandler;

import java.util.concurrent.LinkedBlockingQueue;

public class Handler implements Runnable, BufferReadyEvent {

    Port port;
    LinkedBlockingQueue buffer;
    int data = 0;
    Packet packet;
    int delay;

    Handler(String comPort, boolean read, int i, int packetSize, int secs) {
        buffer = new LinkedBlockingQueue();
        port = new Port(comPort, buffer, read, packetSize);
        port.addListener(this);
        data = i;
        delay = secs * 1000;
    }

    //This runs the Thread and has the Handler simply send packets and sleep for a certain interval of time.
    public void run() {
        while (true) {
            if (port.canSend) {
                packet = new Packet((byte) 5, (short) 2, (short) 1, (short) data);
                port.sendPacket(packet.make());
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
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
            Packet input = (Packet) buffer.remove();
            if (input.header == 6) { //Acknowledgment packet
                port.setCan(false);
            } else if (input.header == 5) { //Enquiry packet
                packet = new Packet((byte) 6, input.from, input.to, input.data);
                port.sendPacket(packet.make());
                data = input.data;
                System.out.println("Message Recieved in " + port.port.getPortName() + ": " + data);
                data++;
                port.setCan(true);
            }
        }
    }
}