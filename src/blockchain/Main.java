package blockchain;

import bank.Server;

public class Main {
    public static void main(String[] args) {

        Server server = new Server("127.0.0.1:2181", Integer.parseInt(args[0]));
        try {
            server.addBlock();
            //TODO: Make the server stateful
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
