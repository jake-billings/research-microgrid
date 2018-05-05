/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.test.abs;

import edu.ucdenver.park.microgrid.data.abs.Edge;
import edu.ucdenver.park.microgrid.data.abs.Graph;
import edu.ucdenver.park.microgrid.data.abs.Node;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void shouldStoreIdUsingEmptyConstructor() {
        String _id = "test";
        Graph g = new Graph(_id);
        assert g.get_id().equals(_id);
    }

    @Test
    void shouldStoreEmptySetsUsingEmptyConstructor() {
        String _id = "test";
        Graph g = new Graph(_id);
        assert g.getEdges().size() == 0;
        assert g.getNodes().size() == 0;
    }

    @Test
    void shouldStoreNodesAndEdges() {
        Set<Node> nodes = new HashSet<Node>();
        Set<Edge> edges = new HashSet<Edge>();

        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);

        Edge<Node> ab = new Edge<Node>("a-b", a, b);
        Edge<Node> ba = new Edge<Node>("b-a", b, a);
        Edge<Node> ac = new Edge<Node>("a-c", a, c); //doesn't get added

        edges.add(ab);
        edges.add(ba);

        Graph<Edge, Node> g = new Graph<Edge, Node>("test", edges, nodes);

        assert g.getNodes().size() == 3;
        assert g.getEdges().size() == 2;

        assert g.getNodes().contains(a);
        assert g.getNodes().contains(b);
        assert g.getNodes().contains(c);
        assert g.getEdges().contains(ab);
        assert g.getEdges().contains(ba);
        assert !g.getEdges().contains(ac);
    }

    @Test
    void shouldPerformSetUnion() {
        Set<Node> nodes = new HashSet<Node>();
        Set<Edge> edges = new HashSet<Edge>();

        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);

        Edge<Node> ab = new Edge<Node>("a-b", a, b);
        Edge<Node> ba = new Edge<Node>("b-a", b, a);
        Edge<Node> ac = new Edge<Node>("a-c", a, c); //doesn't get added

        edges.add(ab);
        edges.add(ba);

        Graph<Edge, Node> g = new Graph<Edge, Node>("test", edges, nodes);


        Set<Node> nodes2 = new HashSet<Node>();
        Set<Edge> edges2 = new HashSet<Edge>();

        Node a2 = new Node("a2");
        Node b2 = new Node("b2");
        Node c2 = new Node("c2");
        nodes.add(a2);
        nodes.add(b2);
        nodes.add(c2);

        Edge<Node> ab2 = new Edge<Node>("a2-b2", a2, b2);
        Edge<Node> ba2 = new Edge<Node>("b2-a2", b2, a2);

        edges.add(ab2);
        edges.add(ba2);

        Graph<Edge, Node> g2 = new Graph<Edge, Node>("test2", edges, nodes);

        Graph<Edge, Node> u = g.union("union", g2);

        assert u.getNodes().size() == 6;
        assert u.getEdges().size() == 4;

        assert u.getNodes().contains(a);
        assert u.getNodes().contains(b2);
        assert g.getEdges().contains(ab);
        assert g.getEdges().contains(ba2);
    }

    @Test
    void shouldSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        //--Create the same sample from the earlier node/edge storing test--
        Set<Node> nodes = new HashSet<Node>();
        Set<Edge> edges = new HashSet<Edge>();

        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);

        Edge<Node> ab = new Edge<Node>("a-b", a, b);
        Edge<Node> ba = new Edge<Node>("b-a", b, a);
        Edge<Node> ac = new Edge<Node>("a-c", a, c); //doesn't get added

        edges.add(ab);
        edges.add(ba);

        Graph<Edge, Node> g = new Graph<Edge, Node>("test", edges, nodes);

        //Keep the assertion statements just to make sure node/edge storing isn't messing us up
        assert g.getNodes().size() == 3;
        assert g.getEdges().size() == 2;

        assert g.getNodes().contains(a);
        assert g.getNodes().contains(b);
        assert g.getNodes().contains(c);
        assert g.getEdges().contains(ab);
        assert g.getEdges().contains(ba);
        assert !g.getEdges().contains(ac);

        //Serialize the object to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(g);
        out.close();

        //Dump to a byte array and take note of the size
        byte[] rawBytes = baos.toByteArray();
        System.out.println("Size of graph is: " + rawBytes.length + " bytes");

        //Deserialize from the byte array
        ByteArrayInputStream bais = new ByteArrayInputStream(rawBytes);
        ObjectInputStream in = new ObjectInputStream(bais);
        Object o = in.readObject();
        assert o instanceof Graph;
        Graph<Edge, Node> g2 = (Graph<Edge, Node>) o;

        //Run the same assertions on the deserialized version
        assert g2.getNodes().size() == 3;
        assert g2.getEdges().size() == 2;

        assert g2.getNodes().contains(a);
        assert g2.getNodes().contains(b);
        assert g2.getNodes().contains(c);
        assert g2.getEdges().contains(ab);
        assert g2.getEdges().contains(ba);
        assert !g2.getEdges().contains(ac);

        //Check equality
        assert g.equals(g2);
        assert g2.equals(g);
    }
}