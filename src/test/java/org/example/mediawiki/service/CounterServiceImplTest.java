package org.example.mediawiki.service;

import org.example.mediawiki.service.impl.CounterServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CounterServiceImplTest {


    @Test
    void testBoundaryValues() {
        CounterServiceImpl.resetCount();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            CounterServiceImpl.incrementCount();
        }

        assertEquals(Integer.MAX_VALUE, CounterServiceImpl.getCount());
    }

    @Test
    void testIncrementCount() {
        CounterServiceImpl.resetCount();

        final int expectedCount = 1004;

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

        assertEquals(expectedCount, actualCount.get());
    }

    @Test
    void testGetCount() {
        CounterServiceImpl.resetCount();

        CounterServiceImpl.incrementCount();
        assertEquals(1, CounterServiceImpl.getCount());

        CounterServiceImpl.incrementCount();
        assertEquals(2, CounterServiceImpl.getCount());

        CounterServiceImpl.incrementCount();
        CounterServiceImpl.incrementCount();
        assertEquals(4, CounterServiceImpl.getCount());
    }

    @Test
    void testResetCount() {
        CounterServiceImpl.resetCount();

        for (int i = 0; i < 5; i++) {
            CounterServiceImpl.incrementCount();
        }

        CounterServiceImpl.resetCount();

        assertEquals(0, CounterServiceImpl.getCount());
    }

    @Test
    void testMultithreadedIncrement() {
        CounterServiceImpl.resetCount();

        final int threadCount = 10;
        final int incrementsPerThread = 100;

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    CounterServiceImpl.incrementCount();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertEquals(threadCount * incrementsPerThread, CounterServiceImpl.getCount());
    }
}
