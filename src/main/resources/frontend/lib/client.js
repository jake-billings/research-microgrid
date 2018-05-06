/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
/**
 * client.js
 *
 * library: UMD module
 *  UMD: https://github.com/umdjs/umd/blob/master/templates/commonjsStrictGlobal.js
 *
 * the purpose of this file is to wrap calls to socket.io
 *  it basically just validates that everything is configured properly before attempting to connect to the backend
 *
 * users of this library shouldn't have to know socket.io exists
 * users of this library shouldn't have to know the backend exists
 *
 * all users get are appropriate callbacks when data becomes available
 *
 * depends on the socket.io-client, which is an npm module. run "yarn install" in the frontend directory
 *  to download this library.
 * the script tag for the library must be included before the script tag for this file
 *
 * depends on a "config" object
 *  a script tag must declare this object before the script tag for client.js
 *
 * Data flows as follows
 *  1. Agent polls instrument and makes measurement
 *  2. Agent pushes data throuh Jade to the microgrid data backend
 *  3. Microgrid data backend pushes to this client via netty-socketio
 *  4. This client calls callbacks in its event handlers
 */
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['exports', 'io', 'config'], function (exports, io) {
            factory((root.client = exports), io);
        });
    } else if (typeof exports === 'object' && typeof exports.nodeName !== 'string') {
        // CommonJS
        factory(exports, require('io'), require('config'));
    } else {
        // Browser globals
        factory((root.client = {}), root.io, root.config);
    }
}(typeof self !== 'undefined' ? self : this, function (exports, io, config) {
    //---Validate Dependencies/Config---
    if (!io) throw new Error('client.js is missing socket.io-client library; script tag for library must be included before script tag for client.js');
    if (!io.connect) throw new Error('client.js found \'io\' on the window; however, it doesn\'t look quite right. it\'s missing a function we need. Maybe it has been polluted in the global namespace.');
    if (!config) throw new Error('client.js: missing config object; config object must be declared in script tab before the client.js script tag.');
    if (typeof config.backendUrl !== 'string') throw new Error('client.js: malformed config object: config.backendUrl must be a string');

    //---Configure the Socket.io Client---
    var socket = io.connect(config.backendUrl);

    //An array of all of the events that users of this library are allowed to register
    var _validEvents = [];

    //Event: connect
    // logs a message to the console when we connect to the server
    socket.on('connect', function(data) {
        console.info('Client has connected to the server!',data);
    });
    _validEvents.push('connect');

    //Event: grid
    // logs a message to the console when we receive grid graph data
    socket.on('grid', function(data) {
        console.info('grid', data);
    });
    _validEvents.push('grid');

    //Event: nodeSnapshot
    // logs a message to the console when we receive grid graph data
    socket.on('nodeSnapshots', function(data) {
        console.info('nodeSnapshots', data);
    });
    _validEvents.push('nodeSnapshots');

    //Event: datum
    // logs a message to the console when we receive grid graph data
    socket.on('datum', function(data) {
        console.info('datum', data);
    });
    _validEvents.push('datum');

    /**
     * on()
     *
     * register an event handler with the socket client
     *
     * this function verifies that you are registering a proper event with a proper callback then
     *  passes the call on to the socket object
     *
     * @param eventName the name of the event to register must be in _validEvents
     * @param callback a function to call in which the first param is the event data
     */
    exports.on = function (eventName, callback) {
        //validate parameters
        if (typeof eventName !== 'string') throw new Error('client.js: call to on() requires a string eventName');
        if (_validEvents.indexOf(eventName) < 0) throw new Error('client.js: call to on() contains an invalid event; no event with that name exists');
        if (typeof callback !== 'function') throw new Error('client.js: call to on() requires a callback function');

        //register event listener
        socket.on(eventName, callback);
    };

    /**
     * removeListener()
     *
     * deregister an event handler with the socket client
     *
     * @param eventName the name of the event to deregister must be in _validEvents
     * @param callback a function to call in which the first param is the event data
     */
    exports.removeListener = function (eventName, callback) {
        //validate parameters
        if (typeof eventName !== 'string') throw new Error('client.js: call to removeListener() requires a string eventName');
        if (_validEvents.indexOf(eventName) < 0) throw new Error('client.js: call to removeListener() contains an invalid event; no event with that name exists');
        if (typeof callback !== 'function') throw new Error('client.js: call to removeListener() requires a callback function');

        //deregister event listener
        socket.removeListener(eventName, callback);
    };
}));