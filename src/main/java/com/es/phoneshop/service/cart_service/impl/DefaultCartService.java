package com.es.phoneshop.service.cart_service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.service.cart_service.CartService;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private final ProductDao productDao;

    private final ReadWriteLock locker;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
        locker = new ReentrantReadWriteLock();
    }

    private static final class DefaultCartServiceHolder {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartServiceHolder.INSTANCE;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        locker.readLock().lock();
        try {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        } finally {
            locker.readLock().unlock();
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        locker.writeLock().lock();
        try {
            if(quantity != 0) {
                Product product = productDao.getProduct(productId);

                Optional<CartItem> cartItem = cart.getItems().stream()
                        .filter(item -> productId.equals(item.getProduct().getId()))
                        .findAny();

                int quantityInCart = cartItem.map(CartItem::getQuantity).orElse(0);

                int availableStock = product.getStock() - quantityInCart;

                if (availableStock - quantity < 0) {
                    throw new OutOfStockException(product, quantity, availableStock);
                }

                if (cartItem.isPresent()) {
                    cartItem.get().increaseQuantity(quantity);
                } else {
                    cart.getItems().add(new CartItem(product, quantity));
                }

                recalculateCart(cart);
            }
        } finally {
            locker.writeLock().unlock();
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        locker.writeLock().lock();
        try {
            if(quantity != 0) {
                Product product = productDao.getProduct(productId);

                Optional<CartItem> cartItem = cart.getItems().stream()
                        .filter(item -> productId.equals(item.getProduct().getId()))
                        .findAny();

                if (product.getStock() - quantity < 0) {
                    throw new OutOfStockException(product, quantity, product.getStock());
                }

                cartItem.ifPresent(item -> item.updateQuantity(quantity));

                recalculateCart(cart);
            }
        } finally {
            locker.writeLock().unlock();
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        locker.writeLock().lock();
        try {
            cart.getItems().removeIf(item ->
                    productId.equals(item.getProduct().getId())
            );
            recalculateCart(cart);
        } finally {
            locker.writeLock().unlock();
        }
    }

    private void recalculateTotalCartQuantity(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum());
    }

    private void recalculateTotalCartCoast(Cart cart) {
        cart.setTotalCoast(cart.getItems().stream()
                .map(item -> item.getProduct()
                        .getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO)
        );
    }

    private void recalculateCart(Cart cart) {
        recalculateTotalCartQuantity(cart);
        recalculateTotalCartCoast(cart);
    }
}
