package bank.server_communication;

public class Transaction {
    private int transactionID;
    private int clientID;
    private int balanceChange;

    public Transaction(int id, int balanceChange, int clientID) {
        this.transactionID = id;
        this.clientID = clientID;
        this.balanceChange = balanceChange;
    }

    int getClientID() { return clientID; }

    int getBalanceChange() {
        return balanceChange;
    }

    int getTransactionID() {
        return transactionID;
    }

    @Override
    public String toString(){
      return transactionID + "," + clientID + "," + balanceChange;
    }
}
