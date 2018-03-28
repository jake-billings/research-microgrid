package edu.ucdenver.park.microgrid.data.abs;

import org.junit.jupiter.api.Test;

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

        Graph<Edge,Node> g = new Graph<Edge, Node>("test", edges, nodes);

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

        Graph<Edge,Node> g = new Graph<Edge, Node>("test", edges, nodes);


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

        Graph<Edge,Node> g2 = new Graph<Edge, Node>("test2", edges, nodes);

        Graph<Edge,Node> u = g.union("union", g2);

        assert u.getNodes().size() == 6;
        assert u.getEdges().size() == 4;

        assert u.getNodes().contains(a);
        assert u.getNodes().contains(b2);
        assert g.getEdges().contains(ab);
        assert g.getEdges().contains(ba2);
    }
}