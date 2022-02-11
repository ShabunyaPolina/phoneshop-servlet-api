package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.recentlyviewed.RecentlyViewedProducts;
import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.service.recently_viewed_service.RecentlyViewedService;
import com.es.phoneshop.service.recently_viewed_service.impl.DefaultRecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
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
}
