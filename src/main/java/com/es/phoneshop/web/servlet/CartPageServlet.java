package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.recentlyviewed.RecentlyViewedProducts;
import com.es.phoneshop.service.cart_service.CartService;
import com.es.phoneshop.service.cart_service.impl.DefaultCartService;
import com.es.phoneshop.service.recently_viewed_service.impl.DefaultRecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CartPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);

        request.setAttribute("cart", cart);

        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }
}
