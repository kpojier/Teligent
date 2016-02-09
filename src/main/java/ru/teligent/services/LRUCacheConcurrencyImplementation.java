package ru.teligent.services;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import ru.teligent.models.WeatherResponse;

/**
 * Concurrency implementation of LRU Cache
 * @author Max Zhuravlov
 */
public class LRUCacheConcurrencyImplementation<K, V extends WeatherResponse> implements LRUCache<K, V> {

    private ConcurrentLinkedHashMap<K, V> map;
    private long liveTime;

    public LRUCacheConcurrencyImplementation(long cacheSize, long liveTime) {
        this.liveTime = liveTime;
        this.map = new ConcurrentLinkedHashMap.Builder<K, V>()
                    .maximumWeightedCapacity(cacheSize)
                    .build();
    }


    @Override
    public boolean containsActual(Object cacheKey) {
        V value = this.map.get(cacheKey);
        if (value == null) {
            return false;
        } else if (System.currentTimeMillis() - value.getTimestamp() > liveTime) {
            this.map.remove(cacheKey);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public V put(K key, V value) {
        return this.map.put(key, value);
    }

    @Override
    public V get(K key) {
        return this.map.get(key);
    }
}
