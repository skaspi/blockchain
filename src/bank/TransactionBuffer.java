package bank;

import java.util.LinkedHashMap;

class TransactionBuffer {
    private LinkedHashMap<Integer, Integer> buffer;

    TransactionBuffer() {
        buffer = new LinkedHashMap<>();
    }

    void accept(int id, int amount){
        buffer.put(id, amount);
    }
}
