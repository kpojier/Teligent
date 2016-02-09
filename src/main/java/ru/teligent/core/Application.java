package ru.teligent.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.teligent.models.WeatherResponse;
import ru.teligent.services.*;

import javax.servlet.Filter;

/**
 *
 * @author Max Zhuravlov
 */
@SpringBootApplication
@ComponentScan("ru.teligent.controllers")
@PropertySource("classpath:app.properties")
public class Application {

    @Value("${cache.size}")
    private int cacheSize;
    @Value("${cache.live}")
    private long cacheLiveTime;

    /**
     * Enter point
     * @param args arguments params
     */
    public static void main(String args[]) {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(Application.class)
                .run(args);
    }

    /**
     * Get weather loader service
     * @return configured loader service
     */
    @Bean
    public WeatherLoader restWeatherLoader() {
        return new RestWeatherLoader();
    }

    @Bean
    public LRUCache<String, WeatherResponse> weatherResponseLRUCache() {
        return new LRUCacheConcurrencyImplementation<>(cacheSize, cacheLiveTime);
    }

    @Bean
    public Filter cacheFilter() {
        return new CacheFilter();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
