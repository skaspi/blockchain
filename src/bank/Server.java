package bank;

import javafx.util.Pair;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Server implements Watcher {
    private final static String root = "/ROOT";
    private static ZooKeeper zk = null;
    private static boolean ordered;
    private int ID;
    private int block_id;

    private final Object lock = new Object();

    public Server(String zkHost, int id) {
        try {
            zk = new ZooKeeper(zkHost, 3000, this);
            ID = id;
            block_id = 1;
            ordered = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            synchronized (lock) {
                if (zk.exists(root, true) == null) {
                    zk.create(root, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addBlock() throws KeeperException, InterruptedException {
        ordered = false;
        Pair<Integer, Integer> pair = new Pair<>(ID, block_id++);

        zk.create(root + "/", pair.toString().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);
    }


    private void totalOrder() throws KeeperException, InterruptedException {
        synchronized (lock) {
            List<String> children = zk.getChildren(root, true);
            Collections.sort(children);
            ordered = true;
        }
    }

    public void process(WatchedEvent watchedEvent) {
        try {
            totalOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
