package bank.server;

import bank.communication.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ServerData {
    private HashMap<Integer, Integer> client_base = new HashMap<>();
    private TransactionBuffer batch_buffer = new TransactionBuffer();
    private HashMap<Integer, List<Transaction>> ISC_buffer = new HashMap<>();
    private List<Server.BatchID> blocksWaitingToBeReceived = new ArrayList<>();

    ServerData(int own_id) {
        ISC_buffer.put(own_id, new ArrayList<>());
    }

    void addBatch(int senderID, List<Transaction> transactionList) {
        if (!ISC_buffer.containsKey(senderID)) {
            ISC_buffer.put(senderID, new ArrayList<>());
        }
        for (Transaction t : transactionList) {
            ISC_buffer.get(senderID).add(t);
        }
    }

    void userCreate(int id) {
        if (!client_base.containsKey(id))
            client_base.put(id, 0);
    }

    void tryUpdateBatch(int id, int amount) {
        if (client_base.get(id) + amount >= 0) {
            batch_buffer.accept(id, amount);
        }
    }

    List<Transaction> getTransactions(int batch_counter, int own_id) {
        List<Transaction> transactionList = new ArrayList<>();
        for (int key : batch_buffer.getUserBuffer().keySet()) {
            Transaction t = new Transaction(batch_counter, batch_buffer.getUserBuffer().get(key), key);
            transactionList.add(t);
            ISC_buffer.get(own_id).add(t);
        }
        return transactionList;
    }

    // TODO: Remove after debugging
    String print() { return client_base.toString();  }

    boolean query_key(int id) { return client_base.containsKey(id);    }

    int query_amount(int id) { return client_base.get(id); }

    void update_amount(int id, int amount) { client_base.put(id, amount); }

    List<Transaction> getServerTransactions(int id) { return ISC_buffer.getOrDefault(id, new ArrayList<>()); }

    public List<Server.BatchID> getBlocksWaitingToBeReceived() {
        return blocksWaitingToBeReceived;
    }

    void clearTransactions() {
        batch_buffer.clear();
    }

    void flush(int serverID) { ISC_buffer.remove(serverID); }

    public void addWaitingBlock(Server.BatchID batchID) {
        blocksWaitingToBeReceived.add(batchID);
    }
}
