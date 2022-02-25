package com.es.phoneshop.service.impl;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.web.listener.DemoDataServletContextListener;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    private final CartService cartService = DefaultCartService.getInstance();
    private final OrderService orderService = DefaultOrderService.getInstance();

    // Mocks
    private static final ServletContextEvent servletContextEvent = Mockito.mock(ServletContextEvent.class);
    private static final ServletContext servletContext = Mockito.mock(ServletContext.class);
    private static final Order order = Mockito.mock(Order.class);

    private final Cart cart = new Cart();

    @BeforeClass
    public static void setup() {
        DemoDataServletContextListener servletContextListener = new DemoDataServletContextListener();
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter(eq("insertDemoData"))).thenReturn("true");
        servletContextListener.contextInitialized(servletContextEvent);
    }

    @Test
    public void testGetOrder() throws OutOfStockException {
        cartService.add(cart, 1L, 1);
        cartService.add(cart, 2L, 2);
        cartService.add(cart, 3L, 3);

        int size = cart.getItems().size();

        Order order = orderService.getOrder(cart);

        assertEquals(size, order.getItems().size());
    }

    @Test
    public void testGetPaymentMethods() {
        int paymentMethods = orderService.getPaymentMethods().size();
        assertEquals(2, paymentMethods);
    }

    @Test
    public void testPlaceOrder() {
        when(order.getId()).thenReturn(null);
        orderService.placeOrder(order);
        verify(order).setSecureId(anyString());
    }
}
