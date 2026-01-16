package com.retail.config;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import jakarta.annotation.PostConstruct;

@Configuration
public class KafkaConsumerConfig {

	  // Listener container factory
/*	@Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            DefaultErrorHandler errorHandler) {
    	 System.out.println("ConcurrentKafkaListenerContainerFactory");
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        // 🔥 THIS LINE ENABLES DLT
        factory.setCommonErrorHandler(errorHandler);
        System.out.println("THIS LINE ENABLES DLT");

        return factory;
    } */
    
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> kafkaTemplate) {
    	System.out.println("Inside errorHandler ");
        // Retry 3 times with 2 sec gap
        FixedBackOff backOff = new FixedBackOff(2000L, 3);
        
        // After retries → send to DLQ
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate,
                        (record, ex) -> {
                        	System.out.println("🚨 SENDING TO DLT: " + record.value());
                              return  new TopicPartition("order-created-dlt", record.partition());
                                }
    );

        return new DefaultErrorHandler(recoverer, backOff);
    }
    @PostConstruct
    public void log() {
        System.out.println("🔥 DLQ CONFIG ACTIVE");
    }
}
