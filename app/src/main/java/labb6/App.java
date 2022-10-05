package labb6;

import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.*;

public class App {

    private static final String TERMINATE = "Exit";
    static volatile boolean finished = false;
    static ArrayList<String> sentMsgs = new ArrayList<String>();

    public static void main(String[] args) {

        // if (args.length != 2) {
        // System.out.println("Two arguments required: <host> <port>");
        // } else {
        try {
            User newUser = new User();
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter name: ");
            newUser.setName(sc.nextLine());

            InetAddress group = InetAddress.getByName("239.0.0.0");
            int port = Integer.parseInt("1234");
            NetworkInterface netIf = NetworkInterface.getByName("bge0");

            MulticastSocket socket = startMulticast(group, port, netIf);
            startThread(newUser, group, port, socket);

            System.out.println("Type messages: \n");
            while (true) {
                String message;
                message = sc.nextLine();
                if (message.equalsIgnoreCase(App.TERMINATE)) {
                    finished = true;
                    socket.leaveGroup(new InetSocketAddress(group, 0), netIf);
                    socket.close();
                    break;
                } else if (message.equalsIgnoreCase("donate")) {
                    message = newUser.userDonate();
                    packetSender(group, port, socket, message);
                } else if (message.equalsIgnoreCase("celebrate")) {
                    message = newUser.userCelebrate();
                    packetSender(group, port, socket, message);
                } else if (message.equalsIgnoreCase("list")) {
                    sentMsgs.forEach((msg) -> {
                        System.out.println(msg);
                    });
                } else {
                    message = newUser.getName() + ": " + message;
                    packetSender(group, port, socket, message);
                    sentMsgs.add(message);
                }
            }
        } catch (SocketException se) {
            System.out.println("Error creating socket.");
            se.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Error reading/writing from/to socket.");
            ioe.printStackTrace();
        }
        // }
    }

    protected static MulticastSocket startMulticast(InetAddress group, int port, NetworkInterface netIf)
            throws IOException {
        MulticastSocket socket = new MulticastSocket(port);
        socket.setTimeToLive(0);
        // Set to 1 for subnet
        socket.joinGroup(new InetSocketAddress(group, 0), netIf);
        return socket;
    }

    private static void startThread(User newUser, InetAddress group, int port, MulticastSocket socket) {
        Thread thread = new Thread(new ReadThread(socket, group, port, newUser));
        thread.start();
    }

    private static void packetSender(InetAddress group, int port, MulticastSocket socket, String message)
            throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
        socket.send(datagram);
    }

}
