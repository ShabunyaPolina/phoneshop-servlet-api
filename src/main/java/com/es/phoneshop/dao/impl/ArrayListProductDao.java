package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.generic.GenericArrayListDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.DaoItemNotFoundException;
import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericArrayListDao<Product> implements ProductDao {

    private ArrayListProductDao() {
        super();
    }

    private static final class ProductDaoHolder {
        private static final ProductDao INSTANCE = new ArrayListProductDao();
    }

    public static ProductDao getInstance() {
        return ProductDaoHolder.INSTANCE;
    }

    @Override
    public Product get(Long id) {
        try {
            return super.get(id);
        } catch (DaoItemNotFoundException e) {
            throw new ProductNotFoundException(e.getMessage(), e.getId());
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        getLocker().readLock().lock();
        try {
            List<Product> result = getItems();

            if (query != null && !query.isEmpty()) {
                result = getItems().stream()
                        .filter(product -> countMatchingWords(product, query) != 0)
                        .sorted(Comparator.comparingInt(product -> countMatchingWords((Product) product, query))
                                .reversed())
                        .collect(Collectors.toList());
            }

            if (sortField != null) {
                result = result.stream()
                        .sorted(getComparator(sortField, sortOrder))
                        .collect(Collectors.toList());
            }

            return result.stream()
                    .filter(this::productHasNonNullPrice)
                    .filter(this::productIsInStock)
                    .collect(Collectors.toList());
        } finally {
            getLocker().readLock().unlock();
        }
    }

    private Comparator<Product> getComparator(SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator = (product1, product2) -> {
            if (SortField.DESCRIPTION == sortField) {
                return product1.getDescription().compareTo(product2.getDescription());
            } else {
                return product1.getPrice().compareTo(product2.getPrice());
            }
        };
        if (SortOrder.DESC == sortOrder) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private int countMatchingWords(Product product, String query) {
        String[] queryTerms = query.toLowerCase().split("\\s");
        String[] productDescriptionTerms = product.getDescription().toLowerCase().split("\\s");
        return (int) Arrays.stream(queryTerms)
                .filter(Arrays.asList(productDescriptionTerms)::contains)
                .count();
    }

    private boolean productHasNonNullPrice(Product product) {
        return product.getPrice() != null;
    }

    private boolean productIsInStock(Product product) {
        return product.getStock() > 0;
    }

    @Override
    public void save(Product product) {
        super.save(product);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<Product> findProductsForAdvancedSearch(String productCode, BigDecimal minPrice, BigDecimal maxPrice, int minStock) {
        getLocker().readLock().lock();
        try {
            List<Product> result = getItems();
            if (productCode != null && !productCode.isEmpty()) {
                result = getItems().stream()
                        .filter(product -> product.getCode().contains(productCode))
                        .collect(Collectors.toList());
            }
            return result.stream()
                    .filter(this::productHasNonNullPrice)
                    .filter(product -> product.getPrice().compareTo(minPrice) >= 0)
                    .filter(product -> product.getPrice().compareTo(maxPrice) <= 0)
                    .filter(product -> product.getStock() >= minStock)
                    .collect(Collectors.toList());
        } finally {
            getLocker().readLock().unlock();
        }
    }
}
