/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.message;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    public static Message read(ObjectInputStream in) throws IOException, ClassNotFoundException {
        byte type = in.readByte();
        Message m;
        if (type == 1) {
            m = new MicrogridDatumMessage();
        } else if (type == 2) {
            m = new MicrogridGraphMessage();
        } else {
            throw new IOException("Invalid message type");
        }
        m.readExternal(in);
        return m;
    }
}
