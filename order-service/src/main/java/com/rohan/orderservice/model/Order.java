package com.rohan.orderservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotNull
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotNull
    @Positive(message = "Price must be positive")
    private Double price;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PLACED;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum OrderStatus {
        PLACED, PROCESSING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    // ── Constructors ──────────────────────────────────────────────────────────
    public Order() {}

    public Order(String orderId, String customerName, String customerEmail,
                 String productName, Integer quantity, Double price,
                 OrderStatus status, LocalDateTime createdAt) {
        this.orderId       = orderId;
        this.customerName  = customerName;
        this.customerEmail = customerEmail;
        this.productName   = productName;
        this.quantity      = quantity;
        this.price         = price;
        this.status        = status != null ? status : OrderStatus.PLACED;
        this.createdAt     = createdAt != null ? createdAt : LocalDateTime.now();
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
        private OrderStatus status = OrderStatus.PLACED;
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder orderId(String v)       { this.orderId = v;       return this; }
        public Builder customerName(String v)  { this.customerName = v;  return this; }
        public Builder customerEmail(String v) { this.customerEmail = v; return this; }
        public Builder productName(String v)   { this.productName = v;   return this; }
        public Builder quantity(Integer v)     { this.quantity = v;      return this; }
        public Builder price(Double v)         { this.price = v;         return this; }
        public Builder status(OrderStatus v)   { this.status = v;        return this; }
        public Builder createdAt(LocalDateTime v) { this.createdAt = v;  return this; }

        public Order build() {
            return new Order(orderId, customerName, customerEmail,
                    productName, quantity, price, status, createdAt);
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public String getOrderId()              { return orderId; }
    public void setOrderId(String v)        { this.orderId = v; }

    public String getCustomerName()         { return customerName; }
    public void setCustomerName(String v)   { this.customerName = v; }

    public String getCustomerEmail()        { return customerEmail; }
    public void setCustomerEmail(String v)  { this.customerEmail = v; }

    public String getProductName()          { return productName; }
    public void setProductName(String v)    { this.productName = v; }

    public Integer getQuantity()            { return quantity; }
    public void setQuantity(Integer v)      { this.quantity = v; }

    public Double getPrice()                { return price; }
    public void setPrice(Double v)          { this.price = v; }

    public OrderStatus getStatus()          { return status; }
    public void setStatus(OrderStatus v)    { this.status = v; }

    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', customer='" + customerName +
               "', product='" + productName + "', status=" + status + "}";
    }
}
