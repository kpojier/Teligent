package ru.teligent.services;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Last Recent User cache
 * @author Max Zhuravlov
 */
public class LRUCache<T> extends LinkedHashMap<String,T> {

    private int cacheSize;

    public LRUCache(int size) {
        super(size, 0.75f, true);
        this.cacheSize = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, T> eldest) {
        return size() > cacheSize;
    }
}
