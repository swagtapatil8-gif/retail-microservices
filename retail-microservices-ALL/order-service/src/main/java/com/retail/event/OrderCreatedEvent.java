package com.retail.event;

/*
 This is the message that will be sent to Kafka
 */
public class OrderCreatedEvent {

    private Long orderId;
    private Long productId;
    private int quantity;

    // getters & setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
