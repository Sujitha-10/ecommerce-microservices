package com.ecommerce.orderservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.orderservice.config.FeignConfig;
import com.ecommerce.orderservice.dto.Product;


@FeignClient(
        name = "product-service",
        configuration = FeignConfig.class
)
public interface ProductClient {


    @GetMapping("/products/{id}")
    Product getProductById(
            @PathVariable Long id
    );


    @PutMapping("/products/{id}/reduce-stock")
    Product reduceStock(
            @PathVariable Long id,
            @RequestParam int quantity
    );

}