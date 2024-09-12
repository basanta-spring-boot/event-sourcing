package com.javatechie.service;

import com.javatechie.dto.enums.OrderStatus;
import com.javatechie.dto.request.OrderRequest;
import com.javatechie.dto.response.OrderResponse;
import com.javatechie.entity.OrderEvent;
import com.javatechie.publisher.KafkaOrderEventPublisher;
import com.javatechie.repository.OrderEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderEventRepository repository;

    @Autowired
    private KafkaOrderEventPublisher eventPublisher;

    // Handle order creation
    public OrderResponse placeAnOrder(OrderRequest orderRequest) {
        String orderId = UUID.randomUUID().toString().split("-")[0];
        orderRequest.setOrderId(orderId);
        //do request validation and real business logic
        OrderEvent event = new OrderEvent(orderId, OrderStatus.CREATED, "Order created successfully.", LocalDateTime.now());
        saveAndPublishOrderEvent(event);
        return new OrderResponse(orderId, OrderStatus.CREATED);
    }

    // Handle order confirmation
    public OrderResponse confirmOrder(String orderId) {
        OrderEvent event = new OrderEvent(orderId, OrderStatus.CONFIRMED, "Order confirmed successfully.", LocalDateTime.now());
        saveAndPublishOrderEvent(event);
        return new OrderResponse(orderId, OrderStatus.CONFIRMED);
    }

    private void saveAndPublishOrderEvent(OrderEvent event) {
        // Save and publish order event
        repository.save(event);
        eventPublisher.sendOrderEvent(event);
    }
}
