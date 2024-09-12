# event-sourcing

Microservice event sourcing is a pattern where state changes are logged as a sequence of immutable events instead of being stored directly in the database. When applied to Spring Boot, the microservices become more resilient and scalable by focusing on events as the primary source of truth.

Use Case: Online Order Management
Let’s assume we are building an Online Order Management system with microservices, and each order goes through multiple stages like:

Order Created
Order Confirmed
Order Shipped
Order Delivered
Instead of saving the current state of the order, we will log events at every state change.

3. Event Bus for Communication
Order Service emits CREATED and CONFIRMED events to the Kafka topic order-events.
Shipping Service listens for CONFIRMED events on order-events and ships the order.
Shipping Service then emits SHIPPED and DELIVERED events to the Kafka topic shipping-events.
4. Real-Time State Transitions and Asynchronous Communication
Here's how the communication flows between services in real time:

Order Service creates an order (CREATED event).

Order Service emits the CREATED event.
Other services (e.g., billing, inventory) can listen for this event if needed.
Order Service confirms the order (CONFIRMED event).

Order Service emits the CONFIRMED event.
Shipping Service listens to CONFIRMED and triggers the shipping process.
Shipping Service ships the order (SHIPPED event).

Shipping Service emits the SHIPPED event.
Other services (like a notification service) can listen to this event.
Shipping Service delivers the order (DELIVERED event).

Shipping Service emits the DELIVERED event.
The user or the order system updates the status to "Delivered."
Each service (e.g., Order, Shipping, Billing, etc.) operates independently, managing its own events and subscribing to others as needed. This keeps each service focused on its own responsibility and avoids tight coupling between different domains.



## Open Source Kafka Startup in local ##

1. Start Zookeeper Server

    ```sh bin/zookeeper-server-start.sh config/zookeeper.properties```

2. Start Kafka Server / Broker

    ```sh bin/kafka-server-start.sh config/server.properties```

3. Create topic

    ```sh bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic NewTopic --partitions 3 --replication-factor 1```

## cURL to place an Order Service ##

```
curl --location 'http://localhost:9191/api/orders/place' \
--header 'Content-Type: application/json' \
--data '{
    "orderId": "12345",
    "name": "Laptop",
    "qty": 2,
    "price": 1200.50,
    "userId": "user789"
}'
```

## cURL to confirm an Order Service ##

```
curl --location --request PUT 'http://localhost:9191/api/orders/confirm/5a2c854b' \
--data ''
```

## cURL to update deliver status of an Order Service ##

```
curl --location --request POST 'http://localhost:9292/shipping/5a2c854b/deliver' \
--data ''
```

