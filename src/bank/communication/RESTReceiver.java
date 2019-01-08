package bank.communication;

import bank.server.Server;
import com.google.protobuf.Empty;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import protos.Batch;
import protos.ClientRESTInterfaceGrpc;

import java.io.IOException;

public class RESTReceiver extends ClientRESTInterfaceGrpc.ClientRESTInterfaceImplBase {

    private io.grpc.Server server;
    private Server bankServer;

    public RESTReceiver(int port, Server bankServer)
    {
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
    @Override
    public void ping(Empty request, StreamObserver<Empty> responseObserver) {
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getBalance(Batch.ClientBalanceQuery request,
                           StreamObserver<Batch.ClientBalanceResponse> responseObserver) {
        int balance = bankServer.getClientBalance(request.getClientID());
        Batch.ClientBalanceResponse response = Batch.ClientBalanceResponse.newBuilder().setBalance(balance).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
