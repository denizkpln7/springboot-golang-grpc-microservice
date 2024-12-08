package com.denizkpln.order_service.service;

import com.denizkpln.order_service.model.Order;
import com.denizkpln.order_service.repository.OrderRepository;
import com.google.protobuf.Descriptors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderGrpcService orderGrpcService;


    public Order create(Order order) {
        Order save = orderRepository.save(order);
        orderGrpcService.savePayment(order);
        return save;
    }

    public Order maxBalance() throws InterruptedException {
        Map<String, Map<Descriptors.FieldDescriptor, Object>> expensiveOrder = orderGrpcService.getExpensiveOrder();
        return null;
    }

    public Order getbyuserId() throws InterruptedException {
        List<Map<Descriptors.FieldDescriptor, Object>> byIdUserOrder = orderGrpcService.getByIdUserOrder(1L);
        return null;
    }

    public Order getbyuserIdMax() throws InterruptedException {
        List<Map<Descriptors.FieldDescriptor, Object>> savePayments = orderGrpcService.getSavePayments();
        return null;
    }
}
