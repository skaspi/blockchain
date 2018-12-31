package blockchain;

import bank.server.Server;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        int serverID = Integer.parseInt(args[0]);
        Configuration.config(serverID);
        Server server = new Server("127.0.0.1:2181", Configuration.addresses, Integer.parseInt(args[0]), Configuration.LISTENER_PORT + serverID);

        try {
            while (true) {
                Scanner reader = new Scanner(System.in);
                int clientID = reader.nextInt();
                reader.useDelimiter(",|\\s+");
                int changeBalance = reader.nextInt();
                server.addTransaction(clientID, changeBalance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.in.read();
    }
}