package org.example.model;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.util.*;

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
        try {
            node1.selector.wakeup();
            link.physicalInterface1.channel.register(node1.selector, SelectionKey.OP_READ);
            node2.selector.wakeup();
            link.physicalInterface2.channel.register(node2.selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            throw new RuntimeException(e);
        }
    }

    public String toMermaid() {
        Set<Link> linkSet = new HashSet<>();
        StringBuilder builder = new StringBuilder("```mermaid\n");
        builder.append("flowchart LR\n");
        nodeList.forEach(node -> {
            builder.append(String.format("%s[%s]%n", node.name, getNodeDesc(node)));
            for (int i = 0; i < node.availableSlotIdx; i++) {
                PhysicalInterface physicalInterface = node.physicalInterfaceSlots[i];
                Link link = physicalInterface.link;
                if (!linkSet.contains(link)) {
                    linkSet.add(link);
                    PhysicalInterface anotherInterface = physicalInterface.getAnotherInterface();
                    builder.append(String.format("%s o--o |\"%s\"|%s%n", node.name,
                            getInterfaceDesc(physicalInterface, anotherInterface),
                            anotherInterface.node.name));
                }
            }
        });
        builder.append("```");
        return builder.toString();
    }

    private String getInterfaceDesc(PhysicalInterface physicalInterface, PhysicalInterface anotherInterface) {
        String gap = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
        InterfaceNetworkProp prop = physicalInterface.interfaceNetworkProp;
        InterfaceNetworkProp anotherProp = anotherInterface.interfaceNetworkProp;
        return String.format("%s%s%s\\n%s%s%s\\n%s%s%s\\n", physicalInterface.name, gap, anotherInterface.name,
                Optional.ofNullable(prop).map(item -> item.ipAddress.getHostAddress() + "/" + item.mask).orElse(""), gap, Optional.ofNullable(anotherProp).map(item -> item.ipAddress.getHostAddress() + "/" + item.mask).orElse(""),
                Optional.ofNullable(prop).map(item -> item.macAddress).orElse(""), gap, Optional.ofNullable(anotherProp).map(item -> item.macAddress).orElse(""));
    }

    private String getNodeDesc(Node node) {
        return node.name + (Objects.nonNull(node.nodeNetworkProp) ? "\\n" + node.nodeNetworkProp.loopback.getHostAddress() : "");
    }
}
