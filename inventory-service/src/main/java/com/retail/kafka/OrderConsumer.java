package com.retail.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

 // Consumes ORDER events asynchronously
 @KafkaListener(topics = "order-events")
 public void consume(String orderId) {
  // Reduce inventory here
 }
}
