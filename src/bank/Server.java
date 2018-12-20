package bank;
import org.apache.zookeeper.*;
import java.io.IOException;

public class Server implements Watcher{
    static ZooKeeper zk = null;
    static String root = "/ROOT";
    static int ID;

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
}
