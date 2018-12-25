package bank;

import org.apache.zookeeper.*;

import java.io.IOException;

public class Server implements Watcher {
    private final String root = "/ROOT";
    private ZooKeeper zk;
    private ServerData db;
    private Integer ID;

    public Server(String zkHost, Integer id) {
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
}
