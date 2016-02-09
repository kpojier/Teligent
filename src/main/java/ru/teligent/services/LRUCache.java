package ru.teligent.services;

import ru.teligent.models.WeatherResponse;

/**
 * Least recently used cache interface
 * @author Max Zhuravlov
 */
public interface LRUCache<K, V extends WeatherResponse> {

    boolean containsActual(K cacheKey);
    V put(K key, V value);
    V get(K key);
}
