package ru.teligent.models;

import org.json.JSONObject;

/**
 * Weather response object
 * Contains city, country code, current temp and min temp forecast on 3 days
 * @author Max Zhuravlov
 */
public class WeatherResponse {

    private String city;
    private String country;
    private double currentTemp;
    private double forecastTemp;
    private long timestamp;
    private boolean isCached;

    public WeatherResponse(String city, String country, double currentTemp, double forecastTemp, boolean isCached) {
        this.city = city;
        this.country = country;
        this.currentTemp = currentTemp;
        this.forecastTemp = forecastTemp;
        this.timestamp = System.currentTimeMillis();
        this.isCached = isCached;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public double getForecastTemp() {
        return forecastTemp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isCached() {
        return isCached;
    }

    public void setCached(boolean cached) {
        isCached = cached;
    }

    @Override
    public String toString() {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("city"        , this.city);
        jsonResponse.put("country"     , this.country);
        jsonResponse.put("currentTemp" , this.currentTemp);
        jsonResponse.put("forecastTemp", this.forecastTemp);
        jsonResponse.put("isCached"    , this.isCached);
        return jsonResponse.toString();
    }
}
