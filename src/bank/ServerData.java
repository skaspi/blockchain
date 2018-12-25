package bank;

import java.util.HashMap;

class ServerData {
    private HashMap<Integer, Integer> client_base;
    private TransactionBuffer batch_buffer;
    private HashMap<Integer,Transaction> ISC_buffer;

    ServerData() {
        client_base = new HashMap<>();
        batch_buffer = new TransactionBuffer();
        ISC_buffer = new HashMap<>();
    }

    void userCreate(int id) {
        client_base.put(id, 100);
    }

    void updateBalance(int id, int amount) {
        int new_amount = client_base.get(id) + amount;
        if (new_amount >= 0) {
            client_base.put(id, new_amount);
            batch_buffer.accept(id, new_amount);
        }
    }
}
