package org.example.model;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;

public class Node {
    private static final int INTERFACES_PER_NODE = 10;

    String name;
    PhysicalInterface[] physicalInterfaceSlots = new PhysicalInterface[INTERFACES_PER_NODE];
    int availableSlotIdx = 0;
    NodeNetworkProp nodeNetworkProp;

    public Node(String name) {
        this.name = name;
    }

    public void setLoopbackAddress(String ipAddress) {
        nodeNetworkProp = new NodeNetworkProp();
        try {
            nodeNetworkProp.loopback = (Inet4Address) InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInterfaceNetworkProp(String name, String macAddress, String ipAddress, byte mask) {
        PhysicalInterface physicalInterface = Arrays.stream(physicalInterfaceSlots)
                .filter(Objects::nonNull).filter(item -> item.name.equals(name)).findFirst().orElse(null);
        if (Objects.isNull(physicalInterface)) {
            return;
        }
        physicalInterface.interfaceNetworkProp = new InterfaceNetworkProp();
        InterfaceNetworkProp interfaceNetworkProp = physicalInterface.interfaceNetworkProp;
        interfaceNetworkProp.macAddress = macAddress;
        try {
            interfaceNetworkProp.ipAddress = (Inet4Address) InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        interfaceNetworkProp.mask = mask;
    }

}
