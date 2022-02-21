package com.es.phoneshop.exception;

public class DaoItemNotFoundException extends RuntimeException {
    private final Long id;

    public DaoItemNotFoundException(Long id) {
        super();
        this.id = id;
    }

    public DaoItemNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
