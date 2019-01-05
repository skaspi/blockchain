package bank.communication;

import bank.server.Server;
import io.grpc.ServerBuilder;
import protos.Batch;
import protos.BatchSenderGrpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BatchReceiver extends BatchSenderGrpc.BatchSenderImplBase {
    private io.grpc.Server server;
    private Server bankServer;

    public BatchReceiver(int port, Server bankServer) {
        this.bankServer = bankServer;
        try {
            server = ServerBuilder.forPort(port)
                    .addService(this)
                    .build()
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    void shutdown() {
        server.shutdown();
    }

    @Override
    public void sendBatch(protos.Batch.TransactionBatch request,
                          io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {

        com.google.protobuf.Empty rep = com.google.protobuf.Empty.getDefaultInstance();
        responseObserver.onNext(rep);
        bankServer.processReceivedBatch(request.getSenderID(), buildTransactionsFromMessage(request.getTransactionsList()), request.getBatchID());
    }

    private List<Transaction> buildTransactionsFromMessage(List<Batch.TransactionBatch.Transaction> grpcTransactionsList) {
        List<Transaction> transactions = new ArrayList<>();
        for (Batch.TransactionBatch.Transaction grpcTransaction : grpcTransactionsList) {
            transactions.add(new Transaction(grpcTransaction.getTransactionID(), grpcTransaction.getChangeInSum(), grpcTransaction.getAccountNumber()));
        }
        return transactions;
    }
}
