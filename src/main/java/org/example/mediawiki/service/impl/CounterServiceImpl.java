package org.example.mediawiki.service.impl;

import org.example.mediawiki.modal.ServiceCounter;

import java.util.concurrent.atomic.AtomicInteger;

public final class CounterServiceImpl {

    private CounterServiceImpl() {
    }

    private static final Object lock = new Object();

    private static ServiceCounter serviceCounter = new ServiceCounter();
    private static AtomicInteger newEnhanceCounter = new AtomicInteger(0);

    public static void incrementCount() {
        synchronized (lock) {
            if (serviceCounter.getCounterRequest() != null) {
                newEnhanceCounter = serviceCounter.getCounterRequest();
            }
            newEnhanceCounter.incrementAndGet();
            serviceCounter.setCounterRequest(newEnhanceCounter);
        }
    }

    public static int getCount() {
        synchronized (lock) {
            AtomicInteger newCounter = serviceCounter.getCounterRequest();
            if (newCounter != null) {
                return newCounter.get();
            } else {
                return 0;
            }
        }
    }

    public static void resetCount() {
        AtomicInteger newCounter = new AtomicInteger(0);
        serviceCounter.setCounterRequest(newCounter);
    }
}
