package com.javatechie.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Value("${order.event.topicName}")
    private String topicName;

    @Bean
    public NewTopic createTopic(){
        return new NewTopic(topicName,3,(short)1);
    }
}