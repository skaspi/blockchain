package bank;
//import org.apache.zookeeper.*;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Server { //implements Watcher{
    //static ZooKeeper zk = null;
    static String root = "/ROOT";
    int ID;
    static int transactionID = 0;
    private List<Communicator> communicators;
    private List<Integer> receivedBatchIDs;
    private BatchReceiver receiver;

    public Server(int id, List<InetSocketAddress> addresses, int listenerPort) {
        ID = id;
        receivedBatchIDs = new ArrayList<>();
        communicators = new ArrayList<>();
        for (InetSocketAddress address : addresses)
        {
            communicators.add(new Communicator(address.getHostName(), address.getPort()));
        }
        receiver = new BatchReceiver(listenerPort, this);
    }

    public void sendMessage(int clientID, int changeBalance) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i =0; i< 5; i++) {
            transactions.add(new Transaction(transactionID++,clientID+i,changeBalance+i));
        }
        sendToServers(transactions, transactionID / 5, ID);
    }

    private void sendToServers(List<Transaction> transactions, int batchID, int senderID) {
        for (Communicator communicator : communicators) {
                communicator.sendBatch(transactions, batchID, senderID);
        }
    }

/*
    public Server(String zkHost, int id) {
        try {
            zk = new ZooKeeper(zkHost, 3000, this);
            ID = id;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void propose() throws KeeperException, InterruptedException {
        if (zk.exists(root, true) == null) {
            zk.create(root, new byte[] {}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        zk.create(root + "/", String.valueOf(ID).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);
    }

    public void process(WatchedEvent watchedEvent) {
        //TODO: Change to more meaningful code.
        //      For now, it's only for passing compilation.
        final Event.EventType eventType = watchedEvent.getType();
        try {
           // electLeader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    public void processReceivedBatch(int batchID, int senderID, List<Transaction> transactions) {

        if (!receivedBatchIDs.contains(batchID) && senderID != this.ID)
        {
            receivedBatchIDs.add(batchID);
            sendToServers(transactions, batchID, senderID); // Send originsl senderID in the message
            System.out.println("Recieved batch number: " + batchID+  " From: " + senderID +  " batch contains following transactions");
            for (Transaction t: transactions)
            {
                System.out.println("Transaction " + t.getTransactionID()+ ": client ID - " + t.getClientID() + " balance change - "+ t.getBalanceChange());
            }
        }
        else {
            System.out.println("Recieved repeated message number: " + batchID+ "from server: " + senderID);

        }
    }
}
