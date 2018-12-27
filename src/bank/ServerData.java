package bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ServerData {
    private HashMap<Integer, Integer> client_base = new HashMap<>();
    private TransactionBuffer batch_buffer = new TransactionBuffer();
    private HashMap<Integer, List<Transaction>> ISC_buffer = new HashMap<>();
    private int transactionCounter = 0;

    void addBatch(int senderID, List<Transaction> transactionList) {
        for (Transaction t : transactionList) {
            if(!ISC_buffer.containsKey(senderID)){
               ISC_buffer.put(senderID, new ArrayList<>());
            }
            ISC_buffer.get(senderID).add(t);
        }
    }

    void userCreate(int id) {
        if (!client_base.containsKey(id))
            client_base.put(id, 100);
    }

    void updateBalance(int id, int amount) {
        int new_amount = client_base.get(id) + amount;
        if (new_amount >= 0) {
            batch_buffer.accept(id, new_amount);
            client_base.replace(id, new_amount);  //TODO: After Zookeeper trigger watch
        }
    }

    List<Transaction> getTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        for (int key : batch_buffer.getUserBuffer().keySet()) {
            transactionList.add(new Transaction(transactionCounter++, batch_buffer.getUserBuffer().get(key), key));
        }
        return transactionList;
    }

    void clearTransaction() {
        batch_buffer.clear();
    }

    void print(){
        System.out.println("HERE" + ISC_buffer.toString());
    }

    void flush(int serverID){
        ISC_buffer.remove(serverID);
    }

}
