package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.order.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;

    private final CheckoutPageServlet servlet = new CheckoutPageServlet();
    private final Order order = new Order();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);

        when(session.getAttribute(eq("order"))).thenReturn(order);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(session).setAttribute(eq("order"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostCorrectData() throws IOException, ServletException {
        when(request.getParameter("firstName")).thenReturn("Polina");
        when(request.getParameter("lastName")).thenReturn("Shabunya");
        when(request.getParameter("deliveryAddress")).thenReturn("asd");
        when(request.getParameter("phone")).thenReturn("24395664");
        when(request.getParameter("deliveryDate")).thenReturn("2020-12-23");
        when(request.getParameter("paymentMethod")).thenReturn("CACHE");

        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostInCorrectString() throws IOException, ServletException {
        when(request.getParameter("firstName")).thenReturn("123");

        servlet.doPost(request, response);
        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostInCorrectDate() throws IOException, ServletException {
        when(request.getParameter("deliveryDate")).thenReturn("123");

        servlet.doPost(request, response);
        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }
}
