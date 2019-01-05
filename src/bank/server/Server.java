package bank.server;

import bank.communication.BatchReceiver;
import bank.communication.Communicator;
import bank.communication.Transaction;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import protos.Batch;

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

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof BatchID)) {
                return false;
            }
            BatchID batchID = (BatchID) object;
            return (serverID == batchID.serverID) && (batchNumber == batchID.batchNumber);
        }

        void setBatchNumber(int num) {
            batchNumber = num;
        }

        @Override
        public String toString() {
            return serverID + "," + batchNumber;
        }
    }

    private final long BATCH_TIMEOUT_IN_MILISECONDS = 3000;
    private final int MAX_TRANSACTIONS_IN_BATCH = 10;

    private ZooKeeper zk;
    private BatchID batch;
    private ServerData db;
    private final Integer ID;
    private BatchReceiver receiver;
    private int batch_counter = 0;
    private int transaction_counter = 0;
    private int running_block = 0;
    private int transactionsiInCurrentBlock = 0;
    private final String root = "/ROOT";
    private final String blocks_path = root + "/BLOCKS";
    private final String servers_path = root + "/SERVERS";

    private Stat state = new Stat();
    private Timer batchTimer = new Timer("batchTimer");
    private List<Transaction> final_chain = new ArrayList<>();
    private List<Communicator> communicators = new ArrayList<>();
    private Set<String> connected_servers = new HashSet<>();


    private TimerTask createNewBatchTask() {
        return new TimerTask() {
            @Override
            public void run() {
                transactionsiInCurrentBlock = 0;
                sendToServers(db.getTransactions(batch_counter, ID), batch_counter, ID);
                try {
                    addBlock(batch_counter++);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
                db.clearTransactions();
            }
        };
    }

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
        transactionsiInCurrentBlock++;
        batchTimer.cancel();
        batchTimer = new Timer("batchTimer");
        if (transactionsiInCurrentBlock < MAX_TRANSACTIONS_IN_BATCH) {
            batchTimer.schedule(createNewBatchTask(), BATCH_TIMEOUT_IN_MILISECONDS);
            return;
        }
        createNewBatchTask().run();
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
        int batchRunningNumber = Integer.parseInt(tokens[1]);
        BatchID batchID = new BatchID(server_id, batchRunningNumber);
        if (!db.getBlocksWaitingToBeReceived().isEmpty()) { //There is a previous ZK block that hasn't been received in gRPC, so can't "deliver" it
            db.addWaitingBlock(batchID);
            return;
        }
        if (!deliverTransactions(server_id, batchRunningNumber)) { //Couldn't find the required batch in the ISC buffer so adding to the waiting blocks
            db.addWaitingBlock(batchID);

        }
    }

    private boolean deliverTransactions(int server_id, int batchRunningNumber) {
        boolean flag = false;
        for (Transaction t : db.getServerTransactions(server_id)) {
            if (t.getTransactionID() == batchRunningNumber) {
                if (testAndUpdate(t.getClientID(), t.getBalanceChange())) {
                    final_chain.add(new Transaction(running_block++, t.getBalanceChange(), t.getClientID()));
                    flag = true;
                    System.out.println(final_chain);
                }
            }
        }
        return flag;
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

    public void processReceivedBatch(int senderID, List<Transaction> transactions, int batchRunningNumber) {
        db.addBatch(senderID, transactions);
        BatchID receivedBatchID = new BatchID(senderID, batchRunningNumber);
        List<BatchID> blocksWaitingToBeReceived = db.getBlocksWaitingToBeReceived();
        if (blocksWaitingToBeReceived.isEmpty()) {
            return;
        }
        if (blocksWaitingToBeReceived.get(0).equals(receivedBatchID)) { // The first (earliest) block is the same as the received so we can "deliver" it
            deliverTransactions(senderID, batchRunningNumber);
            System.out.println("found waiting batch: " + receivedBatchID);
            blocksWaitingToBeReceived.remove(0);
            ListIterator<BatchID> iter = blocksWaitingToBeReceived.listIterator();
            while (iter.hasNext()) {  // Go over all the other waiting blocks and try to deliver them
                BatchID nextWaitingBatchID = iter.next();
                if (!deliverTransactions(nextWaitingBatchID.serverID, nextWaitingBatchID.batchNumber)) {
                    break;  // If couldn't deliver (not in the ISC buffer) then stop
                }
                iter.remove();
                System.out.println("found another batch: " + nextWaitingBatchID);
            }
        }
    }
}
