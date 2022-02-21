package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.recentlyviewed.RecentlyViewedProducts;
import com.es.phoneshop.service.cart_service.CartService;
import com.es.phoneshop.service.cart_service.impl.DefaultCartService;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.recently_viewed_service.RecentlyViewedService;
import com.es.phoneshop.service.recently_viewed_service.impl.DefaultRecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        Product product = productDao.get(productId);
        Cart cart = cartService.getCart(request);
        RecentlyViewedProducts recentlyViewed = recentlyViewedService.getRecentlyViewed(request);

        recentlyViewed.setLastViewedProduct(product);
        request.setAttribute("recentlyViewed", recentlyViewed.getRecentlyViewedProducts());

        request.setAttribute("product", product);

        request.setAttribute("cart", cart);

        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String quantityString = request.getParameter("quantity");
        Long productId = parseProductId(request);

        int quantity;
        try {
            if (!quantityString.matches("[0-9,.]+")) {
                throw new NumberFormatException("Not a number");
            }
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException | NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products/" + productId
                    + "?error=Not a number&errorQuantity=" + quantityString);
            return;
        }

        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            String errorMessage = "Out of stock, available " + e.getStockAvailable();
            response.sendRedirect(request.getContextPath() + "/products/" + productId + "?error=" + errorMessage
                    + "&errorQuantity=" + quantityString);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo();
        return Long.valueOf(productId.substring(1));
    }
}
