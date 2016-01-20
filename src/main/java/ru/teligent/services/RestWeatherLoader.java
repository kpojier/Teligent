package ru.teligent.services;

import org.springframework.web.client.RestTemplate;
import ru.teligent.models.Weather;

/**
 * Weather loader
 * @author Max Zhuravlov
 */
public class RestWeatherLoader extends RestTemplate {

    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s,%s&mode=json&units=metric&appid=2de143494c0b295cca9337e1e96b00e0";

    public Weather loadCurrentWeather(String cityName, String countryCode) {
        return getForObject(String.format(WEATHER_URL, cityName, countryCode), Weather.class);
    }
}
