package org.example.model;

import lombok.Getter;

import java.net.Inet4Address;

@Getter
public class InterfaceNetworkProp {
    String macAddress;
    Inet4Address ipAddress;
    byte mask;

}
