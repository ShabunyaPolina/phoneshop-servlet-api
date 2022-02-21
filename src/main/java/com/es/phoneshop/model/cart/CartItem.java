package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;

import java.io.Serializable;

public class CartItem implements Serializable, Cloneable {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int addValue) {
        this.quantity += addValue;
    }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public String toString() {
        return product.getCode() + ": " + quantity;
    }

    @Override
    public CartItem clone() {
        try {
            return (CartItem) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
