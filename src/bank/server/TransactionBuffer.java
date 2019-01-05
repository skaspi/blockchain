package bank.server;

import java.util.LinkedHashMap;

class TransactionBuffer {
    private LinkedHashMap<Integer, Integer> buffer = new LinkedHashMap<>();

    void accept(int id, int amount) {
        int temp = buffer.getOrDefault(id, 0);
        buffer.put(id, temp + amount);
    }

    LinkedHashMap<Integer, Integer> getUserBuffer() {
        return buffer;
    }

    void clear() {
        buffer.clear();
    }
}