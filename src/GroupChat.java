import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class GroupChat {
    public static String name;
    private static String EXIT = "exit";
    public static boolean isFinished = false;
    public static void main(String[] args) {
        try {
            InetAddress group = InetAddress.getByName("224.0.0.0");
            int port = 3000;
            Scanner scanner = new Scanner(System.in);
            System.out.print("enter the name: ");
            name = scanner.nextLine();
            MulticastSocket socket = new MulticastSocket(port);
            socket.setTimeToLive(0);
            socket.joinGroup(group);
            Thread thread = new Thread(new ReadThread(socket, group, port));
            thread.start();
            System.out.println("Start the Conversation");
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase(EXIT)) {
                    isFinished = true;
                    socket.leaveGroup(group);
                    socket.close();
                    break;
                }
                message = name + ": " + message;
                byte[] buffer = message.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buffer,
                        buffer.length,
                        group,
                        port);
                socket.send(datagramPacket);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

