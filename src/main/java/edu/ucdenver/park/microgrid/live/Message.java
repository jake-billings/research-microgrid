package edu.ucdenver.park.microgrid.live;

import java.io.Serializable;

/**
 * Message
 *
 * abstract class
 *
 * Message is a superclass for all messages.
 *
 * Messages are objects that agents send us that contain information.
 *
 * For instance, the MicrogridFloatDatumMessage contains one float datum.
 * For instance, the MicrogridGraphMessage contains one datum.
 */
public class Message implements Serializable {
}
