package com.denizkpln.order_service.service;


import com.example.grpccommon.*;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class TransactionGrpcService {

    @GrpcClient(value = "grpc-devproblems-service")
    private TransactionServiceGrpc.TransactionServiceBlockingStub transactionServiceBlockingStub;

    @GrpcClient(value = "grpc-devproblems-service")
    private TransactionServiceGrpc.TransactionServiceStub transactionServiceStub;


    public void streamTransactions(String accountNumber, int durationInDays) {
        AccountRequest request = AccountRequest.newBuilder().setAccountNumber(accountNumber)
                .setDurationInDays(durationInDays).build();


        try {
            transactionServiceStub.streamTransactions(request, new StreamObserver<TransactionDetailList>() {
                @Override
                public void onNext(TransactionDetailList transactionDetail) {
                    // Handle each incoming TransactionDetail here
                    System.out.println("Received transaction detail: " + transactionDetail);
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error occurred during transaction streaming: " + throwable);
                }

                @Override
                public void onCompleted() {
                    System.out.println("Transaction streaming completed");
                }
            });
        }catch (StatusRuntimeException ex){
            Status status = ex.getStatus();
            System.out.println("error code -" + status.getCode());
            System.out.println("error description -" + status.getDescription());
            System.out.println("error cause -" + status.getCause());

            Metadata metadata = Status.trailersFromThrowable(ex);
            Metadata.Key<CustomError> customErrorKey = ProtoUtils.keyForProto(CustomError.getDefaultInstance());
            CustomError customError = metadata.get(customErrorKey);
            System.out.println(customError);
        }

    }


}
