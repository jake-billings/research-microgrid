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

    function _levelForNode(node) {
        if (node._id.indexOf('central')>=0) return 1;
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
                        arrows: 'to'
                    }
                }));
            }
        });

        //Event: nodeSnapshots
        // when we receive a node snapshot, update the node's label
        client.on('nodeSnapshots', function (nodeSnapshots) {
            nodeSnapshots.forEach(function (snapshot) {
                nodes.update({
                    id: snapshot._id,
                    label: snapshot.measurements.reduce(function (label, measurement) {
                        return label + +measurement.value + ' ' + measurement.measurementType.unitName + '\n';
                    },'')
                });
            });
        });
    };
}));