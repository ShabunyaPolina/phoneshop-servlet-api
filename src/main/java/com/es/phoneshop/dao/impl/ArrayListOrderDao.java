package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private List<Order> orderList;
    private long orderId;
    private final ReadWriteLock locker;

    private ArrayListOrderDao() {
        orderList = new ArrayList<>();
        locker = new ReentrantReadWriteLock();
    }

    private static final class OrderDaoHolder {
        private static final OrderDao INSTANCE = new ArrayListOrderDao();
    }

    public static OrderDao getInstance() {
        return OrderDaoHolder.INSTANCE;
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        locker.readLock().lock();
        try {
            if (id == null) {
                throw new IllegalArgumentException("Null id");
            }
            return orderList.stream()
                    .filter(order -> id.equals(order.getId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException("No order with id " + id, id));
        } finally {
            locker.readLock().unlock();
        }
    }

    @Override
    public void save(Order order) throws OrderNotFoundException {
        locker.writeLock().lock();
        try {
            if (order.getId() == null) {
                order.setId(++orderId);
                orderList.add(order);
            } else {
                int index = orderList.indexOf(getOrder(order.getId()));
                orderList.set(index, order);
            }
        } finally {
            locker.writeLock().unlock();
        }
    }
}
