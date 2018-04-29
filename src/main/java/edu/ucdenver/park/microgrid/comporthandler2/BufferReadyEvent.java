package edu.ucdenver.park.microgrid.comporthandler2;

/**
 * BufferReadyEvent
 *
 * interface
 *
 * event listener
 *
 * event handler interface for handling when the receive buffer contains enough data to read a packet
 *  modification from original: documentation added
 *
 * @author Amine Sasse
 * @author (modified by) Jake Billings
 */
public interface BufferReadyEvent {
    void BufferReady();
}
