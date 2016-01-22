package ru.teligent.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * City model
 * @author Max Zhuravlov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {

    private String name;
    private long id;
    private String country;

    public City() {
    }

    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
