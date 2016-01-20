package ru.teligent.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Forecast item for weather forecast
 * @author Max Zhuravlov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastItem {

    @JsonProperty("dt")
    private long timestamp;
    @JsonProperty("main")
    private TemperatureInfo tempInfo;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public TemperatureInfo getTempInfo() {
        return tempInfo;
    }

    public void setTempInfo(TemperatureInfo tempInfo) {
        this.tempInfo = tempInfo;
    }
}
