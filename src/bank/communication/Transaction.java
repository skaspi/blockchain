package bank.communication;

import org.apache.commons.codec.digest.DigestUtils;

public class Transaction {
    private int transactionID;
    private int clientID;
    private int balanceChange;

    public Transaction(int id, int balanceChange, int clientID) {
        this.transactionID = id;
        this.clientID = clientID;
        this.balanceChange = balanceChange;
    }

    public int getClientID() { return clientID; }

    public int getBalanceChange() {
        return balanceChange;
    }

    public int getTransactionID() {
        return transactionID;
    }

    @Override
    public String toString() {
        return ("<" + DigestUtils.sha1Hex(String.valueOf(transactionID)).substring(35) + "," + clientID + "," + balanceChange + ">");
    }
}
