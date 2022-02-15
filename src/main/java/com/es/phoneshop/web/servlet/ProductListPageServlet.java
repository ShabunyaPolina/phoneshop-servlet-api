package com.es.phoneshop.web.servlet;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.recentlyviewed.RecentlyViewedProducts;
import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.service.cart_service.CartService;
import com.es.phoneshop.service.cart_service.impl.DefaultCartService;
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
import java.util.Optional;


public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private RecentlyViewedService recentlyViewedService;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RecentlyViewedProducts recentlyViewed = recentlyViewedService.getRecentlyViewed(request);

        recentlyViewedService.add(recentlyViewed, recentlyViewedService.getLastViewedProduct(recentlyViewed), 3);
        request.setAttribute("recentlyViewed", recentlyViewed.getRecentlyViewedProducts());

        String query = request.getParameter("query");
        SortField sortField = Optional.ofNullable(request.getParameter("sort"))
                .map(SortField::valueOf)
                .orElse(null);
        SortOrder sortOrder = Optional.ofNullable(request.getParameter("order"))
                .map(SortOrder::valueOf)
                .orElse(null);
        request.setAttribute("products", productDao.findProducts(query, sortField, sortOrder));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Long productId = Long.valueOf(request.getParameter("id"));

        for(int i = 0; i < productIds.length; i++) {
            if(Long.valueOf(productIds[i]).equals(productId)) {
                String quantityString = quantities[i];

                int quantity;
                try {
                    if (!quantities[i].matches("[0-9,.]+")) {
                        throw new NumberFormatException();
                    }
                    NumberFormat format = NumberFormat.getInstance(request.getLocale());
                    quantity = format.parse(quantityString).intValue();
                } catch (ParseException | NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/products?errorId=" + productId + "&error=Not a number");
                    return;
                }

                if(quantity == 0) {
                    response.sendRedirect(request.getContextPath() + "/products");
                    return;
                }

                Cart cart = cartService.getCart(request);
                try {
                    cartService.add(cart, Long.valueOf(productIds[i]), quantity);
                } catch (OutOfStockException e) {
                    request.setAttribute("errorId", productId);
                    String errorMessage = "Out of stock, available " + e.getStockAvailable();
                    response.sendRedirect(request.getContextPath() + "/products?errorId=" + productId + "&error=" + errorMessage);
                    return;
                }
                break;
            }
        }
        response.sendRedirect(request.getContextPath() + "/products?message=Product added to cart");
    }
}
