package org.example.model;

import lombok.Getter;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;

@Getter
public class PhysicalInterface {
    DatagramChannel channel;
    DatagramSocket socket;
    public PhysicalInterface(String name) {
        this.name = name;
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        socket = channel.socket();
        try {
            socket.bind(new InetSocketAddress("127.0.0.1", 0));
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    String name;
    Link link;
    Node node;
    InterfaceNetworkProp interfaceNetworkProp;

    public PhysicalInterface getAnotherInterface() {
        return link.physicalInterface1 == this ? link.physicalInterface2 : link.physicalInterface1;
    }
}
