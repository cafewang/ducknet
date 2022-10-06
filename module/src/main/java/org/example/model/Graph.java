package org.example.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        link.physicalInterface1.node = node1;
        link.physicalInterface2.node = node2;
        node1.physicalInterfaceSlots[node1.availableSlotIdx++] = link.physicalInterface1;
        node2.physicalInterfaceSlots[node2.availableSlotIdx++] = link.physicalInterface2;
    }

    public String toMermaid() {
        Set<Link> linkSet = new HashSet<>();
        StringBuilder builder = new StringBuilder("```mermaid\n");
        builder.append("flowchart LR\n");
        nodeList.forEach(node -> {
            for (int i = 0; i < node.availableSlotIdx; i++) {
                PhysicalInterface physicalInterface = node.physicalInterfaceSlots[i];
                Link link = physicalInterface.link;
                if (!linkSet.contains(link)) {
                    linkSet.add(link);
                    PhysicalInterface anotherInterface = link.physicalInterface1 == physicalInterface ? link.physicalInterface2 : link.physicalInterface1;
                    builder.append(String.format("%s o--o |%s|%s%n", node.name,
                            physicalInterface.name + "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + anotherInterface.name,
                            anotherInterface.node.name));
                }
            }
        });
        builder.append("```");
        return builder.toString();
    }
}
