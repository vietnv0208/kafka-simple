package com.example.kafkasimple.config;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "multiGroup", topics = "multitype")
public class MultiTypeKafkaListener {

    @KafkaHandler
    public void handleGreeting(Greeting greeting) {
        System.out.println("Greeting received: " + greeting);
    }

    @KafkaHandler
    public void handleF(Farewell farewell) {
        System.out.println("Farewell received: " + farewell);
    }

    @KafkaHandler
    public void handleS(String object) {
        System.out.println("String type received: " + object);
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Unkown type received: " + object);
    }

}
