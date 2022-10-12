package org.example.model;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Node {
    private static final int INTERFACES_PER_NODE = 10;

    String name;
    PhysicalInterface[] physicalInterfaceSlots = new PhysicalInterface[INTERFACES_PER_NODE];
    int availableSlotIdx = 0;
    NodeNetworkProp nodeNetworkProp;
    Selector selector;
    ThreadPoolExecutor executor = new ThreadPoolExecutor(INTERFACES_PER_NODE, INTERFACES_PER_NODE, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

    public Node(String name) {
        this.name = name;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        executor.execute(startListening());
    }

    private Runnable startListening() {
        return () -> {
            ByteBuffer buffer = ByteBuffer.allocate(65535);
            while (true) {
                try {
                    while (selector.select() > 0) {
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        while (iterator.hasNext()) {
                            SelectionKey selectionKey = iterator.next();
                            if (selectionKey.isReadable()) {
                                iterator.remove();
                                DatagramChannel channel = (DatagramChannel) selectionKey.channel();
                                channel.receive(buffer);
                                buffer.flip();
                                System.out.printf("%s receiving through %s: %s%n", name, getInterfaceByChannel(channel).name, new String(buffer.array(), 0, buffer.limit()));
                                buffer.clear();
                            }
                        }
                    }
                    Thread.sleep(1);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private PhysicalInterface getInterfaceByChannel(DatagramChannel channel) {
        return getInterfaces().stream().filter(physicalInterface -> physicalInterface.channel == channel).findFirst().orElse(null);
    }

    public void sendThrough(String interfaceName, byte[] data) {
        getInterfaces().stream().filter(physicalInterface -> physicalInterface.name.equals(interfaceName)).findFirst()
                .ifPresent(physicalInterface -> {
                    final ByteBuffer buffer = ByteBuffer.allocate(65535);
                    buffer.put(data);
                    buffer.flip();
                    PhysicalInterface anotherInterface = physicalInterface.getAnotherInterface();
                    try {
                        physicalInterface.channel.send(buffer, new InetSocketAddress("127.0.0.1", anotherInterface.socket.getLocalPort()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    buffer.clear();
                });
    }

    public List<PhysicalInterface> getInterfaces() {
        return Arrays.stream(physicalInterfaceSlots).limit(availableSlotIdx).collect(Collectors.toList());
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
