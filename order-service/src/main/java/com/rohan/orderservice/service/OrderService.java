package com.rohan.orderservice.service;

import com.rohan.orderservice.model.Order;
import com.rohan.orderservice.model.OrderEvent;
import com.rohan.orderservice.producer.OrderProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    public OrderService(OrderRepository orderRepository, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderProducer   = orderProducer;
    }

    @Transactional
    public Order placeOrder(Order order) {
        log.info("Placing order for customer: {}", order.getCustomerEmail());
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved with orderId: {}", savedOrder.getOrderId());
        orderProducer.publishOrderEvent(OrderEvent.fromOrder(savedOrder));
        return savedOrder;
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order updateOrderStatus(String orderId, Order.OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        orderProducer.publishOrderEvent(OrderEvent.fromOrder(updated));
        return updated;
    }
}
