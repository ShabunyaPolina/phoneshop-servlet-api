package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.product.Product;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, String> errors = new HashMap<>();

        String productCode = request.getParameter("productCode");

        BigDecimal minPrice = parsePrice(request, "minPrice", errors);
        BigDecimal maxPrice = parsePrice(request, "maxPrice", errors);

        int minStock = 0;
        String minStockString = request.getParameter("minStock");
        if(minStockString != null && !minStockString.isEmpty()) {
            try {
                minStock = Integer.parseInt(minStockString);
            } catch (NumberFormatException e) {
                errors.put("minStock", "Not a number");
            }
        }

        if (errors.isEmpty()) {
            ArrayList<Product> products = (ArrayList<Product>) productDao.findProductsForAdvancedSearch(productCode, minPrice, maxPrice, minStock);
            request.setAttribute("products", products);
            request.setAttribute("message", "Found " + products.size() + " products");
        } else {
            request.setAttribute("products", new ArrayList<Product>());
            request.setAttribute("errors", errors);
        }
        request.getRequestDispatcher("/WEB-INF/pages/searchPage.jsp").forward(request, response);
    }

    private BigDecimal parsePrice(HttpServletRequest request, String parameter, Map<String, String> errors) {
        String priceString = request.getParameter(parameter);
        BigDecimal price = null;

        try {
            if (priceString == null || priceString.isEmpty()) {
                if(parameter.equals("minPrice")) {
                    price = new BigDecimal(0);
                } else {
                    price = new BigDecimal(Double.MAX_VALUE);
                }
            } else if (!priceString.matches("[0-9,.]+")) {
                throw new NumberFormatException("Not a number");
            } else {
                price = new BigDecimal(priceString);
            }
        } catch (NumberFormatException e) {
            errors.put(parameter, "Not a number");
        }
        return price;
    }
}
