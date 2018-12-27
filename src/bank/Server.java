package bank;
//import org.apache.zookeeper.*;

import org.apache.zookeeper.*;

import java.io.IOException;

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
public class Server implements Watcher {
    private final String root = "/ROOT";
    private ZooKeeper zk;
    private ServerData db;
    private Integer ID;

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
            db = new ServerData();
            ID = id;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (zk.exists(root, true) == null) {
                zk.create(root, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (zk.exists(root + "/BLOCKS", true) == null) {
                zk.create(root + "/BLOCKS", new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (zk.exists(root + "/SERVERS", true) == null) {
                zk.create(root + "/SERVERS", new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zk.create(root + "/SERVERS/" + ID, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            zk.setData(root + "/SERVERS/" + ID, String.valueOf(ID).getBytes(), -1);

        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addBlock() throws KeeperException, InterruptedException {
        String ddd = "8,1";
        zk.create(root + "/BLOCKS/", ddd.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);
    }

    private void totalOrder() {
        try {
            System.out.println(zk.getChildren(root + "/BLOCKS", true));
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Skipping inconsistency in initial znode creation...");
        }
    }

    public void process(WatchedEvent watchedEvent) {
        try {
            totalOrder();
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
