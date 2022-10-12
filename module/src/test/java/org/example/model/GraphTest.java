package org.example.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import static org.example.utils.NetUtil.generateRandomMacAddress;

class GraphTest {
    @Test
    void buildGraph() {
        Graph graph = new Graph("test graph");
        Node r0 = graph.addNode("R0");
        r0.setLoopbackAddress("122.1.1.1");
        Node r1 = graph.addNode("R1");
        r1.setLoopbackAddress("122.1.1.2");
        Node r2 = graph.addNode("R2");
        r2.setLoopbackAddress("122.1.1.3");

        graph.insertLinkBetweenTwoNodes(r0, r1, "eth0/0", "eth0/1", 1);
        graph.insertLinkBetweenTwoNodes(r1, r2, "eth0/2", "eth0/3", 1);
        graph.insertLinkBetweenTwoNodes(r0, r2, "eth0/4", "eth0/5", 1);
        r0.setInterfaceNetworkProp("eth0/0", generateRandomMacAddress(), "20.11.1.1", (byte)24);
        r0.setInterfaceNetworkProp("eth0/4", generateRandomMacAddress(), "40.11.1.1", (byte)24);
        r1.setInterfaceNetworkProp("eth0/1", generateRandomMacAddress(), "20.11.1.2", (byte)24);
        r1.setInterfaceNetworkProp("eth0/2", generateRandomMacAddress(), "30.11.1.1", (byte)24);
        r2.setInterfaceNetworkProp("eth0/3", generateRandomMacAddress(), "30.11.1.2", (byte)24);
        r2.setInterfaceNetworkProp("eth0/5", generateRandomMacAddress(), "40.11.1.2", (byte)24);
        System.out.println(graph.toMermaid());

        r0.sendThrough("eth0/0", "是的冯绍峰".getBytes());
        Assertions.assertEquals(3, graph.nodeList.size());
    }

}
