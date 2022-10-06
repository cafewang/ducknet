package org.example.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GraphTest {
    @Test
    void buildGraph() {
        Graph graph = new Graph("test graph");
        Node r0 = graph.addNode("R0");
        Node r1 = graph.addNode("R1");
        Node r2 = graph.addNode("R2");

        graph.insertLinkBetweenTwoNodes(r0, r1, "eth0/0", "eth0/1", 1);
        graph.insertLinkBetweenTwoNodes(r1, r2, "eth0/2", "eth0/3", 1);
        graph.insertLinkBetweenTwoNodes(r0, r2, "eth0/4", "eth0/5", 1);
        System.out.println(graph.toMermaid());
        Assertions.assertEquals(3, graph.nodeList.size());
    }

}
