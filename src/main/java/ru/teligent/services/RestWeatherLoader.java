package ru.teligent.services;

import org.springframework.web.client.RestTemplate;
import ru.teligent.models.Weather;
import ru.teligent.models.WeatherForecast;

/**
 * Weather loader
 * @author Max Zhuravlov
 */
public class RestWeatherLoader extends RestTemplate implements WeatherLoader {

    private static final String WEATHER_URL  = "http://api.openweathermap.org/data/2.5/weather?q=%s,%s&mode=json&units=metric&appid=f949dad4641e63f6a36bd7f9fe26589b";
    private static final String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s,%s&mode=json&appid=f949dad4641e63f6a36bd7f9fe26589b";

    @Override
    public Weather loadCurrentWeather(String cityName, String countryCode) {
        return getForObject(String.format(WEATHER_URL, cityName, countryCode), Weather.class);
    }

    @Override
    public WeatherForecast loadWeatherForecast(String cityName, String countryCode) {
        return getForObject(String.format(FORECAST_URL, cityName, countryCode), WeatherForecast.class);
    }
}
