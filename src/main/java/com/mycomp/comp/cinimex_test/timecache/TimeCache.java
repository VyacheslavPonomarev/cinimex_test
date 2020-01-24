package com.mycomp.comp.cinimex_test.timecache;

import java.time.Duration;

public interface TimeCache<K, V> {
    void add(K key, V value);
    V get(K key);
    int getSize();
    void remove(K key);
    void clear();
    Long getItemAddTime(K key);
    void updateItemAddTime(Duration addTime, K key);
    Long getTimeToLive();
    void setTimeToLive(Long timeToLive);
}
