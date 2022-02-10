package com.es.phoneshop.service.recently_viewed_service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.recentlyviewed.RecentlyViewedProducts;

import javax.servlet.http.HttpServletRequest;

public interface RecentlyViewedService {
    RecentlyViewedProducts getRecentlyViewed(HttpServletRequest request);
    Product getLastViewedProduct(RecentlyViewedProducts recentlyViewed);
    void add(RecentlyViewedProducts recentlyViewedProducts, Product product, int numberOfDisplayedProducts);
}
