package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    String typologyName;
    List<Node> nodeList = new ArrayList<>();

    public Graph(String typologyName) {
        this.typologyName = typologyName;
    }

    public Node addNode(String nodeName) {
        Node node = new Node(nodeName);
        nodeList.add(node);
        return node;
    }

    public void insertLinkBetweenTwoNodes(Node node1, Node node2, String fromInterface, String toInterface, int cost) {
        Link link = new Link(new PhysicalInterface(fromInterface), new PhysicalInterface(toInterface), cost);
        link.physicalInterface1.link = link;
        link.physicalInterface2.link = link;
        node1.physicalInterfaceSlots[node1.availableSlotIdx++] = link.physicalInterface1;
        node2.physicalInterfaceSlots[node2.availableSlotIdx++] = link.physicalInterface2;
    }
}
