package edu.ucdenver.park.microgrid.comporthandler2;

/**
 * ControllerDataListener
 *
 * interface
 *
 * event listener
 *
 * interface to handle when a data has successfully been read from a controller
 *  this interface was not present in the original comporthandler package.
 *  this interface allows outside classes (I.E. JADE agents) to register to receive data from the handler
 *
 * @author Jake Billings
 */
public interface ControllerDataListener {
    void onControllerData(int data);
}
