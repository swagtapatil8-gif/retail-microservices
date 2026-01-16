package com.retail.entity;

import jakarta.persistence.*;

@Entity
public class Inventory {

    @Id
    private Long productId;

    private int stock;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

    // getters & setters
    
}
