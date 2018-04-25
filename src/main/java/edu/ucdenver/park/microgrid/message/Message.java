/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.message;

import java.io.Externalizable;
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
 *
 * @author Jake Billings
 */
public abstract class Message implements Externalizable {
}
