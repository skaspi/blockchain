package blockchain;

import bank.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        LRUBuffer<Integer, Integer> buffer = new LRUBuffer<>(16, 0.75f, true, 5);

        Server server = new Server("127.0.0.1:2181", Integer.parseInt(args[0]));

        try {
            server.addBlock();
            server.addBlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.in.read();
    }
}
