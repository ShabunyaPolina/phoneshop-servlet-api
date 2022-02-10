package com.es.phoneshop.service.recently_viewed_service.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.recentlyviewed.RecentlyViewedProducts;
import com.es.phoneshop.service.recently_viewed_service.RecentlyViewedService;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultRecentlyViewedService implements RecentlyViewedService {
    private static final String RECENTLY_VIEWED_SESSION_ATTRIBUTE =
            DefaultRecentlyViewedService.class.getName() + ".recentlyViewed";

    private final ReadWriteLock locker;

    private DefaultRecentlyViewedService() {
        locker = new ReentrantReadWriteLock();
    }

    private static final class DefaultRecentlyViewedServiceHolder {
        private static final DefaultRecentlyViewedService INSTANCE = new DefaultRecentlyViewedService();
    }

    public static DefaultRecentlyViewedService getInstance() {
        return DefaultRecentlyViewedServiceHolder.INSTANCE;
    }

    @Override
    public RecentlyViewedProducts getRecentlyViewed(HttpServletRequest request) {
        locker.readLock().lock();
        try {
            RecentlyViewedProducts recentlyViewed = (RecentlyViewedProducts) request.getSession()
                    .getAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE);
            if (recentlyViewed == null) {
                request.getSession().setAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE, recentlyViewed = new RecentlyViewedProducts());
            }
            return recentlyViewed;
        } finally {
            locker.readLock().unlock();
        }
    }

    @Override
    public Product getLastViewedProduct(RecentlyViewedProducts recentlyViewed) {
        return recentlyViewed.getLastViewedProduct();
    }

    @Override
    public void add(RecentlyViewedProducts recentlyViewed, Product product, int numberOfDisplayedProducts) {
        if (product != null) {
            recentlyViewed.getRecentlyViewedProducts().removeIf(item -> product.getId().equals(item.getId()));

            if (recentlyViewed.getRecentlyViewedProducts().size() == numberOfDisplayedProducts) {
                recentlyViewed.getRecentlyViewedProducts().remove(numberOfDisplayedProducts - 1);
            }

            recentlyViewed.getRecentlyViewedProducts().add(0, product);
        }
    }
}
