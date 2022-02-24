package com.es.phoneshop.service.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.recentlyviewed.RecentlyViewedProducts;
import com.es.phoneshop.service.RecentlyViewedService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecentlyViewedServiceTest {

    private final RecentlyViewedService recentlyViewedService
            = DefaultRecentlyViewedService.getInstance();

    // Mocks
    private static final ServletContextEvent servletContextEvent = Mockito.mock(ServletContextEvent.class);
    private static final ServletContext servletContext = Mockito.mock(ServletContext.class);
    private static final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private static final HttpSession session = Mockito.mock(HttpSession.class);
    private static final Product product = Mockito.mock(Product.class);

    private static final RecentlyViewedProducts recentlyViewed = new RecentlyViewedProducts();

    @BeforeClass
    public static void setup() {
        DemoDataServletContextListener servletContextListener = new DemoDataServletContextListener();
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter(eq("insertDemoData"))).thenReturn("true");
        servletContextListener.contextInitialized(servletContextEvent);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null);

        recentlyViewed.setLastViewedProduct(product);
    }

    @Test
    public void testGetRecentlyViewed() {
        recentlyViewedService.getRecentlyViewed(request);
        verify(session).setAttribute(anyString(), any());
        verify(session).getAttribute(anyString());
    }

    @Test
    public void testGetRecentlyViewedProduct() {
        Product lastProduct = recentlyViewedService.getLastViewedProduct(recentlyViewed);
        assertEquals(product, lastProduct);
    }

    @Test
    public void testAddProductToRecentlyViewed() {
        recentlyViewedService.add(recentlyViewed, product, 3);

        assertEquals(1, recentlyViewed.getRecentlyViewedProducts().size());
    }
}
