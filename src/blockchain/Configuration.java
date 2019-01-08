package blockchain;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

class Configuration {
    private final static String LOCAL_HOST = "127.0.0.1";
    static final int LISTENER_PORT = 12345;
    static final int REST_LISTENER_PORT = 54321;
    private final static int NUMBER_OF_SERVERS = 30;
    static final ArrayList<InetSocketAddress> addresses = new ArrayList<>();

    static void config(int serverID) {
        addAddresses(serverID);
    }


    private static List<InetSocketAddress> addAddresses(int serverID) {
        List<InetSocketAddress> addresses = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            if (i == serverID) {
                continue;
            }
            Configuration.addresses.add(new InetSocketAddress(LOCAL_HOST, LISTENER_PORT + i));
        }
        return addresses;
    }
}
