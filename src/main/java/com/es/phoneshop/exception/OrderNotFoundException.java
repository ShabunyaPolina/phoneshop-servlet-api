package com.es.phoneshop.exception;

public class OrderNotFoundException extends RuntimeException{
    private Long orderId;

    public OrderNotFoundException(Long id) {
        super();
        this.orderId = id;
    }

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Long id) {
        super(message);
        this.orderId = id;
    }

    public Long getOrderId() {
        return orderId;
    }
}
