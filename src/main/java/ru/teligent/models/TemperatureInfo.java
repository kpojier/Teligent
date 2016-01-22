package ru.teligent.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Temperature Information.
 * Contain maximum, minimum at the moment and current temperature.
 * Unit in Celsius.
 * @author Max Zhuravlov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemperatureInfo {

    private double temp;
    @JsonProperty("temp_max")
    private double maxTemp;
    @JsonProperty("temp_min")
    private double minTemp;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }
}
