/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
/**
 * measurementDropdown.js
 *
 * library: UMD module
 *  UMD: https://github.com/umdjs/umd/blob/master/templates/commonjsStrictGlobal.js
 *
 * the purpose of this file is to create a dropdown for all available measurement ids
 */
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['exports', 'client'], function (exports, client) {
            factory((root.measurementDropdown = exports), client);
        });
    } else if (typeof exports === 'object' && typeof exports.nodeName !== 'string') {
        // CommonJS
        factory(exports, require('client'));
    } else {
        // Browser globals
        factory((root.measurementDropdown = {}), root.client, root.SmoothieChart, root.TimeSeries);
    }
}(typeof self !== 'undefined' ? self : this, function (exports, client) {
    //---Validate Dependencies/Config---
    if (!client) throw new Error('measurementDropdown.js is missing client.js library; script tag for library must be included before script tag for measurementDropdown.js');


    /**
     * init()
     *
     * deletes all children of target then appends a select dropdown
     *
     * updates select options on every nodeSnapshot that results in a change in measurement ids available
     *
     * @param target the target element to place the dropdown inside of
     */
    exports.init = function (target) {
        var measurements = [];
        var measurementIds = [];

        function createDropdown() {
            while (target.firstChild) {
                target.removeChild(target.firstChild);
            }
            var select = document.createElement('select');

            measurements.forEach(function (measurement) {
                var option = document.createElement('option');
                option.innerHTML = measurement._id + ' (' + measurement.measurementType.name + ')';
                option.setAttribute('value', measurement._id);
                select.appendChild(option);
            });

            target.appendChild(select);
        }

        client.on('nodeSnapshots', function (nodeSnapshots) {
            var measurementIdsFromMostRecentSnapshot = [];
            var measurementsFromMostRecentSnapshot = [];
            nodeSnapshots.forEach(function (nodeSnapshot) {
                nodeSnapshot.measurements.forEach(function (measurement) {
                    measurementsFromMostRecentSnapshot.push(measurement);
                    measurementIdsFromMostRecentSnapshot.push(measurement._id);
                });
            });
            measurementIdsFromMostRecentSnapshot = measurementIdsFromMostRecentSnapshot.sort();

            if (measurementIds.join(',') !== measurementIdsFromMostRecentSnapshot.join(',')) {
                measurements = measurementsFromMostRecentSnapshot;
                measurementIds = measurementIdsFromMostRecentSnapshot;

                createDropdown();
            }
        });
    };
}));