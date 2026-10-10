package com.rohan.notificationservice.model;

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
