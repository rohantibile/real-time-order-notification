package com.rohan.orderservice.model;

import java.time.LocalDateTime;

public class OrderEvent {

    private String orderId;
    private String customerName;
    private String customerEmail;
    private String productName;
    private Integer quantity;
    private Double price;
    private String status;
    private LocalDateTime createdAt;

    // ── Constructors ──────────────────────────────────────────────────────────
    public OrderEvent() {}

    public OrderEvent(String orderId, String customerName, String customerEmail,
                      String productName, Integer quantity, Double price,
                      String status, LocalDateTime createdAt) {
        this.orderId       = orderId;
        this.customerName  = customerName;
        this.customerEmail = customerEmail;
        this.productName   = productName;
        this.quantity      = quantity;
        this.price         = price;
        this.status        = status;
        this.createdAt     = createdAt;
    }

    // ── Factory method ────────────────────────────────────────────────────────
    public static OrderEvent fromOrder(Order order) {
        return new OrderEvent(
                order.getOrderId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }

    // ── Static Builder ────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String orderId;
        private String customerName;
        private String customerEmail;
        private String productName;
        private Integer quantity;
        private Double price;
        private String status;
        private LocalDateTime createdAt;

        public Builder orderId(String v)       { this.orderId = v;       return this; }
        public Builder customerName(String v)  { this.customerName = v;  return this; }
        public Builder customerEmail(String v) { this.customerEmail = v; return this; }
        public Builder productName(String v)   { this.productName = v;   return this; }
        public Builder quantity(Integer v)     { this.quantity = v;      return this; }
        public Builder price(Double v)         { this.price = v;         return this; }
        public Builder status(String v)        { this.status = v;        return this; }
        public Builder createdAt(LocalDateTime v) { this.createdAt = v;  return this; }

        public OrderEvent build() {
            return new OrderEvent(orderId, customerName, customerEmail,
                    productName, quantity, price, status, createdAt);
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public String getOrderId()               { return orderId; }
    public void setOrderId(String v)         { this.orderId = v; }

    public String getCustomerName()          { return customerName; }
    public void setCustomerName(String v)    { this.customerName = v; }

    public String getCustomerEmail()         { return customerEmail; }
    public void setCustomerEmail(String v)   { this.customerEmail = v; }

    public String getProductName()           { return productName; }
    public void setProductName(String v)     { this.productName = v; }

    public Integer getQuantity()             { return quantity; }
    public void setQuantity(Integer v)       { this.quantity = v; }

    public Double getPrice()                 { return price; }
    public void setPrice(Double v)           { this.price = v; }

    public String getStatus()                { return status; }
    public void setStatus(String v)          { this.status = v; }

    public LocalDateTime getCreatedAt()      { return createdAt; }
    public void setCreatedAt(LocalDateTime v){ this.createdAt = v; }

    @Override
    public String toString() {
        return "OrderEvent{orderId='" + orderId + "', status='" + status + "'}";
    }
}
