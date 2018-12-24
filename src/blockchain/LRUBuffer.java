package blockchain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
  Implementing a simple LRU Cache, with LinkedHashMap
  See reference: https://www.baeldung.com/java-linked-hashmap
 */

public class LRUBuffer<K, V> extends LinkedHashMap<K, V> {

    private int LIMIT;

    LRUBuffer(int initialCapacity, float loadFactor, boolean accessOrder, int limit) {
        super(initialCapacity, loadFactor, accessOrder);
        LIMIT = limit;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > LIMIT;
    }

}