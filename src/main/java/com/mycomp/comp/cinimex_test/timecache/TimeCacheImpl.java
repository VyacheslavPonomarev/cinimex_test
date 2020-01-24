package com.mycomp.comp.cinimex_test.timecache;

import java.time.Duration;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import static java.lang.Thread.sleep;

public class TimeCacheImpl<K, V> implements TimeCache<K, V> {
    private Map<K, TimeCacheItem<V>> timeCacheItemsMap = new ConcurrentHashMap<>();
    private Queue<K> timeCacheItemsQueue = new ConcurrentLinkedQueue<>();
    private Long timeToLive;
    private Long checkTime;

    public TimeCacheImpl(Duration timeToLive, Duration checkTime) {
        this.timeToLive = timeToLive.toMillis();
        this.checkTime = checkTime.toMillis();

        Thread autoCheckThread = new Thread(new AutoCheckRunnable());
        autoCheckThread.setDaemon(true);
        autoCheckThread.start();
    }

    @Override
    public void add(K key, V value) {
        timeCacheItemsQueue.add(key);
        timeCacheItemsMap.put(key, new TimeCacheItem<>(value, System.currentTimeMillis()));
    }

    @Override
    public V get(K key) {
        TimeCacheItem<V> item = timeCacheItemsMap.get(key);
        if (item == null) return null;
        else return item.getValue();
    }

    @Override
    public int getSize() {
        return timeCacheItemsMap.size();
    }

    @Override
    public void remove(K key) {
        timeCacheItemsMap.remove(key);
    }

    @Override
    public void clear() {
        timeCacheItemsMap.clear();
    }

    @Override
    public Long getItemAddTime(K key) {
        TimeCacheItem<V> item = timeCacheItemsMap.get(key);
        if (item == null) return null;
        else return item.getAddTime();
    }

    @Override
    public void updateItemAddTime(Duration addTime, K key) {
        TimeCacheItem<V> timeCacheItem = timeCacheItemsMap.get(key);
        if (timeCacheItem != null) {
            timeCacheItem.setAddTime(addTime.toMillis());
        }
    }

    @Override
    public Long getTimeToLive() {
        return this.timeToLive;
    }

    @Override
    public void setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
    }

    private class AutoCheckRunnable implements Runnable {
        @Override
        public void run() {
            autoCheckCacheItems();
        }

        private void checkCacheItemsTimeLive() {
            for (K k = timeCacheItemsQueue.peek(); k != null && timeCacheItemsMap.get(k).isExpired(timeToLive); k = timeCacheItemsQueue.peek()) {
                timeCacheItemsMap.remove(k);
                timeCacheItemsQueue.remove();
            }
        }

        private void autoCheckCacheItems() {
            while (!Thread.interrupted()) {
                try {
                    sleep(checkTime);
                } catch (InterruptedException ex) {
                    LOGGER.log(Level.WARNING, "Interrupted!", ex);
                    Thread.currentThread().interrupt();
                }
                checkCacheItemsTimeLive();
            }
        }
    }

}
