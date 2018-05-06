/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
/**
 * plot.js
 *
 * library: UMD module
 *  UMD: https://github.com/umdjs/umd/blob/master/templates/commonjsStrictGlobal.js
 *
 * the purpose of this file is to render a realtime plot of incoming measurement data using a graphics library
 *
 * (this is the fun part/the main part of the project)
 *
 * this is what actually draws the plot
 *
 * listens for the "nodeSnapshots" event
 *
 * see plot.css
 */
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['exports', 'client', 'SmoothieChart', 'TimeSeries'], function (exports, client, SmoothieChart, TimeSeries) {
            factory((root.plot = exports), client, SmoothieChart, TimeSeries);
        });
    } else if (typeof exports === 'object' && typeof exports.nodeName !== 'string') {
        // CommonJS
        factory(exports, require('client'), require('SmoothieChart'), require('TimeSeries'));
    } else {
        // Browser globals
        factory((root.plot = {}), root.client, root.SmoothieChart, root.TimeSeries);
    }
}(typeof self !== 'undefined' ? self : this, function (exports, client, SmoothieChart, TimeSeries) {
    //---Validate Dependencies/Config---
    if (!client) throw new Error('plot.js is missing client.js library; script tag for library must be included before script tag for plot.js');
    if (!SmoothieChart) throw new Error('plot.js is missing smoothie.js library; script tag for library must be included before script tag for plot.js');
    if (!TimeSeries) throw new Error('plot.js is missing smoothie.js library; script tag for library must be included before script tag for plot.js');

    /**
     * init()
     *
     * function
     *
     * initializes a SmoothiePlot from Smoothie.js that listens to the client websocket for measurements with
     * a given measurementId; draws this on the canvas "target"
     *
     * @param target a canvas object DOM object for Smoothie.js to draw on
     * @param measurementId a string measurement ID for us to filter through events for
     * @returns {destroyPlot} function; degresisters listeners, cleans up, and destroys this plot
     */
    exports.init = function (target, measurementId) {
        if (!target) throw new Error('argument \'target\' required to initialize a plot; must be a canvas object');
        if (typeof measurementId !== 'string') throw new Error('argument \'measurementId\' must be a string to initialize a plot');

        //Instatiate a SmoothieChart object in order to use Smoothie.js to draw the plot
        var smoothie = new SmoothieChart({
            maxValueScale: 1.5,
            minValueScale: 1.5,
            tooltip: true,
            timestampFormatter: SmoothieChart.timeFormatter
        });
        smoothie.streamTo(target, 1000);

        //Instantiate a TimeSeries object from Smoothie.js to push the data we receive into the chart
        var line = new TimeSeries();

        //Add the data set to the chart
        smoothie.addTimeSeries(line);

        //Register an event handler on the client websocket that pushes data from our target measurementId to the plot
        function updatePlot(nodeSnapshots) {
            nodeSnapshots.forEach(function (snapshot) {
                snapshot.measurements.forEach(function (measurement) {
                    if (measurement._id == measurementId) {
                        line.append(measurement.timestamp, measurement.value);
                    }
                });
            });
        }

        var listener = client.on('nodeSnapshots', updatePlot);

        return function destroyPlot() {
            client.removeListener('nodeSnapshots', updatePlot);
            smoothie.stop();
        };
    };
}));