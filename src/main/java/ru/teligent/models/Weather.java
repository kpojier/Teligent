package ru.teligent.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Weather information
 * @author Max Zhuravlov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    @JsonProperty("name")
    private String cityName;
    @JsonProperty("main")
    private TemperatureInfo tempInfo;
    @JsonProperty("sys")
    private SystemInfo sysInfo;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public TemperatureInfo getTempInfo() {
        return tempInfo;
    }

    public void setTempInfo(TemperatureInfo tempInfo) {
        this.tempInfo = tempInfo;
    }

    public SystemInfo getSysInfo() {
        return sysInfo;
    }

    public void setSysInfo(SystemInfo sysInfo) {
        this.sysInfo = sysInfo;
    }
}
