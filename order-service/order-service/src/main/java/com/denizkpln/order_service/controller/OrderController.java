package com.denizkpln.order_service.controller;

import com.denizkpln.order_service.model.Order;
import com.denizkpln.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return new ResponseEntity<>(orderService.create(order), HttpStatus.CREATED);
    }

    @PostMapping("/maxBalance")
    public ResponseEntity<Order> maxBalance() throws InterruptedException {
        return new ResponseEntity<>(orderService.maxBalance(), HttpStatus.OK);
    }

    @PostMapping("/getbyuserid")
    public ResponseEntity<Order> getbyuserId() throws InterruptedException {
        return new ResponseEntity<>(orderService.getbyuserId(), HttpStatus.OK);
    }

    @PostMapping("/getbyuseridmax")
    public ResponseEntity<Order> getbyuserIdMax() throws InterruptedException {
        return new ResponseEntity<>(orderService.getbyuserIdMax(), HttpStatus.OK);
    }
}
