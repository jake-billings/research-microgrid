package edu.ucdenver.park.microgrid.comporthandler;

import jssc.SerialPortList;

public class Driver {
    static int data;

    //List and print the available COM ports.
    private static void listPorts(){
        String[] list = SerialPortList.getPortNames();

        for(String comPort : list){
            System.out.println(comPort);
        }
    }

    // The job of this driver class is to simply create new threads that contain Handlers to the ports you wish to receive and send data through.
    public static void main(String[] args) {
        listPorts();
        new Thread(new Handler("COM1", true, data, 8, 3)).start();
        new Thread(new Handler("COM3", false, data, 8, 3)).start();
    }

    /*
    How to use:

    listPorts() simply lists the available ports you can use. If you do not want this in your code, simply omit it from the main function.

    To create a new Connection between ports:
        new Thread(new Handler(name of COM port, handler will start by writing(true) or listening(false), data, size of packet (in bytes), seconds of delay between writes)).start();

    Here is an example to utilize
         new Thread(new Handler("COM1", false, data, 8, 3)).start();


    Your final main function should look something like this:

    public static void main(String[] args) {
        listPorts();
        new Thread(new Handler("COM1", false, data, 8, 3)).start();
        new Thread(new Handler("COM7", true, data, 8, 3)).start();
    }

    I left my test implementation in the main loop.

    */

}
