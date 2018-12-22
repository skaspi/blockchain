package blockchain;
import bank.Server;

import java.net.InetSocketAddress;
import java.util.*;

public class Main {

    final static int NUMBER_OF_SERVERS = 30;
    final static int STARTING_PORT = 12345;
    public static void main(String[] args) {
       //TODO:  Add the main logic of the app
//       Server server = new Server("127.0.0.1:2181", Integer.parseInt(args[0]));
//        try {
//           server.propose();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        int serverID = Integer.parseInt(args[0]);
        Server server = new Server(serverID, addAddresses(serverID), STARTING_PORT+serverID); // Every server listens on the same port
        Scanner reader = new Scanner(System.in);

        while(true) {

            int clientID = reader.nextInt();
            int changeBalance = reader.nextInt();
            server.sendMessage(clientID,changeBalance);
        }
    }
    public static List<InetSocketAddress> addAddresses(int serverID)
    {
        List<InetSocketAddress> addresses = new ArrayList<>();
        for (int i = 0; i<NUMBER_OF_SERVERS; i++) {
            if (i== serverID) {
                continue;
            }
            addresses.add(new InetSocketAddress("127.0.0.1", STARTING_PORT+i));
        }
        return addresses;
    }
}
