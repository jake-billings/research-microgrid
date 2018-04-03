/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.socketioserver.serializers;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.ucdenver.park.microgrid.data.MicrogridEdge;
import edu.ucdenver.park.microgrid.data.abs.Edge;

/**
 * MicrogridJacksonModule
 *
 * class: singleton
 *
 * To customize how objects are converted from Java Object Instances to JSON, custom Serializers must be created (for
 *  instance, MicrogridEdgeSerializer). Serializers are stored in collections called Modules.
 *
 * This module contains all JacksonSerializers necessary to properly serialize all data objects from the Microgrid
 *  package.
 *
 * Serializers are instantiated and registered in the constructor
 */
public class MicrogridJacksonModule extends SimpleModule {
    private static MicrogridJacksonModule ourInstance = new MicrogridJacksonModule();

    public static MicrogridJacksonModule getInstance() {
        return ourInstance;
    }

    private MicrogridJacksonModule() {
        //--Module metadata---
        // name/version
        // we don't really care about these right now, so they're hard-coded here
        super ("Microgrid", new Version(1, 0, 0, null, null, null));

        //---Serializers---
        this.addSerializer(MicrogridEdge.class, new MicrogridEdgeSerializer(MicrogridEdge.class));
    }
}
