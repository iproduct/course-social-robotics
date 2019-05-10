package org.iproduct.demos.spring.streamingdemos.udp;

import java.net.InetAddress;
import java.util.Objects;

class AddressInfo {
    public InetAddress address;
    public int port;

    public AddressInfo(InetAddress a, int p) {
        address = a;
        port = p;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressInfo)) return false;
        AddressInfo that = (AddressInfo) o;
        return port == that.port &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AddressInfo{");
        sb.append(address);
        sb.append(":").append(port);
        return sb.toString();
    }
}
