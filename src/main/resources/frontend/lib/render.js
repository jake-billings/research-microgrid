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
                return 'Battery.jpg';
            case 'GENERATOR':
                return 'Generator.png';
            default:
                throw new Error('unknown node type');
        }
    }

    /**
     * init()
     *
     * registers event handlers with client and with graphics library in order to draw the microgrid to the screen
     *
     * @param target the target element from the dom on which we should render
     */
    exports.init = function (target) {
        client.on('grid', function (grid) {// create an array with nodes
            var nodes = new vis.DataSet(grid.nodes.map(function (node) {
                return {
                    id: node._id,
                    label: node._id,
                    image: 'img/' + _imgForNodeType(node.microgridNodeType),
                    shape: 'image'
                }
            }));

            // create an array with edges
            var edges = new vis.DataSet(grid.edges.map(function (edge) {
                return {
                    from: edge.from,
                    to: edge.to,
                    label: edge._id
                }
            }));

            // create a network
            var data = {
                nodes: nodes,
                edges: edges
            };
            var options = {};
            var network = new vis.Network(target, data, options);
        });
    };
}));