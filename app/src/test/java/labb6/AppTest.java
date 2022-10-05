package labb6;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.InetAddress;
import java.net.NetworkInterface;

import org.junit.jupiter.api.BeforeAll;

class AppTest {
    static InetAddress group;
    static int port;
    static NetworkInterface netIf;

    @BeforeAll
    public static void setup() {
        try {
            group = InetAddress.getByName("239.0.0.0");
            port = Integer.parseInt("1234");
            netIf = NetworkInterface.getByName("bge0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void appCanStartMulticastSocketWhenGivenValidParameters() {
        try {
            group = InetAddress.getByName("239.0.0.0");
            port = Integer.parseInt("1234");
            netIf = NetworkInterface.getByName("bge0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertDoesNotThrow(() -> App.startMulticast(group, port, netIf));

    }
}
