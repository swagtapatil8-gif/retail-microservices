package com.retail.service;

import com.retail.client.ProductClient;
import com.retail.dto.CreateOrderRequest;
import com.retail.dto.ProductDto;
import com.retail.entity.Order;
import com.retail.event.OrderCreatedEvent;
import com.retail.kafka.OrderEventProducer;
import com.retail.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
	
	private static final String PRODUCT_CB = "productService";

    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;

    public OrderService(ProductClient productClient,
                        OrderRepository orderRepository,
                        OrderEventProducer eventProducer) {
        this.productClient = productClient;
        this.orderRepository = orderRepository;
        this.eventProducer= eventProducer;
    }
    @CircuitBreaker(name = PRODUCT_CB, fallbackMethod = "productFallback")
    @Retry(name = PRODUCT_CB)
    public Order createOrder(CreateOrderRequest request) {

        // 1. Call product-service using Feign
        ProductDto product = productClient.getProduct(request.getProductId());

        // 2. Basic validation
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // 3. Create order
        Order order = new Order();
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setStatus("CREATED");

        // 4. Save order
        Order savedOrder = orderRepository.save(order);

        // 🔥 Publish Kafka Event
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(savedOrder.getId());
        event.setProductId(savedOrder.getProductId());
        event.setQuantity(savedOrder.getQuantity());

        eventProducer.publishOrderCreatedEvent(event);

        return savedOrder;
    }
    /**
     * Fallback method is called when:
     * - Product service is down
     * - Circuit is open
     * - Retries are exhausted
     */
    public Order productFallback(CreateOrderRequest request, Throwable ex) {
        throw new RuntimeException(
                "Product service unavailable. Please try again later."
        );
    }
}
