package com.es.phoneshop.web.servlet;

import com.es.phoneshop.web.listener.DemoDataServletContextListener;
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
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
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
    @Mock
    private ServletContextEvent servletContextEvent;
    @Mock
    private ServletContext servletContext;

    private final CartPageServlet servlet = new CartPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);

        DemoDataServletContextListener servletContextListener = new DemoDataServletContextListener();
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter(eq("insertDemoData"))).thenReturn("true");
        servletContextListener.contextInitialized(servletContextEvent);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null);

        when(request.getLocale()).thenReturn(Locale.getDefault());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("cart"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostCorrectData() throws IOException, ServletException {
        String[] strArr = {"1", "2", "3"};
        when(request.getParameterValues(eq("productId"))).thenReturn(strArr);
        when(request.getParameterValues(eq("quantity"))).thenReturn(strArr);

        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostIncorrectQuantity() throws IOException, ServletException {
        String[] ids = {"1"};
        when(request.getParameterValues(eq("productId"))).thenReturn(ids);
        String[] quantities = {"a"};
        when(request.getParameterValues(eq("quantity"))).thenReturn(quantities);

        servlet.doPost(request, response);
        verify(request).setAttribute(eq("errors"), any());
        verify(request.getRequestDispatcher(anyString())).forward(request, response);
    }

    @Test
    public void testDoPostZeroQuantity() throws IOException, ServletException {
        String[] ids = {"1"};
        when(request.getParameterValues(eq("productId"))).thenReturn(ids);
        String[] quantities = {"0"};
        when(request.getParameterValues(eq("quantity"))).thenReturn(quantities);

        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostOutOfStock() throws IOException, ServletException {
        String[] ids = {"1"};
        when(request.getParameterValues(eq("productId"))).thenReturn(ids);
        String[] quantities = {"123"};
        when(request.getParameterValues(eq("quantity"))).thenReturn(quantities);

        servlet.doPost(request, response);
        verify(request).setAttribute(eq("errors"), any());
        verify(request.getRequestDispatcher(anyString())).forward(request, response);
    }
}
