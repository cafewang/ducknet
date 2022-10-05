package org.example.model;

public class Node {
    private static final int INTERFACES_PER_NODE = 10;

    String name;
    PhysicalInterface[] physicalInterfaceSlots = new PhysicalInterface[INTERFACES_PER_NODE];
    int availableSlotIdx = 0;

    public Node(String name) {
        this.name = name;
    }
}
