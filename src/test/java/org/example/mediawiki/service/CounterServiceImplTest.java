package org.example.mediawiki.service;

import org.example.mediawiki.service.impl.CounterServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CounterServiceImplTest {

    @Test
    void testIncrementCount() {
        CounterServiceImpl.resetCount();

        final int expectedCount = 1004;

        // Use AtomicInteger for atomic incrementation of the counter
        AtomicInteger actualCount = new AtomicInteger(0);

        Thread incrementThread = new Thread(() -> {
            for (int i = 0; i < expectedCount; i++) {
                CounterServiceImpl.incrementCount();
            }
            actualCount.set(CounterServiceImpl.getCount());
        });

        incrementThread.start();

        try {
            incrementThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check that the counter increased by the expected amount
        assertEquals(expectedCount, actualCount.get());
    }

    @Test
    void testGetCount() {
        // Reset the counter before each test
        CounterServiceImpl.resetCount();

        // Increment count to start from 1
        CounterServiceImpl.incrementCount();
        assertEquals(1, CounterServiceImpl.getCount());

        // Increment count and verify
        CounterServiceImpl.incrementCount();
        assertEquals(2, CounterServiceImpl.getCount());

        // Increment count multiple times and verify
        CounterServiceImpl.incrementCount();
        CounterServiceImpl.incrementCount();
        assertEquals(4, CounterServiceImpl.getCount());
    }
}
