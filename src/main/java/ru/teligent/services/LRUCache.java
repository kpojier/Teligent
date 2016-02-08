package ru.teligent.services;

import org.springframework.stereotype.Component;
import ru.teligent.models.WeatherResponse;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Last Recent User cache
 * @author Max Zhuravlov
 */
@Component
public class LRUCache<T extends WeatherResponse> extends LinkedHashMap<String,T> {

    private int  cacheSize;
    private long liveTime;

    public LRUCache(int size, long liveTime) {
        super(size, 0.75f, true);
        this.cacheSize = size;
        this.liveTime  = liveTime * 1000;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, T> eldest) {
        return size() > cacheSize;
    }

    public boolean containsActual(String key) {
        T value = this.get(key);
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
