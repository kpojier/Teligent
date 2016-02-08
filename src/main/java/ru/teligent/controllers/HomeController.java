package ru.teligent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.teligent.models.ForecastItem;
import ru.teligent.models.Weather;
import ru.teligent.models.WeatherForecast;
import ru.teligent.models.WeatherResponse;
import ru.teligent.services.CacheFilter;
import ru.teligent.services.LRUCache;
import ru.teligent.services.WeatherLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Main App controller
 * @author Max Zhuravlov
 */
@Controller
@RequestMapping("")
public class HomeController {

    @Autowired
    WeatherLoader loader;

    /**
     * Get Weather info
     * @return weather info object
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getWeatherInfo() {
        return "";
    }

    /**
     * Test method
     * @return "test" string to check controller health
     */
    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public String getTest() {
        return "test";
    }


    @RequestMapping(value = "{country}/{city}", method = RequestMethod.GET)
    @ResponseBody
    public void getWeather( HttpServletRequest  request,
                            HttpServletResponse response,
                            @PathVariable String country,
                            @PathVariable String city) {
        try {
            response.setContentType("application/json");
            WeatherResponse weatherResponse = (WeatherResponse)request.getAttribute(CacheFilter.REQ_ATT_NAME);
            if (weatherResponse != null) {
                weatherResponse.setCached(true);
            } else {
                // Load weather
                Weather weather = loader.loadCurrentWeather(city, country);
                if (!weather.getCityName().equalsIgnoreCase(city) || !weather.getSysInfo().getCountry().equalsIgnoreCase(country)) {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    response.getWriter().write("Can't find "+city+" in database. Wrong city name or country code");
                    return;
                }
                // Load forecast
                long TIME_THRESHOLD = System.currentTimeMillis() + 1000*60*60*24*3;
                WeatherForecast forecast = loader.loadWeatherForecast(city, country);
                ForecastItem minForecast = forecast.getForecastsList().stream()
                        .filter(item -> item.getTimestamp() <= TIME_THRESHOLD)
                        .min((o1, o2) -> Double.compare(o1.getTempInfo().getTemp(), o2.getTempInfo().getTemp()))
                        .get();

                weatherResponse = new WeatherResponse(
                        weather.getCityName(),
                        weather.getSysInfo().getCountry(),
                        weather.getTempInfo().getTemp(),
                        minForecast.getTempInfo().getTemp(),
                        false);
                request.setAttribute(CacheFilter.RES_ATT_NAME, weatherResponse);
            }

            response.getWriter().write(weatherResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }
}
