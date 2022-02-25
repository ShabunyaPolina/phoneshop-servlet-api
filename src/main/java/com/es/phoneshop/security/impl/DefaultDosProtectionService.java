package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 20;

    private final Map<String, Long> countMap = new ConcurrentHashMap<>();

    private DefaultDosProtectionService() {
        Thread mapCleaner = new Thread(new Thread(() -> {
            while(true){
                try {
                    countMap.clear();
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getCause());
                }
            }
        }));
        mapCleaner.start();
    }

    private static final class DosProtectionServiceHolder {
        private static final DosProtectionService INSTANCE = new DefaultDosProtectionService();
    }

    public static DosProtectionService getInstance() {
        return DosProtectionServiceHolder.INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if(count > THRESHOLD) {
                return false;
            }
            ++count;
        }
        countMap.put(ip, count);
        return true;
    }
}
