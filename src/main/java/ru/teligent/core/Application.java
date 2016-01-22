package ru.teligent.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
public class Application {

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
        LRUCache<WeatherResponse> cache = new LRUCache<>(20);
        return cache;
    }

}
