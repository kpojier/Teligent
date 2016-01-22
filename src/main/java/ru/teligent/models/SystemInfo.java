package ru.teligent.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Weather system info
 * @author Max Zhuravlov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemInfo {

    private long id;
    private String country;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
