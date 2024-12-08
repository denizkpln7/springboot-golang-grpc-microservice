package com.denizkpln.payment_service.service;

import com.denizkpln.payment_service.dto.ProductDto;
import com.denizkpln.payment_service.model.PaymentItem;
import com.example.grpccommon.*;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class PaymentGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    private final PaymentService paymentService;

    private final RandomGenerator m_randomGenerator;


    @Override
    public StreamObserver<Payment> getByUsersMaxOrder(StreamObserver<SavePayment> responseObserver) {
        List<SavePayment> savePaymentsReturn = new ArrayList<>();
        List<SavePayment> savePayments = getAll();

        return new StreamObserver<Payment>() {
            @Override
            public void onNext(Payment payment) {
                savePayments.stream()
                        .filter(savePayment -> savePayment.getUserId() == payment.getUserId())
                        .forEach(savePaymentsReturn::add);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                savePayments.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getByUserIdMaxOrder(Payment request, StreamObserver<SavePayment> responseObserver) {
        List<SavePayment> savePayments = new ArrayList<>();
        savePayments.add(SavePayment.newBuilder().setUserId(1L).setBalance(55).build());
        savePayments.stream().filter(order -> order.getUserId() == request.getUserId()).forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Payment> saveAndMaxBalanceOrder(StreamObserver<SavePayment> responseObserver) {
        return new StreamObserver<Payment>() {
            SavePayment savePayment = null;
            Integer maxBalance = 0;

            @Override
            public void onNext(Payment payment) {
                if (payment.getBalance() > maxBalance) {
                    maxBalance = payment.getBalance();
                    savePayment = SavePayment.newBuilder()
                            .setUserId(payment.getUserId())
                            .addAllProductId(payment.getProductIdList())
                            .setBalance(maxBalance)
                            .build();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(savePayment);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void savePaymentData(Payment request, StreamObserver<SavePayment> responseObserver) {
        PaymentItem paymentItem = PaymentItem.builder()
                .balance(request.getBalance())
                .productId(request.getProductIdList())
                .userId(request.getUserId())
                .build();
        PaymentItem payment = paymentService.createPayment(paymentItem);
        SavePayment savePayment = SavePayment.newBuilder()
                .setId(payment.getId())
                .setBalance(payment.getBalance())
                .addAllProductId(payment.getProductId())
                .setUserId(payment.getUserId())
                .build();

        responseObserver.onNext(savePayment);
        responseObserver.onCompleted();
    }


    private static List<SavePayment> getAll() {
        List<SavePayment> exampleList = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 10; i++) {
            long id = i;
            List<Long> productIds = Arrays.asList(
                    random.nextLong(1000),
                    random.nextLong(1000),
                    random.nextLong(1000)
            );
            long userId = random.nextLong(1000);
            int balance = random.nextInt(1000);

            SavePayment example = SavePayment.newBuilder()
                    .setId(id)
                    .setUserId(userId)
                    .setBalance(balance)
                    .addAllProductId(productIds)
                    .build();
            exampleList.add(example);
        }
        return exampleList;
    }


    ////grpc golang
    @Override
    public StreamObserver<Product> saveProductsData(StreamObserver<SaveProduct> responseObserver) {
        return new StreamObserver<Product>() {

            SaveProduct saveProduct = null;
            Integer maxBalance = 0;

            @Override
            public void onNext(Product product) {
                if (product.getBalance() > maxBalance) {
                    maxBalance = product.getBalance();
                    saveProduct = SaveProduct.newBuilder()
                            .setBalance(product.getBalance())
                            .setTitle(product.getTitle())
                            .setId(10L)
                            .build();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(saveProduct);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void saveProduct(Product request, StreamObserver<SaveProduct> responseObserver) {
        ProductDto productDtoItem = ProductDto.builder()
                .Balance(request.getBalance())
                .Title(request.getTitle())
                .build();

        log.info("Saving product {}", productDtoItem);


        SaveProduct saveProduct = SaveProduct.newBuilder()
                .setId(1L)
                .setBalance(productDtoItem.getBalance())
                .setTitle(request.getTitle())
                .build();

        responseObserver.onNext(saveProduct);
        responseObserver.onCompleted();
    }


    @Override
    public void saveProductsDataAll(Product request, StreamObserver<SaveProduct> responseObserver) {
        List<SaveProduct> saveProducts = new ArrayList<>();
        saveProducts.add(SaveProduct.newBuilder().setTitle(request.getTitle()).setBalance(55).build());
        saveProducts.forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Product> saveProductDataAllBack(StreamObserver<SaveProduct> responseObserver) {
        List<SaveProduct> saveProductsReturn = new ArrayList<>();
        List<SaveProduct> saveProduct = getAllProduct();

        return new StreamObserver<Product>() {
            @Override
            public void onNext(Product product) {
                saveProduct.stream().filter(sp -> sp.getTitle().equals(product.getTitle())).forEach(saveProductsReturn::add);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                saveProductsReturn.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }

    private List<SaveProduct> getAllProduct() {
        List<SaveProduct> saveProducts = new ArrayList<>();
        saveProducts.add(SaveProduct.newBuilder().setTitle("admin1").setBalance(15).setId(1L).build());
        saveProducts.add(SaveProduct.newBuilder().setTitle("deneme2").setBalance(25).setId(2L).build());
        saveProducts.add(SaveProduct.newBuilder().setTitle("deneme3").setBalance(35).setId(3L).build());
        return saveProducts;
    }


    @Override
    public void generateTextsFile(TextFileGenerateInfo request, StreamObserver<Chunk> responseObserver) {
        var n = request.getN();
        var count = request.getCount();
        var fileName = request.getFileName();

        log.info("N:{}, Count:{}, File Name:{}", n, count, fileName);


        if (!createTextsFileEN(n, count, fileName, responseObserver)) //Burada demo olarak fileName doğrudan verilmiştir
            return;


        if (!sendFile(fileName, responseObserver))
            return;

        responseObserver.onCompleted();
    }

    private boolean createTextsFileEN(int n, int count, String fileName, StreamObserver<Chunk> responseObserver)
    {
        var path = Path.of(fileName);

        if (Files.exists(path)) {

            return false;
        }

        var result = true;

        try (var bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            while (n-- > 0){
                byte[] array = new byte[7]; // length is bounded by 7
                new Random().nextBytes(array);
                String generatedString = new String(array, Charset.forName("UTF-8"));
                bw.write("%s\r\n".formatted(generatedString));
            }
        }
        catch (IOException ex) {
            result = false;
        }

        return result;

    }

    private boolean sendFile(String fileName, StreamObserver<Chunk> responseObserver)
    {
        var result = true;
        var data = new byte[1024];

        try (var bis = new BufferedInputStream(new FileInputStream(fileName))) {
            int read;

            while ((read = bis.read(data)) > 0)
                responseObserver.onNext(Chunk.newBuilder().setData(ByteString.copyFrom(data, 0, read))
                        .setSize(read).build());
        }
        catch (IOException ex) {
            result = false;
        }

        return result;
    }
}
