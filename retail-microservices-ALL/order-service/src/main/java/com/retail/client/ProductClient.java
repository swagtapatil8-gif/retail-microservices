package com.retail.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.retail.dto.ProductDto;

@FeignClient(name = "product-service", url = "${PRODUCT_SERVICE_URL}")
public interface ProductClient {

 // Feign calls Product Service synchronously
 @GetMapping("/products/{id}")
 ProductDto getProduct(@PathVariable("id") Long id);
}
