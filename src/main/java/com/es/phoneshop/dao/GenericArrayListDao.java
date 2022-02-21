package com.es.phoneshop.dao;

import com.es.phoneshop.dao.dao_item.DaoItem;
import com.es.phoneshop.exception.DaoItemNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GenericArrayListDao<T extends DaoItem> {
    private final List<T> items;
    private long currentId;
    private final ReadWriteLock locker;

    public GenericArrayListDao() {
        items = new ArrayList<>();
        locker = new ReentrantReadWriteLock();
    }

    public List<T> getItems() {
        return items;
    }

    public ReadWriteLock getLocker() {
        return locker;
    }

    public T get(Long id) throws DaoItemNotFoundException, IllegalArgumentException {
        locker.readLock().lock();
        try {
            if (id == null) {
                throw new IllegalArgumentException("Null id");
            }
            return items.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(() -> new DaoItemNotFoundException("No order with id " + id, id));
        } finally {
            locker.readLock().unlock();
        }
    }


    public void save(T item) {
        locker.writeLock().lock();
        try {
            if (item.getId() == null) {
                item.setId(++currentId);
                items.add(item);
            } else {
                int index = items.indexOf(get(item.getId()));
                items.set(index, item);
            }
        } finally {
            locker.writeLock().unlock();
        }
    }

    public void delete(Long id) throws DaoItemNotFoundException {
        locker.writeLock().lock();
        try {
            items.removeIf(item -> item.getId() == null || id.equals(item.getId()));
        } finally {
            locker.writeLock().unlock();
        }
    }
}
