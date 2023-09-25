import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static java.nio.charset.StandardCharsets.*;

public class ReadThread implements Runnable{
    private static final int MAX_LEN = 1000;
    private MulticastSocket socket;
    private InetAddress group;
    private int port;

    public ReadThread(MulticastSocket socket, InetAddress group, int port) {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {
        while (!GroupChat.isFinished) {
            byte[] buffer = new byte[MAX_LEN];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, MAX_LEN, group, port);
            try {
                socket.receive(datagramPacket);
                String message = new String(buffer,
                        0, datagramPacket.getLength(),
                        UTF_8);
                System.out.println(message);
                if (!message.startsWith(GroupChat.name)) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Conversation ended");
            }
        }
    }
}

