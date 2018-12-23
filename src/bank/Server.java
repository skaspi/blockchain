package bank;

import javafx.util.Pair;
import org.apache.zookeeper.*;

import java.io.IOException;

public class Server implements Watcher {
    private final static String root = "/ROOT";
    private static ZooKeeper zk = null;
    private int ID;
    private int block_id;

    private final Object lock = new Object();

    public Server(String zkHost, int id) {
        try {
            zk = new ZooKeeper(zkHost, 3000, this);
            ID = id;
            block_id = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            synchronized (lock) {
                if (zk.exists(root, true) == null) {
                    zk.create(root, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    zk.create(root + "/BLOCKS", new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addBlock() throws KeeperException, InterruptedException {
        Pair<Integer, Integer> pair = new Pair<>(ID, block_id++);
        zk.create(root + "/BLOCKS/", pair.toString().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);
    }

    private void totalOrder() {
        try {
            System.out.println(zk.getChildren(root + "/BLOCKS", true));
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Skipping inconsistency in initial znode creation...");
        }
        //TODO: Remove printing after debugging
    }

    public void process(WatchedEvent watchedEvent) {
        try {
            totalOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
