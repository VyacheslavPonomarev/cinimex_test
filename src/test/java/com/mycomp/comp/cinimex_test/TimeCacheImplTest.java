package com.mycomp.comp.cinimex_test;

import com.mycomp.comp.cinimex_test.timecache.TimeCacheItem;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import com.mycomp.comp.cinimex_test.timecache.TimeCacheImpl;

import static org.junit.jupiter.api.Assertions.*;

class TimeCacheImplTest {

    @Test
    void testAdd() {
        TimeCacheImpl<Integer, String> cache = new TimeCacheImpl<>(Duration.ofSeconds(30), Duration.ofSeconds(1));
        cache.add(1, "Один");
        cache.add(2, "Два");
        assertEquals(2, cache.getSize());
    }

    @Test
    void testGet() {
        TimeCacheImpl<Integer, String> cache = new TimeCacheImpl<>(Duration.ofSeconds(30), Duration.ofSeconds(1));
        cache.add(1, "Один");
        assertEquals("Один", cache.get(1));
    }

    @Test
    void testRemove() {
        TimeCacheImpl<Integer, String> cache = new TimeCacheImpl<>(Duration.ofSeconds(30), Duration.ofSeconds(1));
        cache.add(1, "Один");
        cache.remove(1);
        assertNull(cache.get(1));
    }

    @Test
    void testClear() {
        TimeCacheImpl<Integer, String> cache = new TimeCacheImpl<>(Duration.ofSeconds(30), Duration.ofSeconds(1));
        cache.add(1, "Один");
        cache.add(2, "Два");
        cache.clear();
        assertEquals(0, cache.getSize());
    }

    @Test
    void testSetTimeToLive() {
        TimeCacheImpl<Integer, String> cache = new TimeCacheImpl<>(Duration.ofSeconds(30), Duration.ofSeconds(1));
        cache.setTimeToLive(5000L);
        assertEquals(5000L, cache.getTimeToLive());
    }

    @Test
    void testExpired() throws InterruptedException {
        TimeCacheItem<String> item = new TimeCacheItem<>("Один", System.currentTimeMillis());
        Thread.sleep(6000);
        assertTrue(item.isExpired(5000L));
    }

    @Test
    void testAutoDelete() throws InterruptedException {
        TimeCacheImpl<Integer, String> cache = new TimeCacheImpl<>(Duration.ofSeconds(5), Duration.ofSeconds(1));
        cache.add(10, "Десять");
        assertEquals("Десять", cache.get(10));
        Thread.sleep(6000);
        assertNull(cache.get(10));
    }

    @Test
    void testUpdateItemAddTime() throws InterruptedException {
        TimeCacheItem<String> item = new TimeCacheItem<>("Один", System.currentTimeMillis());
        Thread.sleep(6000);
        assertTrue(item.isExpired(5000L));
        item.setAddTime(System.currentTimeMillis());
        assertFalse(item.isExpired(5000L));
    }
}