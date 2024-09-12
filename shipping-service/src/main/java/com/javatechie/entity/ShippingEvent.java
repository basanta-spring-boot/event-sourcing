package com.javatechie.entity;

import com.javatechie.dto.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "OrderEvents")
public class ShippingEvent {
    private String id;
    private String orderId;
    private OrderStatus status;  // SHIPPED, DELIVERED
    private String details;
    private LocalDateTime eventTimestamp;

    public ShippingEvent(String orderId, OrderStatus status, String details, LocalDateTime eventTimestamp) {
        this.orderId = orderId;
        this.status = status;
        this.details = details;
        this.eventTimestamp = eventTimestamp;
    }
}
