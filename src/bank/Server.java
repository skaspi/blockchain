package bank;

import bank.server_communication.BatchReceiver;
import bank.server_communication.Communicator;
import bank.server_communication.Transaction;
import bank.server_data.ServerData;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    private final Integer ID;
    private BatchReceiver receiver;
    private int batch_counter = 0;
    private int transaction_counter = 0;
    private final String root = "/ROOT";
    private final String blocks_path = root + "/BLOCKS";
    private final String servers_path = root + "/SERVERS";
    private ServerData db = new ServerData();
    private Stat dummy = new Stat();
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
        batch = new BatchID(ID, 0);
        receiver = new BatchReceiver(listenerPort, this);
    }

    public void addTransaction(int clientID, int changeBalance) throws KeeperException, InterruptedException {
        db.userCreate(clientID);
        db.updateBalance(clientID, changeBalance);
        if (isBatchThreshold()) {
            sendToServers(db.getTransactions(batch_counter), batch_counter, ID);
            addBlock(batch_counter++);
            db.clearTransaction();
        }
        transaction_counter++;
    }

    private boolean isBatchThreshold() {
        //TODO: Replace with the logic of adapting batching
        return (transaction_counter > 1 && (transaction_counter % 3) == 0);
    }

    private void sendToServers(List<bank.server_communication.Transaction> transactions, int batchID, int senderID) {
        for (Communicator communicator : communicators) {
            communicator.sendBatch(transactions, batchID, senderID);
        }
    }

    private void addBlock(int batchRunningNumber) throws KeeperException, InterruptedException {
        batch.setBatchNumber(batchRunningNumber);
        zk.create(blocks_path + "/", String.valueOf(batch).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    private void updateServers(String path) {
        try {
            Set<String> children = new HashSet<>(zk.getChildren(path, true));
            if (children.size() > connected_servers.size()) {
                connected_servers.addAll(children);
            } else {
                Set<String> difference = new HashSet<>(connected_servers);
                difference.removeAll(children);
                connected_servers = new HashSet<>(children);
                db.flush(Integer.parseInt(String.valueOf(difference.toArray()[0])));
                db.print();
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processRequest(String path) {
        try {
            //TODO : Remove wasteful syscalls
            System.out.println(zk.getChildren(root, true));
            System.out.println(zk.getChildren(servers_path, true));
            System.out.println(zk.getChildren(blocks_path, true));
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Skipping inconsistency in initial znode creation...");
        }
        if (path == null) return;

        if (path.equals(servers_path)) {
            updateServers(path);
        } else if (path.equals(blocks_path)) {
            try {
                List<String> children = new ArrayList<>(zk.getChildren(path, true));
                for (String s : children) {
                    String data = new String(zk.getData(blocks_path + "/" + s, true, dummy));
                    System.out.println(data + '|');
                }
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.getPath() + watchedEvent.getType() + watchedEvent.getState());
        processRequest(watchedEvent.getPath());
    }

    public void processReceivedBatch(int batchID, int senderID, List<Transaction> transactions) {
        db.addBatch(senderID, transactions);
        db.print();
    }
}
