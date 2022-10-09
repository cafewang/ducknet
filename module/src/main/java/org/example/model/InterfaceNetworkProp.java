package org.example.model;

import java.net.Inet4Address;
import java.util.Random;

public class InterfaceNetworkProp {
    private static final Random RANDOM = new Random();
    String macAddress;
    Inet4Address ipAddress;
    byte mask;

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
}
