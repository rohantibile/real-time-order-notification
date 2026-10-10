package com.rohan.notificationservice.consumer;

import com.rohan.notificationservice.model.OrderEvent;
import com.rohan.notificationservice.service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    private final NotificationService notificationService;

    public OrderConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
        topics = "order-events",
        groupId = "notification-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderEvent(
            ConsumerRecord<String, OrderEvent> record,
            Acknowledgment acknowledgment) {

        log.info("Received event. Topic: {}, Partition: {}, Offset: {}",
                record.topic(), record.partition(), record.offset());

        OrderEvent event = record.value();
        log.info("Processing orderId: {}, status: {}", event.getOrderId(), event.getStatus());

        try {
            notificationService.processOrderNotification(event);
            // Commit offset only after successful processing
            acknowledgment.acknowledge();
            log.info("Event acknowledged. OrderId: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("Error processing orderId: {}. Error: {}",
                    event.getOrderId(), e.getMessage(), e);
            // Do NOT acknowledge — Kafka will redeliver
        }
    }
}
