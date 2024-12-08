package com.denizkpln.order_service.service;


import com.denizkpln.order_service.model.Order;
import com.example.grpccommon.*;
import com.google.protobuf.Descriptors;
import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderGrpcService {


    @GrpcClient(value = "grpc-devproblems-service")
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    @GrpcClient(value = "grpc-devproblems-service")
    private OrderServiceGrpc.OrderServiceStub orderServiceStub;

    public SavePayment savePayment(Order order) {
        Payment payment = Payment.newBuilder()
                .setBalance(order.getBalance())
                .setUserId(order.getUserId())
                .addAllProductId(order.getProductId())
                .build();
        SavePayment savePayment = orderServiceBlockingStub.savePaymentData(payment);
        return savePayment;
    }

    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getExpensiveOrder() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Map<String, Map<Descriptors.FieldDescriptor, Object>> response = new HashMap<>();
        StreamObserver<Payment> responseObserver = orderServiceStub.withDeadline(Deadline.after(2, TimeUnit.SECONDS)).saveAndMaxBalanceOrder(new StreamObserver<SavePayment>() {

            @Override
            public void onNext(SavePayment payment) {
                response.put("MaxBalance",payment.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        getPayment().forEach(responseObserver::onNext);
        responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyMap();
    }


    public static List<Payment> getPayment() {
        return new ArrayList<Payment>() {
            List<Long> list = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            {
                add(Payment.newBuilder().addAllProductId(list).setUserId(2L).setBalance(50).build());
                add(Payment.newBuilder().addAllProductId(list).setUserId(3L).setBalance(55).build());
                add(Payment.newBuilder().addAllProductId(list).setUserId(4L).setBalance(60).build());
            }
        };
    }


    public List<Map<Descriptors.FieldDescriptor, Object>> getByIdUserOrder(Long userId) throws InterruptedException {
        final Payment paymentRequest = Payment.newBuilder().setUserId(userId).setBalance(100).build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        orderServiceStub.withDeadline(Deadline.after(2, TimeUnit.SECONDS)).getByUserIdMaxOrder(paymentRequest, new StreamObserver<SavePayment>() {

            @Override
            public void onNext(SavePayment payment) {
                response.add(payment.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getSavePayments() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        StreamObserver<Payment> responseObserver = orderServiceStub.withDeadline(Deadline.after(2, TimeUnit.SECONDS)).getByUsersMaxOrder(new StreamObserver<SavePayment>() {

            @Override
            public void onNext(SavePayment payment) {
                response.add(payment.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });


        List<Payment> list =getAll();
        list.forEach(responseObserver::onNext);
        responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }


    private static List<Payment> getAll(){
        List<Payment> exampleList = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 50; i++) {

            List<Long> productIds = Arrays.asList(
                    random.nextLong(1000),
                    random.nextLong(1000),
                    random.nextLong(1000)
            );
            long userId = random.nextLong(1000);
            int balance = random.nextInt(1000);

            Payment example = Payment.newBuilder()
                    .setUserId(userId)
                    .setBalance(balance)
                    .addAllProductId(productIds)
                    .build();
            exampleList.add(example);
        }
        return exampleList;
    }

    private void saveDataCallback(Chunk chunk, BufferedOutputStream bos)
    {
        try {
            log.info("Size:{}", chunk.getSize());
            bos.write(chunk.getData().toByteArray());
        }
        catch (IOException ex) {
            log.error("IO Error: {}", ex.getMessage());
        }
    }

    public void generateTexts(int count, int n, String fileName)
    {
        var info = TextFileGenerateInfo.newBuilder()
                .setCount(count)
                .setN(n).setFileName(fileName)
                .build();

        try (var bos = new BufferedOutputStream(new FileOutputStream(fileName))){
            var chunks = orderServiceBlockingStub.generateTextsFile(info);

            chunks.forEachRemaining(c -> saveDataCallback(c, bos));
        }
        catch (IOException ex) {
            log.error("IO Error: {}", ex.getMessage());
        }
        catch (StatusRuntimeException ex) {
            log.error("Error:{}, {}", ex.getStatus(), ex.getMessage());
        }
        catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
        }
    }




}