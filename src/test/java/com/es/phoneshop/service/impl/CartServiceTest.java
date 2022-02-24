package com.es.phoneshop.service.impl;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.web.listener.DemoDataServletContextListener;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartServiceTest {

    private final CartService cartService = DefaultCartService.getInstance();

    // Mocks
    private static final ServletContextEvent servletContextEvent = Mockito.mock(ServletContextEvent.class);
    private static final ServletContext servletContext = Mockito.mock(ServletContext.class);
    private static final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private static final HttpSession session = Mockito.mock(HttpSession.class);

    private final Cart cart = new Cart();

    @BeforeClass
    public static void setup() {
        DemoDataServletContextListener servletContextListener = new DemoDataServletContextListener();
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter(eq("insertDemoData"))).thenReturn("true");
        servletContextListener.contextInitialized(servletContextEvent);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null);
    }

    @Test
    public void testGetCart() {
        cartService.getCart(request);
        verify(session).setAttribute(anyString(), any());
        verify(session).getAttribute(anyString());
    }


    @Test
    public void testAddProductToCart() throws OutOfStockException {
        int initialQuantity = 3;
        cartService.add(cart, 1L, initialQuantity);
        assertEquals(1, cart.getItems().size());

        assertEquals(initialQuantity, cart.getItems().get(0).getQuantity());

        int additionalQuantity = 5;
        int newQuantity = initialQuantity + additionalQuantity;

        cartService.add(cart, 1L, additionalQuantity);

        assertEquals(newQuantity, cart.getItems().get(0).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddProductToCartOutOfStock() throws OutOfStockException {
        cartService.add(cart, 1L, 123);
    }

    @Test
    public void testUpdateCart() throws OutOfStockException {
        int initialQuantity = 3;
        cartService.add(cart, 1L, initialQuantity);

        assertEquals(initialQuantity, cart.getItems().get(0).getQuantity());

        int newQuantity = 5;
        cartService.update(cart, 1L, newQuantity);

        assertEquals(newQuantity, cart.getItems().get(0).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateCartOutOfStock() throws OutOfStockException {
        int initialQuantity = 3;
        cartService.add(cart, 1L, initialQuantity);

        assertEquals(initialQuantity, cart.getItems().get(0).getQuantity());

        int newQuantity = 123;
        cartService.update(cart, 1L, newQuantity);

        assertEquals(newQuantity, cart.getItems().get(0).getQuantity());
    }

    @Test
    public void testDeleteCartItem() throws OutOfStockException {
        Long productId = 1L;

        cartService.add(cart, productId, 1);

        assertEquals(1, cart.getItems().size());

        cartService.delete(cart, productId);

        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void testClearCart() throws OutOfStockException {
        cartService.add(cart, 1L, 1);
        cartService.add(cart, 2L, 1);
        cartService.add(cart, 3L, 1);

        assertEquals(3, cart.getItems().size());

        cartService.clear(cart);

        assertEquals(0, cart.getItems().size());
    }
}
