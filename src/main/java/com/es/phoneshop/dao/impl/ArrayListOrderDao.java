package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.GenericArrayListDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.DaoItemNotFoundException;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public class ArrayListOrderDao extends GenericArrayListDao<Order> implements OrderDao {

    private ArrayListOrderDao() {
        super();
    }

    private static final class OrderDaoHolder {
        private static final OrderDao INSTANCE = new ArrayListOrderDao();
    }

    public static OrderDao getInstance() {
        return OrderDaoHolder.INSTANCE;
    }

    @Override
    public Order get(Long id) throws OrderNotFoundException, IllegalArgumentException {
        try {
            return super.get(id);
        } catch (DaoItemNotFoundException e) {
            throw new OrderNotFoundException(e.getMessage(), e.getId());
        }
    }

    @Override
    public Order getBySecureId(String secureId) throws OrderNotFoundException {
        getLocker().readLock().lock();
        try {
            if (secureId == null) {
                throw new IllegalArgumentException("Null id");
            }
            return getItems().stream()
                    .filter(item -> secureId.equals(item.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException("No order with secure id " + secureId));
        } finally {
            getLocker().readLock().unlock();
        }
    }

    @Override
    public void save(Order order) throws OrderNotFoundException {
        super.save(order);
    }
}
