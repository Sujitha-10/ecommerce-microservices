package com.ecommerce.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.exception.InsufficientStockException;

@Service
public class ProductService {

	private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->  new ProductNotFoundException("Product not found with id: " + id));
    }
    
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }
    
    public Product reduceStock(Long id, int quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        if(product.getQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock available");
        }

        product.setQuantity(product.getQuantity() - quantity);

        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
    }
}