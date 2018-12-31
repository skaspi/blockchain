package bank.server_data;

import bank.server_communication.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerData {
    private HashMap<Integer, Integer> client_base = new HashMap<>();
    private TransactionBuffer batch_buffer = new TransactionBuffer();
    private HashMap<Integer, List<Transaction>> ISC_buffer = new HashMap<>();

    public void addBatch(int senderID, List<Transaction> transactionList) {
        for (Transaction t : transactionList) {
            if(!ISC_buffer.containsKey(senderID)){
               ISC_buffer.put(senderID, new ArrayList<>());
            }
            ISC_buffer.get(senderID).add(t);
        }
    }

    public void userCreate(int id) {
        if (!client_base.containsKey(id))
            client_base.put(id, 100);
    }

    public void updateBalance(int id, int amount) {
        int new_amount = client_base.get(id) + amount;
        if (new_amount >= 0) {
            batch_buffer.accept(id, new_amount);
            client_base.replace(id, new_amount);  //TODO: After Zookeeper trigger watch
        }
    }

    public List<Transaction> getTransactions(int batch_counter) {
        List<Transaction> transactionList = new ArrayList<>();
        for (int key : batch_buffer.getUserBuffer().keySet()) {
            transactionList.add(new Transaction(batch_counter, batch_buffer.getUserBuffer().get(key), key));
        }
        return transactionList;
    }

    public void clearTransaction() {
        batch_buffer.clear();
    }

    public void print(){
        System.out.println("HERE" + ISC_buffer.toString());
    }

    public void flush(int serverID){
        ISC_buffer.remove(serverID);
    }

}
