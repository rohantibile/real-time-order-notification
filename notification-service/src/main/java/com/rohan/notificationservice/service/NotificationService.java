package com.rohan.notificationservice.service;

import com.rohan.notificationservice.model.Notification;
import com.rohan.notificationservice.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void processOrderNotification(OrderEvent event) {
        String idempotencyKey = event.getOrderId() + "_" + event.getStatus();

        // Idempotency check — skip duplicates safely
        if (notificationRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
            log.warn("Duplicate event detected. Skipping. Key: {}", idempotencyKey);
            return;
        }

        String message = buildMessage(event);
        sendNotification(event.getCustomerEmail(), message);

        Notification notification = Notification.builder()
                .orderId(event.getOrderId())
                .customerEmail(event.getCustomerEmail())
                .customerName(event.getCustomerName())
                .message(message)
                .status(Notification.NotificationStatus.SENT)
                .idempotencyKey(idempotencyKey)
                .build();

        notificationRepository.save(notification);
        log.info("Notification saved for orderId: {}", event.getOrderId());
    }

    private String buildMessage(OrderEvent event) {
        return switch (event.getStatus()) {
            case "PLACED" -> String.format(
                "Dear %s, your order for '%s' (Qty: %d) has been placed! Order ID: %s",
                event.getCustomerName(), event.getProductName(),
                event.getQuantity(), event.getOrderId());
            case "CONFIRMED" -> String.format(
                "Good news %s! Your order %s has been confirmed.",
                event.getCustomerName(), event.getOrderId());
            case "SHIPPED" -> String.format(
                "Your order %s has been shipped!", event.getOrderId());
            case "DELIVERED" -> String.format(
                "Your order %s has been delivered. Thank you!", event.getOrderId());
            case "CANCELLED" -> String.format(
                "Your order %s has been cancelled. Refund in 3-5 days.", event.getOrderId());
            default -> String.format("Order %s status updated to: %s",
                event.getOrderId(), event.getStatus());
        };
    }

    private void sendNotification(String email, String message) {
        // In production: integrate JavaMailSender / AWS SNS / Twilio
        log.info("Sending notification to: {} | Message: {}", email, message);
    }
}
