package bank;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import protos.Batch;
import protos.BatchSenderGrpc;

import java.util.List;

public class Communicator {
    private BatchSenderGrpc.BatchSenderFutureStub stub;
    private ManagedChannel channel;
    private int serverID;
    public Communicator(String host, int port) {
        channel = ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext()
            .build();
        stub = BatchSenderGrpc.newFutureStub(channel);
        this.serverID = serverID;
    }
    public void shutdown()
    {
        channel.shutdown();
    }

    public void sendBatch(List<Transaction> transactions, int batchID, int senderID) {
        sendBatchAction(transactions, batchID, senderID);
    }

    private void sendBatchAction(List<Transaction> transactions, int batchID, int senderID)
    {
        Batch.TransactionBatch.Builder batchBuilder = Batch.TransactionBatch.newBuilder().setBatchID(batchID).setSenderID(senderID);
        for (Transaction transaction : transactions){
            Batch.TransactionBatch.Transaction.Builder transactionBuilder = Batch.TransactionBatch.Transaction.newBuilder();
            transactionBuilder.setAccountNumber(transaction.getClientID()).
                    setChangeInSum(transaction.getBalanceChange()).
                    setTransactionID(transaction.getTransactionID());
            batchBuilder.addTransactions(transactionBuilder.build());
        }
        stub.sendBatch(batchBuilder.build()); //Return value is Empty so no need to store it
    }
    public int getServerID()
    {
        return serverID;
    }

}
