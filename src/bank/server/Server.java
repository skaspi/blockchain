package bank.server;

import bank.communication.BatchReceiver;
import bank.communication.Communicator;
import bank.communication.Transaction;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Server implements Watcher {

    class BatchID {
        int serverID;
        int batchNumber;

        BatchID(int serverID, int batchRunningNumber) {
            this.serverID = serverID;
            batchNumber = batchRunningNumber;
        }

        void setBatchNumber(int num) {
            batchNumber = num;
        }

        @Override
        public String toString() {
            return serverID + "," + batchNumber;
        }
    }

    private ZooKeeper zk;
    private BatchID batch;
    private ServerData db;
    private final Integer ID;
    private BatchReceiver receiver;
    private int batch_counter = 0;
    private int transaction_counter = 0;
    private int running_block = 0;
    private final String root = "/ROOT";
    private final String blocks_path = root + "/BLOCKS";
    private final String servers_path = root + "/SERVERS";

    private Stat state = new Stat();
    private List<Transaction> final_chain = new ArrayList<>();
    private List<Communicator> communicators = new ArrayList<>();
    private Set<String> connected_servers = new HashSet<>();

    public Server(String zkHost, List<InetSocketAddress> addresses, int id, int listenerPort) {
        ID = id;
        try {
            zk = new ZooKeeper(zkHost, 3000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (zk.exists(root, true) == null) {
                zk.create(root, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (zk.exists(blocks_path, true) == null) {
                zk.create(blocks_path, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (zk.exists(servers_path, true) == null) {
                zk.create(servers_path, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zk.create(servers_path + "/" + ID, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            zk.setData(servers_path + "/" + ID, String.valueOf(ID).getBytes(), -1);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        for (InetSocketAddress address : addresses) {
            communicators.add(new Communicator(address.getHostName(), address.getPort()));
        }
        db = new ServerData(ID);
        batch = new BatchID(ID, 0);
        receiver = new BatchReceiver(listenerPort, this);
    }

    public void addTransaction(int clientID, int changeBalance) throws KeeperException, InterruptedException {
        db.userCreate(clientID);
        db.tryUpdateBatch(clientID, changeBalance);
        transaction_counter++;
        if (isBatchThreshold()) {
            sendToServers(db.getTransactions(batch_counter, ID), batch_counter, ID);
            TimeUnit.SECONDS.sleep(1);  // TODO: Replace with something more meaningful ...
            addBlock(batch_counter++);
            db.clearTransactions();
        }
    }


    private boolean isBatchThreshold() {
        return ((transaction_counter % 3) == 0);
    }

    private void sendToServers(List<bank.communication.Transaction> transactions, int batchID, int senderID) {
        for (Communicator communicator : communicators) {
            communicator.sendBatch(transactions, batchID, senderID);
        }
    }

    private void addBlock(int batchRunningNumber) throws KeeperException, InterruptedException {
        batch.setBatchNumber(batchRunningNumber);
        zk.create(blocks_path + "/", String.valueOf(batch).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);
    }

    private void updateServers(String path) throws KeeperException, InterruptedException {
        Set<String> children = new HashSet<>(zk.getChildren(path, true));
        if (children.size() > connected_servers.size()) {
            connected_servers.addAll(children);
        } else {
            Set<String> difference = new HashSet<>(connected_servers);
            difference.removeAll(children);
            connected_servers = new HashSet<>(children);
            db.flush(Integer.parseInt(String.valueOf(difference.toArray()[0])));
        }
    }

    private boolean testAndUpdate(int id, int balance) {
        if (!db.query_key(id)) {
            db.update_amount(id, balance);
            return true;
        }
        if ((db.query_amount(id) + balance) < 0) {
            return false;
        }
        db.update_amount(id, db.query_amount(id) + balance);
        return true;
    }

    private void blockChain(String path) throws KeeperException, InterruptedException {
        zk.getData(path, true, state);

        if (state.getNumChildren() == 0) return;

        String[] tokens = new String(zk.getData(blocks_path + "/" + String.format("%010d", state.getNumChildren() - 1),
                true, null)).split(",");

        int server_id = Integer.parseInt(tokens[0]);
        int batch_id = Integer.parseInt(tokens[1]);
        for (Transaction t : db.getServerTransactions(server_id)) {
            if (t.getTransactionID() == batch_id) {
                if (testAndUpdate(t.getClientID(), t.getBalanceChange())) {
                    final_chain.add(new Transaction(running_block++, t.getBalanceChange(), t.getClientID()));
                }
            }
        }
        System.out.println(final_chain);
    }

    private void processRequest(String path) {
        try {
            zk.getChildren(root, true);
            zk.getChildren(servers_path, true);
            zk.getChildren(blocks_path, true);

            if (path == null) return;

            if (path.equals(servers_path)) {
                updateServers(path);
            } else if (path.equals(blocks_path)) {
                blockChain(path);
            }
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Skipping inconsistency in initial znode creation...");
        }
    }

    public void process(WatchedEvent watchedEvent) {
        processRequest(watchedEvent.getPath());
    }

    public void processReceivedBatch(int senderID, List<Transaction> transactions) {db.addBatch(senderID, transactions);}
}
