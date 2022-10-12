package org.example.utils;

import org.example.model.InterfaceNetworkProp;
import org.example.model.Node;
import org.example.model.PhysicalInterface;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public abstract class NetUtil {
    private static final Random RANDOM = new Random();

    public static String generateRandomMacAddress() {
        Long longValue = RANDOM.nextLong();
        String hexString = Long.toHexString(longValue).toUpperCase();
        return splitAndConcat(hexString);
    }

    private static String splitAndConcat(String str) {
        if (str.length() > 2) {
            return str.substring(0, 2) + ":" + splitAndConcat(str.substring(2));
        } else {
            return str;
        }
    }

    public static String applyMask(Inet4Address ipAddress, byte mask) {
        byte[] byteArr = ipAddress.getAddress();
        byte remaining = mask;
        int i = 0;
        while (remaining > 0) {
            if (remaining > 8) {
                remaining -= 8;
                i++;
            } else {
                byteArr[i] = (byte) (byteArr[i] & ((byte)-1 << (8 - remaining)));
                remaining = 0;
                i++;
                for (; i < byteArr.length; i++) {
                    byteArr[i] = 0;
                }
            }
        }
        try {
            return InetAddress.getByAddress(byteArr).getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static int ipFromString(String stringValue) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(stringValue);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        byte[] byteArr = inetAddress.getAddress();
        return ((byteArr[0] & 255) << 24) | ((byteArr[1] & 255) << 16) | ((byteArr[2]& 255) << 8) | (byteArr[3] & 255);
    }

    public static String ipFromInt(int intValue) {
        byte[] byteArr = new byte[]{(byte)(intValue >>> 24),
                (byte) ((intValue >>> 16) & 255),
                (byte) ((intValue >>> 8) & 255),
                (byte) (intValue & 255)};
        try {
            return InetAddress.getByAddress(byteArr).getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static PhysicalInterface getInterfaceInSameSubnet(Node node, String ipAddress, byte mask) {
        return node.getInterfaces().stream().filter(physicalInterface -> {
            if (physicalInterface.getInterfaceNetworkProp().getMask() == mask) {
                InterfaceNetworkProp prop = physicalInterface.getInterfaceNetworkProp();
                try {
                    return applyMask((Inet4Address) InetAddress.getByName(ipAddress), mask)
                            .equals(applyMask(prop.getIpAddress(), prop.getMask()));
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            }
            return false;
        }).findFirst().orElse(null);
    }
}
