package ru.teligent.services;

import org.springframework.web.client.RestTemplate;
import ru.teligent.models.Weather;

/**
 * Weather loader
 * @author Max Zhuravlov
 */
public class RestWeatherLoader extends RestTemplate {

    public Weather loadCurrentWeather(String cityName, String countryCode) {
        return getForObject("http://api.openweathermap.org/data/2.5/weather?q=KRASNODAR,RU&mode=json&units=metric&appid=2de143494c0b295cca9337e1e96b00e0", Weather.class);
    }
}
