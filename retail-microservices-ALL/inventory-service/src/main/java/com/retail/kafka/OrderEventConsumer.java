package com.retail.kafka;

import com.retail.event.OrderCreatedEvent;
import com.retail.entity.Inventory;
import com.retail.repository.InventoryRepository;

import jakarta.transaction.Transactional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private final InventoryRepository inventoryRepository;

    public OrderEventConsumer(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    		//, containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consume(OrderCreatedEvent event) {
    	System.out.println("📦 Inventory received: " + event);
        Inventory inventory = inventoryRepository
                .findById(event.getProductId())
                .orElseThrow(() -> new RuntimeException("Inventory not found !!!!!!!!!!!!!"));

        inventory.setStock(
                inventory.getStock() - event.getQuantity()
        );

        inventoryRepository.save(inventory);
    }
}
