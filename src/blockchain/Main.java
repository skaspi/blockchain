package blockchain;
import bank.Server;

public class Main {
    public static void main(String[] args) {
       //TODO:  Add the main logic of the app
       Server server = new Server("127.0.0.1:2181", Integer.parseInt(args[0]));
        try {
           server.propose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
