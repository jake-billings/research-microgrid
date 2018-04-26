package edu.ucdenver.park.microgrid.comporthandler2;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Port
 *
 * class
 *
 * represents a serial port/bus and listens for events from it
 *
 * modified from original comporthandler package
 *  added class documentation similar to the rest of the repository
 *
 * The jssc "Java Simple Serial Connector" library was utilized to open up Ports, Send data and receive data.
 * It was written by scream3r and can be found here https://github.com/scream3r/java-simple-serial-connector
 * How to use:
 * Overall, this Port object should not be touched or modified.
 *
 * @author Amine Sasse
 * @author (modified by) Jake Billings
 */
public class Port implements SerialPortEventListener{
    SerialPort port;
    ArrayList<BufferReadyEvent> listenerList;
    LinkedBlockingQueue buffer;
    boolean canSend = false;
    int packetSize;

    //constructor for port object
    Port(String port1, LinkedBlockingQueue buf, boolean send, int i) {
        listenerList = new ArrayList<BufferReadyEvent>();
        openPort(port1);
        buffer = buf;
        canSend = send;
        packetSize = i;
    }

    public void setCan(boolean send) {
        canSend = send;
    }

    //add BufferReadyEvent listener to port object
    public void addListener(BufferReadyEvent ev) {
        listenerList.add(ev);
    }

    //sets up and opens a port.
    //Creates an EventListener to check when data is received through the RX
    private void openPort(String Port1) {
        port = new SerialPort(Port1);
        try {
            port.openPort();
            port.setParams(SerialPort.BAUDRATE_57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            port.addEventListener(this, SerialPort.MASK_RXCHAR);
            port.purgePort(SerialPort.PURGE_RXCLEAR);
            port.purgePort(SerialPort.PURGE_TXCLEAR);
        } catch (SerialPortException e) {
            System.out.println("A Serial Port was not found");
        }
    }

    //write Packet
    public void sendPacket(byte[] p) {
        try {
                port.writeBytes(p);
                port.purgePort(SerialPort.PURGE_TXCLEAR);
        } catch (SerialPortException exc) {
            exc.printStackTrace();
        }
    }

    //when the serial port receives data, this function is called and runs
    public void serialEvent(SerialPortEvent e) {
        if (e.isRXCHAR() && e.getEventValue() > 0) {
            try {
                try {
                    byte[] input = port.readBytes(packetSize);
                    buffer.add(new Packet(input));
                    } catch (SerialPortException spte) {
                        spte.printStackTrace();
                    }
                    for (BufferReadyEvent evnt : listenerList) {
                        evnt.BufferReady();
                    }
                port.purgePort(SerialPort.PURGE_RXCLEAR);
            } catch (SerialPortException ex) {
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
    }
}