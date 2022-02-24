package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayListOrderDaoTest {

    private static final OrderDao orderDao = ArrayListOrderDao.getInstance();
    private static final OrderService orderService = DefaultOrderService.getInstance();
    private static Order order1;


    @BeforeClass
    public static void setup() {
        order1 = new Order();
        Order order2 = new Order();
        orderService.placeOrder(order1);
        orderService.placeOrder(order2);
    }


    // getOrder method tests

    @Test(expected = IllegalArgumentException.class)
    public void testGetOrderNullId() {
        orderDao.get(null);
    }

    @Test
    public void testGetExistingOrder() {
        Long testId = 1L;
        Order receivedOrder = orderDao.get(testId);
        assertEquals(testId, receivedOrder.getId());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetNonExistingOrder() {
        orderDao.get(666L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOrderNullSecureId() {
        orderDao.getBySecureId(null);
    }

    @Test
    public void testGetOrderBySecureId() {
        String secureId = order1.getSecureId();
        Order receivedOrder = orderDao.getBySecureId(secureId);
        assertEquals(secureId, receivedOrder.getSecureId());
    }
}
