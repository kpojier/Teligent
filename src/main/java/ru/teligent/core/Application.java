package ru.teligent.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.teligent.models.WeatherResponse;
import ru.teligent.services.LRUCache;
import ru.teligent.services.RestWeatherLoader;
import ru.teligent.services.WeatherLoader;

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

    /**
     * App logger
     */
    private static final Logger log = LoggerFactory.getLogger(Application.class);

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
        RestWeatherLoader restWeatherLoader = new RestWeatherLoader();
        return restWeatherLoader;
    }

    @Bean
    public LRUCache<WeatherResponse> weatherResponseLRUCache() {
        System.out.println("Generate cache "+cacheSize);
        LRUCache<WeatherResponse> cache = new LRUCache<>(cacheSize);
        return cache;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
