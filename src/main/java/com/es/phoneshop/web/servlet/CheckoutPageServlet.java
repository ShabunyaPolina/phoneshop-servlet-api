package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.enums.PaymentMethod;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultOrderService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        request.setAttribute("order", order);
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());

        request.getSession().setAttribute("order", order);

        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        Map<String, String> errors = new HashMap<>();

        Predicate<String> isCorrectName = x -> x.matches("[a-zA-Z-]+");
        Predicate<String> isCorrectPhone = x -> x.matches("[0-9+-]+") && x.length() > 5;

        setRequiredParameter(request, "firstName", errors, order::setFirstName, isCorrectName);
        setRequiredParameter(request, "lastName", errors, order::setLastName, isCorrectName);
        setRequiredParameter(request, "deliveryAddress", errors, order::setDeliveryAddress, null);
        setRequiredParameter(request, "phone", errors, order::setPhone, isCorrectPhone);
        setDeliveryDate(request, errors, order);
        setPaymentMethod(request, errors, order);

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(cart);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        }
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                      Consumer<String> consumer, Predicate<String> predicate) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value is required");
        } else if (predicate != null && !predicate.test(value)) {
            errors.put(parameter, "Value is not correct");
        } else {
            consumer.accept(value);
        }
    }

    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("deliveryDate");
        if (value == null || value.isEmpty()) {
            errors.put("deliveryDate", "Value is required");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                order.setDeliveryDate(LocalDate.parse(value, formatter));
            } catch (DateTimeParseException e) {
                errors.put("deliveryDate", "Value is not correct");
            }
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("paymentMethod");
        if (value == null || value.isEmpty()) {
            errors.put("paymentMethod", "Value is required");
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }
}
