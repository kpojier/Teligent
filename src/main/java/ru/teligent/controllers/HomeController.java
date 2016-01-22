package ru.teligent.controllers;

import org.json.JSONObject;
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
import ru.teligent.services.RestWeatherLoader;
import ru.teligent.services.WeatherLoader;

import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;

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


    @RequestMapping(value = "weather/{country}/{city}", method = RequestMethod.GET)
    @ResponseBody
    public void getWeather(HttpServletResponse response,
                             @PathVariable String country,
                             @PathVariable String city) {
        try {
            response.setContentType("application/json");

            // Load weather
            Weather weather = loader.loadCurrentWeather(city, country);
            JSONObject jsonResponse = new JSONObject();
            if (!weather.getCityName().equalsIgnoreCase(city) || !weather.getSysInfo().getCountry().equalsIgnoreCase(country)) {
                jsonResponse.put("state", HttpStatus.BAD_REQUEST.value());
                jsonResponse.put("errorMessage","Can't find "+city+" in database. Wrong city name or country code");
                return;
            }
            // Load forecast
            WeatherForecast forecast = loader.loadWeatherForecast(city, country);
            ForecastItem minForecast = forecast.getForecastsList().stream()
                    .min((o1, o2) -> Double.compare(o1.getTempInfo().getTemp(), o2.getTempInfo().getTemp()))
                    .get();

            jsonResponse.put("city"   , weather.getCityName());
            jsonResponse.put("country", weather.getSysInfo().getCountry());
            jsonResponse.put("currentTemp", weather.getTempInfo().getTemp());
            jsonResponse.put("forecastTemp", minForecast.getTempInfo().getTemp());
            jsonResponse.put("state", 200);
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }
}
