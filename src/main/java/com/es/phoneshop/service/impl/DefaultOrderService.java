package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.enums.PaymentMethod;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private final OrderDao orderDao;

    private final ReadWriteLock locker;

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
        locker = new ReentrantReadWriteLock();
    }

    private static final class DefaultOrderServiceHolder {
        private static final DefaultOrderService INSTANCE = new DefaultOrderService();
    }

    public static DefaultOrderService getInstance() {
        return DefaultOrderServiceHolder.INSTANCE;
    }

    @Override
    public Order getOrder(Cart cart) {
        locker.writeLock().lock();
        try {
            Order order = new Order();
            if (cart != null && !cart.getItems().isEmpty()) {
                order.setItems(cart.getItems().stream()
                        .map(CartItem::clone)
                        .collect(Collectors.toList()));
                order.setSubtotal(cart.getTotalCoast());
                order.setDeliveryCoast(calculateDeliveryCoast());
                order.setTotalCoast(order.getSubtotal().add(order.getDeliveryCoast()));
            }
            return order;
        } finally {
            locker.writeLock().unlock();
        }
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCoast() {
        return new BigDecimal(5);
    }
}
