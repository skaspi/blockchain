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
public final class BatchSenderGrpc {

  private BatchSenderGrpc() {}

  public static final String SERVICE_NAME = "protos.BatchSender";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<protos.Batch.TransactionBatch,
      com.google.protobuf.Empty> getSendBatchMethod;

  public static io.grpc.MethodDescriptor<protos.Batch.TransactionBatch,
      com.google.protobuf.Empty> getSendBatchMethod() {
    io.grpc.MethodDescriptor<protos.Batch.TransactionBatch, com.google.protobuf.Empty> getSendBatchMethod;
    if ((getSendBatchMethod = BatchSenderGrpc.getSendBatchMethod) == null) {
      synchronized (BatchSenderGrpc.class) {
        if ((getSendBatchMethod = BatchSenderGrpc.getSendBatchMethod) == null) {
          BatchSenderGrpc.getSendBatchMethod = getSendBatchMethod = 
              io.grpc.MethodDescriptor.<protos.Batch.TransactionBatch, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.BatchSender", "SendBatch"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Batch.TransactionBatch.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
                  .setSchemaDescriptor(new BatchSenderMethodDescriptorSupplier("SendBatch"))
                  .build();
          }
        }
     }
     return getSendBatchMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BatchSenderStub newStub(io.grpc.Channel channel) {
    return new BatchSenderStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BatchSenderBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new BatchSenderBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BatchSenderFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new BatchSenderFutureStub(channel);
  }

  /**
   */
  public static abstract class BatchSenderImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendBatch(protos.Batch.TransactionBatch request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getSendBatchMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendBatchMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.Batch.TransactionBatch,
                com.google.protobuf.Empty>(
                  this, METHODID_SEND_BATCH)))
          .build();
    }
  }

  /**
   */
  public static final class BatchSenderStub extends io.grpc.stub.AbstractStub<BatchSenderStub> {
    private BatchSenderStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BatchSenderStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BatchSenderStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BatchSenderStub(channel, callOptions);
    }

    /**
     */
    public void sendBatch(protos.Batch.TransactionBatch request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendBatchMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class BatchSenderBlockingStub extends io.grpc.stub.AbstractStub<BatchSenderBlockingStub> {
    private BatchSenderBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BatchSenderBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BatchSenderBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BatchSenderBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.google.protobuf.Empty sendBatch(protos.Batch.TransactionBatch request) {
      return blockingUnaryCall(
          getChannel(), getSendBatchMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BatchSenderFutureStub extends io.grpc.stub.AbstractStub<BatchSenderFutureStub> {
    private BatchSenderFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BatchSenderFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BatchSenderFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BatchSenderFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> sendBatch(
        protos.Batch.TransactionBatch request) {
      return futureUnaryCall(
          getChannel().newCall(getSendBatchMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_BATCH = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BatchSenderImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BatchSenderImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_BATCH:
          serviceImpl.sendBatch((protos.Batch.TransactionBatch) request,
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

  private static abstract class BatchSenderBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BatchSenderBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return protos.Batch.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BatchSender");
    }
  }

  private static final class BatchSenderFileDescriptorSupplier
      extends BatchSenderBaseDescriptorSupplier {
    BatchSenderFileDescriptorSupplier() {}
  }

  private static final class BatchSenderMethodDescriptorSupplier
      extends BatchSenderBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BatchSenderMethodDescriptorSupplier(String methodName) {
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
      synchronized (BatchSenderGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BatchSenderFileDescriptorSupplier())
              .addMethod(getSendBatchMethod())
              .build();
        }
      }
    }
    return result;
  }
}
