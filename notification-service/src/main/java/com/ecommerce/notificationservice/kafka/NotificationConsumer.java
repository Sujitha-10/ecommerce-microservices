package com.ecommerce.notificationservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecommerce.notificationservice.dto.OrderEvent;

@Service
public class NotificationConsumer {
	
	public NotificationConsumer() {
        System.out.println("Notification Consumer Bean Created");
    }


    @KafkaListener(
            topics = "order-created",
            groupId = "notification-group"
    )
    public void consume(OrderEvent event) {

        System.out.println("==================================");
        System.out.println("Order Received");
        System.out.println("Order ID : " + event.getOrderId());
        System.out.println("Product ID : " + event.getProductId());
        System.out.println("Quantity : " + event.getQuantity());
        System.out.println("Notification Sent Successfully");
        System.out.println("==================================");
    }
}