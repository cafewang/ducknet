package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    @Test
    void buildGraph() {
        Graph graph = new Graph("test graph");
        Node r0 = graph.addNode("R0");
        Node r1 = graph.addNode("R1");
        Node r2 = graph.addNode("R2");

        graph.insertLinkBetweenTwoNodes(r0, r1, "0/0", "0/1", 1);
        graph.insertLinkBetweenTwoNodes(r1, r2, "0/2", "0/3", 1);
        graph.insertLinkBetweenTwoNodes(r0, r2, "0/4", "0/5", 1);
    }

}
