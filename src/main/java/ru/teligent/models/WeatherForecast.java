package ru.teligent.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Weather forecast
 * @author Max Zhuravlov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecast {

    private City city;
    @JsonProperty("list")
    private List<ForecastItem> forecastsList = new ArrayList<>();

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<ForecastItem> getForecastsList() {
        return forecastsList;
    }

    public void setForecastsList(List<ForecastItem> forecastsList) {
        this.forecastsList = forecastsList;
    }
}
