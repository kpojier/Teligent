package ru.teligent.services;

import ru.teligent.models.Weather;
import ru.teligent.models.WeatherForecast;

/**
 * Weather loader
 * @author Max Zhuravlov
 */
public interface WeatherLoader {

    Weather loadCurrentWeather(String cityName, String countryCode);
    WeatherForecast loadWeatherForecast(String cityName, String countryCode);
}
