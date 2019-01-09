import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.io.IOException;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args) throws IOException {
        try (ZContext context = new ZContext()) {
            //  Socket to talk to server
            System.out.println("Connecting to Greedy Bank server");

            ZMQ.Socket socket = context.createSocket(ZMQ.REQ);
            socket.connect("tcp://192.168.1.4:5555");

            try {
                while (true) {
                    Scanner reader = new Scanner(System.in);
                    String[] clientID = reader.nextLine().split(" ");
                    reader.useDelimiter(",|\\s+");
                    String[] changeBalance = reader.nextLine().split(" ");

                    String request = clientID[0] + "," + changeBalance[0];
                    socket.send(request.getBytes(ZMQ.CHARSET), 0);

                    byte[] reply = socket.recv(0);
                    System.out.println(new String(reply, ZMQ.CHARSET));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.in.read();
        }
    }
}