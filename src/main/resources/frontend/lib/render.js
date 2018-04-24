/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
/**
 * render.js
 *
 * library: UMD module
 *  UMD: https://github.com/umdjs/umd/blob/master/templates/commonjsStrictGlobal.js
 *
 * the purpose of this file is to render the data received in client.js via a graphics library
 *
 * depends on vis.js
 *
 * (this is the fun part/the main part of the project)
 *
 * this is what actually draws the map
 *
 * listens for the "grid" event
 *
 * see render.css
 */
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['exports', 'client', 'vis'], function (exports, io) {
            factory((root.render = exports), io);
        });
    } else if (typeof exports === 'object' && typeof exports.nodeName !== 'string') {
        // CommonJS
        factory(exports, require('client'), require('vis'));
    } else {
        // Browser globals
        factory((root.render = {}), root.client, root.vis);
    }
}(typeof self !== 'undefined' ? self : this, function (exports, client, vis) {
    //---Validate Dependencies/Config---
    if (!client) throw new Error('render.js is missing client.js library; script tag for library must be included before script tag for render.js');
    if (!vis) throw new Error('render.js is missing vis.js library; script tag for library must be included before script tag for render.js');

    function _imgForNodeType(nodeType) {
        switch (nodeType.toUpperCase()) {
            case 'BATTERY':
                return 'battery.png';
            case 'GENERATOR':
                return 'generator.png';
            case 'LOAD':
                return 'load.png';
            case 'CIRCUIT_BREAKER':
                return 'circuit_breaker_closed.png';
            case 'HUB':
                return 'hub.png';
            default:
                throw new Error('unknown node type: ' + nodeType);
        }
    }

    function _imgForMeasurement(measurement) {
        if (measurement.measurementType._id === -3) {
            if (measurement.value) {
                return 'img/circuit_breaker_open.png';
            } else {
                return 'img/circuit_breaker_closed.png';
            }
        }
    }


    function _levelForNode(node) {
        if (node._id.indexOf('central') >= 0) return 1;
        return 2;
    }

    /**
     * init()
     *
     * registers event handlers with client and with graphics library in order to draw the microgrid to the screen
     *
     * @param target the target element from the dom on which we should render
     */
    exports.init = function (target) {
        //Init empty data sets
        var nodes = new vis.DataSet();
        var edges = new vis.DataSet();

        // create a network
        var data = {
            nodes: nodes,
            edges: edges
        };
        var options = {
            //arrows are configured on an edge by edge basis in 'grid' and in 'nodeSnapshots'
        };
        var network = new vis.Network(target, data, options);

        //Event: grid
        // when we receive new grid graph data, check if it changed
        // if it did, redraw the grid with the new data
        client.on('grid', function (grid) {
            //---Check if the graph changed by comparing the new/old nodes/edges--
            var oldNodeIds = nodes.getIds();
            var oldEdgeIds = edges.getIds();

            var newNodeIds = grid.nodes.map(function (node) {
                return node._id;
            });
            var newEdgeIds = grid.edges.map(function (edge) {
                return edge._id;
            });

            oldNodeIds.sort();
            oldEdgeIds.sort();
            newNodeIds.sort();
            newEdgeIds.sort();

            var graphChanged = (JSON.stringify(newNodeIds) !== JSON.stringify(oldNodeIds)) || (JSON.stringify(newEdgeIds) !== JSON.stringify(oldEdgeIds));

            //---If the graph changed, update the graph data---
            // this will trigger a total redraw/loss of state
            if (graphChanged) {
                //clear out the old data
                nodes.clear();
                edges.clear();

                //update the grid data
                nodes.add(grid.nodes.map(function (node) {
                    return {
                        id: node._id,
                        image: 'img/' + _imgForNodeType(node.microgridNodeType),
                        shape: 'image'
                    }
                }));
                edges.add(grid.edges.map(function (edge) {
                    return {
                        id: edge._id,
                        from: edge.from,
                        to: edge.to,
                        arrows: {
                            to: {enabled: false}
                        } //edges initially have no direction; see "update arrow directions" in nodeSnapshots event
                    }
                }));
            }
        });

        //Event: nodeSnapshots
        // when we receive a node snapshot, update the node's label
        client.on('nodeSnapshots', function (nodeSnapshots) {
            if (nodes.length < 1) return;
            nodeSnapshots.forEach(function (snapshot) {
                var update = {
                    id: snapshot._id,
                    label: snapshot.measurements.reduce(function (label, measurement) {
                        //By a convention I invented, boolean measurement types will have negative _ids
                        // and floating point ones will have positive _ids
                        // if there are more measurement types or this convention is broken, we
                        // will need to update the logic here
                        // See MicrogridBooleanMeasurementType and MicrogridFloatMeasurementType
                        if (measurement.measurementType._id > 0) {
                            return label + measurement.value + ' ' + measurement.measurementType.unitName + '\n';
                        } else {
                            return label + (measurement.value ? measurement.measurementType.trueName : measurement.measurementType.falseName) + '\n';
                        }
                    }, '')
                };
                snapshot.measurements.forEach(function (measurement) {
                    var img = _imgForMeasurement(measurement);
                    if (img) {
                        update.image = img;
                    }
                });
                nodes.update(update);

                //---Update Arrow Directions Based on Current Flow---
                //By convention, edges should point away from nodes with positive current and towards
                // nodes with negative current
                // If an edge points away from a node, and the node has negative current, the edge is wrong; flip it
                // If an edge points towards a node, and the node has positive current, the edge is wrong; flip it
                edges.forEach(function (edge) {
                        snapshot.measurements.forEach(function (measurement) {
                            if (measurement.measurementType.baseUnitType === 'Current') {
                                if (measurement.value == 0) {
                                    edges.update({
                                        id: edge.id,
                                        arrows: {
                                              to: {enabled: false}
                                          }
                                    });
                                } else if (edge.from === snapshot._id) {
                                            if (measurement.value < 0) {
                                                edges.update({
                                                    id: edge.id,
                                                    to: edge.from,
                                                    from: edge.to,
                                                    arrows: {
                                                        to: {scaleFactor: 1, enabled: true}
                                                    }
                                                });
                                            }
                                } else if (edge.to === snapshot._id) {
                                    if (measurement.value > 0) {
                                        edges.update({
                                            id: edge.id,
                                            to: edge.from,
                                            from: edge.to,
                                            arrows: {
                                                  to: {scaleFactor: 1, enabled: true}
                                              }
                                        });
                                    }
                                }
                             }
                         });
                });
            });
        });
    };
}));