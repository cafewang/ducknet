package org.example.model;

public class Link {
    public Link(PhysicalInterface physicalInterface1, PhysicalInterface physicalInterface2, int cost) {
        this.physicalInterface1 = physicalInterface1;
        this.physicalInterface2 = physicalInterface2;
        this.cost = cost;
    }

    PhysicalInterface physicalInterface1;
    PhysicalInterface physicalInterface2;

    int cost;
}
