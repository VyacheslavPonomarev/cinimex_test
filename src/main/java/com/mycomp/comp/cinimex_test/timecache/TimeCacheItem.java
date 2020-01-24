package com.mycomp.comp.cinimex_test.timecache;

public class TimeCacheItem<V> {
    private V value;
    private Long addTime;

    public TimeCacheItem(V value, Long addTime) {
        this.value = value;
        this.addTime = addTime;
    }

    public boolean isExpired(Long timeToLive) {
        return System.currentTimeMillis() - addTime > timeToLive;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getAddTime() {
        return addTime;
    }

    public V getValue() {
        return value;
    }
}
