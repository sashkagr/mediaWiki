package org.example.mediawiki.service.impl;

import lombok.Data;
import org.example.mediawiki.modal.ServiceCounter;


import java.util.concurrent.atomic.AtomicInteger;


@Data
public final class CounterServiceImpl {

    private CounterServiceImpl() {
    }

    private static ServiceCounter serviceCounter = new ServiceCounter();

    private static AtomicInteger newEnhanceCounter = new AtomicInteger(0);


    public static synchronized void incrementCount() {
        if (serviceCounter.getCounterRequest() != null) {
            newEnhanceCounter = serviceCounter.getCounterRequest();
        }
        newEnhanceCounter.incrementAndGet();
        serviceCounter.setCounterRequest(newEnhanceCounter);
    }

    public static synchronized int getCount() {
        AtomicInteger newCounter = serviceCounter.getCounterRequest();
        return newCounter.get();
    }
}