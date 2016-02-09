package ru.teligent.services;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.springframework.stereotype.Component;
import ru.teligent.models.WeatherResponse;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Last Recent User cache
 * @author Max Zhuravlov
 */
@Component
public class LRUCacheImpl<K, V extends WeatherResponse> extends LinkedHashMap<K, V> implements LRUCache<K, V> {

    private int  cacheSize;
    private long liveTime;

    public LRUCacheImpl(int size, long liveTime) {
        super(size, 0.75f, true);
        this.cacheSize = size;
        this.liveTime  = liveTime * 1000;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > cacheSize;
    }

    public boolean containsActual(K key) {
        V value = this.get(key);
        if (value == null) {
            return false;
        } else if (System.currentTimeMillis() - value.getTimestamp() > liveTime) {
            this.remove(key);
            return false;
        } else {
            return true;
        }
    }

}
