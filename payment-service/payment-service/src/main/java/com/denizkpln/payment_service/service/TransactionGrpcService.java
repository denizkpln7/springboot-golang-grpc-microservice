package com.denizkpln.payment_service.service;

import com.denizkpln.payment_service.exception.MyCustomError;
import com.denizkpln.payment_service.model.Transaction;
import com.example.grpccommon.*;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;

import java.util.List;

import static com.denizkpln.payment_service.utils.Utility.createTransactionDetailFromTransaction;
import static com.denizkpln.payment_service.utils.Utility.fetchTransactions;

public class TransactionGrpcService extends TransactionServiceGrpc.TransactionServiceImplBase {

    @Override
    public void streamTransactions(AccountRequest request, StreamObserver<TransactionDetailList> responseObserver) {
        List<Transaction> transactions = fetchTransactions();
        int batchSize = 3;

        if (batchSize < 2) {
            Metadata metadata = new Metadata();
            Metadata.Key<CustomError> customError = ProtoUtils.keyForProto(CustomError.getDefaultInstance());
            metadata.put(customError, CustomError.newBuilder().setMessage("Database Error- Connection Refused.")
                    .setErrorType("CONNECTION_REFUSED").build());
            var statusRuntimeException = Status.NOT_FOUND
                    .withDescription("The requested Account Number cannot be found.").asRuntimeException(metadata);
            responseObserver.onError(statusRuntimeException);
            return;
        }

        for (int i = 0; i < transactions.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, transactions.size());
            List<Transaction> batchTransactions = transactions.subList(i, endIndex);

            TransactionDetailList.Builder transactionDetailListBuilder = TransactionDetailList.newBuilder();

            for (Transaction transaction : batchTransactions) {
                TransactionDetail transactionDetail = createTransactionDetailFromTransaction(transaction);
                transactionDetailListBuilder.addTransactionDetails(transactionDetail);
            }
            TransactionDetailList transactionDetailList = transactionDetailListBuilder.build();

            responseObserver.onNext(transactionDetailList);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                responseObserver.onError(e);
                return;
            }
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ChatMessage> startChat(StreamObserver<ChatResponse> responseObserver) {
        return super.startChat(responseObserver);
    }
}
