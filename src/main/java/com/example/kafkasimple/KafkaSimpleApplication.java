package com.example.kafkasimple;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class KafkaSimpleApplication {
    static ConfigurableApplicationContext context;
    public static void main(String[] args) throws InterruptedException {
        context = SpringApplication.run(KafkaSimpleApplication.class, args);
        MessageProducer producer = context.getBean(MessageProducer.class);
        MessageListener listener = context.getBean(MessageListener.class);

        producer.sendMessage("Hello, World!");
        for(int i=0;i<10;i++)
        producer.sendMessage("Hello "+i);

        listener.latch.await(10, TimeUnit.SECONDS);
        context.close();

    }

    @Bean
    public MessageProducer messageProducer() {
        return new MessageProducer();
    }

    public static class MessageProducer {

        @Autowired
        private KafkaTemplate<String, String> kafkaTemplate;
/*

        @Autowired
        private KafkaTemplate<String, Greeting> greetingKafkaTemplate;
*/


        @Value(value = "${message.topic.name}")
        private String topicName;

        public void sendMessage(String message) {

            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);

            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    System.out.println("Sent message=[" + message + "] to topic " + topicName + " with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
                }

                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
                }
            });
        }

    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }

    public static class MessageListener {
        private CountDownLatch latch = new CountDownLatch(3);

        @KafkaListener(topics = "${message.topic.name}")
        public void listenGroupFoo(String message) {
            System.out.println("Received Message in topic "+ context.getEnvironment().getProperty("message.topic.name")+" : " + message);
            latch.countDown();
        }

        @KafkaListener(topics = "${message.topic.name}")
        public void listenWithHeaders(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
            System.out.println(
                "Received Message: " + message + "from partition: " + partition);
        }

        /*
               @KafkaListener(
                   topicPartitions = @TopicPartition(topic = "topicName",
                       partitionOffsets = {
                           @PartitionOffset(partition = "0", initialOffset = "0"),
                           @PartitionOffset(partition = "3", initialOffset = "0")}),
                   containerFactory = "partitionsKafkaListenerContainerFactory")
               public void listenToPartition(
                   @Payload String message,
                   @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
                   System.out.println(
                       "Received Message: " + message + "from partition: " + partition);
               }
       */
        @KafkaListener(
            topics = "${message.topic.name}",
            containerFactory = "filterKafkaListenerContainerFactory")
        public void listenWithFilter(String message) {
            System.out.println("Received Message in filtered listener: " + message);
        }

        @KafkaListener(topics = "cdataTwd")
        public void receive(ConsumerRecord<?, ?> consumerRecord) {
            log.info("received payload='{}'", consumerRecord.toString());
            latch.countDown();
        }

    }


}
