package com.ecommerce.orderservice.feign;

import org.springframework.stereotype.Component;

import com.ecommerce.orderservice.dto.Product;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public Product getProductById(Long id) {
    	
        Product product = new Product();

        product.setId(id);
        product.setName("Product service unavailable");
        product.setPrice(0);

        return product;
    }
    
    @Override
    public Product reduceStock(Long id, int quantity) {

        Product product = new Product();
        product.setId(id);
        product.setName("Product service unavailable");

        return product;
    }
}