package com.javatechie.service;

import com.javatechie.dto.enums.OrderStatus;
import com.javatechie.entity.OrderEvent;
import com.javatechie.entity.ShippingEvent;
import com.javatechie.repository.ShippingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShippingEventService {

    @Autowired
    private KafkaTemplate<String, ShippingEvent> kafkaTemplate;

    @Autowired
    private ShippingEventRepository repository;

    @Value("${shipping.event.topicName}")
    private String topicName;

    // Consume Order Events and trigger shipping
    @KafkaListener(topics = "order-events", groupId = "shipping-service")
    public void consumeOrderEvent(OrderEvent orderEvent) {
        if (orderEvent.getStatus().equals(OrderStatus.CONFIRMED)) {
            // Automatically ship after order confirmation
            shipOrder(orderEvent.getOrderId());
        }
    }

    // Ship the order
    public void shipOrder(String orderId) {
        ShippingEvent event = new ShippingEvent(orderId, OrderStatus.SHIPPED, "Order shipped successfully.", LocalDateTime.now());
        saveAndPublishShippingEvent(event);
    }

    // Deliver the order
    public void deliverOrder(String orderId) {
        ShippingEvent event = new ShippingEvent(orderId, OrderStatus.DELIVERED, "Order delivered successfully.",LocalDateTime.now());
        saveAndPublishShippingEvent(event);
    }

    private void saveAndPublishShippingEvent(ShippingEvent event) {
        // Save and publish shipping event
        repository.save(event);
        kafkaTemplate.send(topicName, event.getOrderId(), event);
    }
}
