package com.retail.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
