package protos;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.13.1)",
    comments = "Source: batch.proto")
public final class ClientRESTInterfaceGrpc {

  private ClientRESTInterfaceGrpc() {}

  public static final String SERVICE_NAME = "protos.ClientRESTInterface";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<protos.Batch.ClientBalanceQuery,
      protos.Batch.ClientBalanceResponse> getGetBalanceMethod;

  public static io.grpc.MethodDescriptor<protos.Batch.ClientBalanceQuery,
      protos.Batch.ClientBalanceResponse> getGetBalanceMethod() {
    io.grpc.MethodDescriptor<protos.Batch.ClientBalanceQuery, protos.Batch.ClientBalanceResponse> getGetBalanceMethod;
    if ((getGetBalanceMethod = ClientRESTInterfaceGrpc.getGetBalanceMethod) == null) {
      synchronized (ClientRESTInterfaceGrpc.class) {
        if ((getGetBalanceMethod = ClientRESTInterfaceGrpc.getGetBalanceMethod) == null) {
          ClientRESTInterfaceGrpc.getGetBalanceMethod = getGetBalanceMethod = 
              io.grpc.MethodDescriptor.<protos.Batch.ClientBalanceQuery, protos.Batch.ClientBalanceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.ClientRESTInterface", "getBalance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Batch.ClientBalanceQuery.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Batch.ClientBalanceResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ClientRESTInterfaceMethodDescriptorSupplier("getBalance"))
                  .build();
          }
        }
     }
     return getGetBalanceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getPingMethod;

  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getPingMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.google.protobuf.Empty> getPingMethod;
    if ((getPingMethod = ClientRESTInterfaceGrpc.getPingMethod) == null) {
      synchronized (ClientRESTInterfaceGrpc.class) {
        if ((getPingMethod = ClientRESTInterfaceGrpc.getPingMethod) == null) {
          ClientRESTInterfaceGrpc.getPingMethod = getPingMethod = 
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.ClientRESTInterface", "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
                  .setSchemaDescriptor(new ClientRESTInterfaceMethodDescriptorSupplier("Ping"))
                  .build();
          }
        }
     }
     return getPingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ClientRESTInterfaceStub newStub(io.grpc.Channel channel) {
    return new ClientRESTInterfaceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ClientRESTInterfaceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ClientRESTInterfaceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ClientRESTInterfaceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ClientRESTInterfaceFutureStub(channel);
  }

  /**
   */
  public static abstract class ClientRESTInterfaceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getBalance(protos.Batch.ClientBalanceQuery request,
        io.grpc.stub.StreamObserver<protos.Batch.ClientBalanceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetBalanceMethod(), responseObserver);
    }

    /**
     */
    public void ping(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetBalanceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.Batch.ClientBalanceQuery,
                protos.Batch.ClientBalanceResponse>(
                  this, METHODID_GET_BALANCE)))
          .addMethod(
            getPingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                com.google.protobuf.Empty>(
                  this, METHODID_PING)))
          .build();
    }
  }

  /**
   */
  public static final class ClientRESTInterfaceStub extends io.grpc.stub.AbstractStub<ClientRESTInterfaceStub> {
    private ClientRESTInterfaceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ClientRESTInterfaceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientRESTInterfaceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ClientRESTInterfaceStub(channel, callOptions);
    }

    /**
     */
    public void getBalance(protos.Batch.ClientBalanceQuery request,
        io.grpc.stub.StreamObserver<protos.Batch.ClientBalanceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetBalanceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ping(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ClientRESTInterfaceBlockingStub extends io.grpc.stub.AbstractStub<ClientRESTInterfaceBlockingStub> {
    private ClientRESTInterfaceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ClientRESTInterfaceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientRESTInterfaceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ClientRESTInterfaceBlockingStub(channel, callOptions);
    }

    /**
     */
    public protos.Batch.ClientBalanceResponse getBalance(protos.Batch.ClientBalanceQuery request) {
      return blockingUnaryCall(
          getChannel(), getGetBalanceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty ping(com.google.protobuf.Empty request) {
      return blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ClientRESTInterfaceFutureStub extends io.grpc.stub.AbstractStub<ClientRESTInterfaceFutureStub> {
    private ClientRESTInterfaceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ClientRESTInterfaceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientRESTInterfaceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ClientRESTInterfaceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.Batch.ClientBalanceResponse> getBalance(
        protos.Batch.ClientBalanceQuery request) {
      return futureUnaryCall(
          getChannel().newCall(getGetBalanceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> ping(
        com.google.protobuf.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_BALANCE = 0;
  private static final int METHODID_PING = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ClientRESTInterfaceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ClientRESTInterfaceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_BALANCE:
          serviceImpl.getBalance((protos.Batch.ClientBalanceQuery) request,
              (io.grpc.stub.StreamObserver<protos.Batch.ClientBalanceResponse>) responseObserver);
          break;
        case METHODID_PING:
          serviceImpl.ping((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ClientRESTInterfaceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ClientRESTInterfaceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return protos.Batch.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ClientRESTInterface");
    }
  }

  private static final class ClientRESTInterfaceFileDescriptorSupplier
      extends ClientRESTInterfaceBaseDescriptorSupplier {
    ClientRESTInterfaceFileDescriptorSupplier() {}
  }

  private static final class ClientRESTInterfaceMethodDescriptorSupplier
      extends ClientRESTInterfaceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ClientRESTInterfaceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ClientRESTInterfaceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ClientRESTInterfaceFileDescriptorSupplier())
              .addMethod(getGetBalanceMethod())
              .addMethod(getPingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
