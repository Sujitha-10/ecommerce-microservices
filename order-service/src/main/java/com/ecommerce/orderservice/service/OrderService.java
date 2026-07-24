package com.ecommerce.orderservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.orderservice.dto.OrderEvent;
import com.ecommerce.orderservice.dto.Product;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.exception.InsufficientStockException;
import com.ecommerce.orderservice.exception.ProductNotFoundException;
import com.ecommerce.orderservice.exception.ProductServiceUnavailableException;
import com.ecommerce.orderservice.feign.ProductClient;
import com.ecommerce.orderservice.kafka.OrderProducer;
import com.ecommerce.orderservice.repository.OrderRepository;

import feign.FeignException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderProducer orderProducer;

    public OrderService(OrderRepository orderRepository,
                        ProductClient productClient,
                        OrderProducer orderProducer) {

        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.orderProducer = orderProducer;
    }

    public Order saveOrder(Order order) {

        Product product;

        try {

            product = productClient.getProductById(order.getProductId());

        } catch (FeignException e) {

            if (e.status() == 404) {

                throw new ProductNotFoundException(
                        "Product not found with id: " + order.getProductId());
            }

            throw new ProductServiceUnavailableException(
                    "Product service is currently unavailable");
        } 

        if (product.getQuantity() < order.getQuantity()) {

            throw new InsufficientStockException(
                    "Insufficient stock available");
        }

        // Reduce stock in Product Service
        productClient.reduceStock(order.getProductId(), order.getQuantity());

        // Save Order
        Order savedOrder = orderRepository.save(order);

        // Publish Kafka Event
        OrderEvent event = new OrderEvent(
                savedOrder.getId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity());

        orderProducer.sendOrder(event);

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}