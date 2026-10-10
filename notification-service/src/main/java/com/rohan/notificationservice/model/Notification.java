package com.rohan.notificationservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String notificationId;

    private String orderId;
    private String customerEmail;
    private String customerName;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime sentAt;

    @Column(unique = true)
    private String idempotencyKey;

    public enum NotificationStatus {
        SENT, FAILED, SKIPPED_DUPLICATE
    }

    // ── Constructors ──────────────────────────────────────────────────────────
    public Notification() {}

    private Notification(String notificationId, String orderId, String customerEmail,
                         String customerName, String message, NotificationStatus status,
                         LocalDateTime sentAt, String idempotencyKey) {
        this.notificationId = notificationId;
        this.orderId        = orderId;
        this.customerEmail  = customerEmail;
        this.customerName   = customerName;
        this.message        = message;
        this.status         = status;
        this.sentAt         = sentAt != null ? sentAt : LocalDateTime.now();
        this.idempotencyKey = idempotencyKey;
    }

    // ── Static Builder ────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String notificationId;
        private String orderId;
        private String customerEmail;
        private String customerName;
        private String message;
        private NotificationStatus status;
        private LocalDateTime sentAt = LocalDateTime.now();
        private String idempotencyKey;

        public Builder notificationId(String v) { this.notificationId = v; return this; }
        public Builder orderId(String v)        { this.orderId = v;        return this; }
        public Builder customerEmail(String v)  { this.customerEmail = v;  return this; }
        public Builder customerName(String v)   { this.customerName = v;   return this; }
        public Builder message(String v)        { this.message = v;        return this; }
        public Builder status(NotificationStatus v) { this.status = v;     return this; }
        public Builder sentAt(LocalDateTime v)  { this.sentAt = v;         return this; }
        public Builder idempotencyKey(String v) { this.idempotencyKey = v; return this; }

        public Notification build() {
            return new Notification(notificationId, orderId, customerEmail,
                    customerName, message, status, sentAt, idempotencyKey);
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public String getNotificationId()              { return notificationId; }
    public void setNotificationId(String v)        { this.notificationId = v; }

    public String getOrderId()                     { return orderId; }
    public void setOrderId(String v)               { this.orderId = v; }

    public String getCustomerEmail()               { return customerEmail; }
    public void setCustomerEmail(String v)         { this.customerEmail = v; }

    public String getCustomerName()                { return customerName; }
    public void setCustomerName(String v)          { this.customerName = v; }

    public String getMessage()                     { return message; }
    public void setMessage(String v)               { this.message = v; }

    public NotificationStatus getStatus()          { return status; }
    public void setStatus(NotificationStatus v)    { this.status = v; }

    public LocalDateTime getSentAt()               { return sentAt; }
    public void setSentAt(LocalDateTime v)         { this.sentAt = v; }

    public String getIdempotencyKey()              { return idempotencyKey; }
    public void setIdempotencyKey(String v)        { this.idempotencyKey = v; }

    @Override
    public String toString() {
        return "Notification{orderId='" + orderId + "', status=" + status + "}";
    }
}
