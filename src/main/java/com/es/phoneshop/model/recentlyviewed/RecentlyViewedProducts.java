package com.es.phoneshop.model.recentlyviewed;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedProducts {
    private Product lastViewedProduct;
    private final List<Product> recentlyViewedProducts;

    public RecentlyViewedProducts() {
        this.recentlyViewedProducts = new ArrayList<>();
    }

    public Product getLastViewedProduct() {
        return lastViewedProduct;
    }

    public void setLastViewedProduct(Product lastViewedProduct) {
        this.lastViewedProduct = lastViewedProduct;
    }

    public List<Product> getRecentlyViewedProducts() {
        return recentlyViewedProducts;
    }
}
