package blockchain;

import bank.server.Server;

import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Main {
    public static void main(String[] args) {
        int serverID = Integer.parseInt(args[0]);
        int server_port = 5555 + serverID - 1;
        String response;
        Configuration.config(serverID);

        Server server = new Server("172.19.0.2:2181,172.19.0.3:2181,172.19.0.4:2181",
                                   Configuration.addresses, Integer.parseInt(args[0]),
                                   Configuration.LISTENER_PORT + serverID);

        try (ZContext context = new ZContext()) {
            ZMQ.Socket socket = context.createSocket(ZMQ.REP);
            socket.bind("tcp://*:" + server_port);

            while (!Thread.currentThread().isInterrupted()) {
                byte[] reply = socket.recv(0);

                String data = new String(reply, ZMQ.CHARSET);
                String[] tokens = data.split(",");
                if (tokens[1].charAt(0) != 'Q') {
                    try {
                        server.addTransaction(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                    } catch (NumberFormatException e){
                        e.getStackTrace();
                    }
                    response = "<" + data + ">" + " received for processing";
                }
                else{
                    response = "Amount:" + server.queryAmount(Integer.parseInt(tokens[0]));
                }
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
            }
        }
    }
}
