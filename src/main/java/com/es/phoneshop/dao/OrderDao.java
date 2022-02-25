package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public interface OrderDao {
    Order get(Long id) throws OrderNotFoundException;
    Order getBySecureId(String secureId) throws OrderNotFoundException;
    void save(Order order) throws OrderNotFoundException;
}
