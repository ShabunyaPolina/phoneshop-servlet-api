package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.dao_item.DaoItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Cart extends DaoItem implements Serializable {
    private List<CartItem> items;

    private int totalQuantity;
    private BigDecimal totalCoast;

    private Currency currency;

    public Cart() {
        super(null);
        this.items = new ArrayList<>();
        currency = Currency.getInstance("USD");
    }

    public List<CartItem> getItems() {
        return items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalCoast() {
        return totalCoast;
    }

    public void setTotalCoast(BigDecimal totalCoast) {
        this.totalCoast = totalCoast;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "[" + items + "]";
    }
}
