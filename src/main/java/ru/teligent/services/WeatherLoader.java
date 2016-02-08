package ru.teligent.services;

import org.springframework.stereotype.Component;
import ru.teligent.models.Weather;
import ru.teligent.models.WeatherForecast;

/**
 * Weather loader
 * @author Max Zhuravlov
 */
@Component
public interface WeatherLoader {

    Weather loadCurrentWeather(String cityName, String countryCode);
    WeatherForecast loadWeatherForecast(String cityName, String countryCode);
}
