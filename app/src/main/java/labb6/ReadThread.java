package labb6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ReadThread implements Runnable {

    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private User newUser;
    private static final int MAX_LEN = 1000;

    ReadThread(MulticastSocket socket, InetAddress group, int port, User newUser) {

        this.socket = socket;
        this.group = group;
        this.port = port;
        this.newUser = newUser;
    }

    @Override
    public void run() {

        while (!App.finished) {

            byte[] buffer = new byte[ReadThread.MAX_LEN];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
            String message;

            try {
                socket.receive(datagram);
                message = new String(buffer, 0, datagram.getLength(), "UTF-8");
                if (!message.startsWith(newUser.getName())) {
                    System.out.println(message);
                }
            } catch (IOException ioe) {
                System.out.println("Socket closed!");
            }
        }
    }
}