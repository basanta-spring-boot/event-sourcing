package com.javatechie.repository;

import com.javatechie.entity.ShippingEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShippingEventRepository extends MongoRepository<ShippingEvent,String> {
}
