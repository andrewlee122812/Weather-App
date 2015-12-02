package com.randomname.vlad.weathertest.Model;

import io.realm.RealmObject;

public class Sys extends RealmObject {
    public Sys() {
    }

    private String country;
    private long sunrise;
    private long sunset;

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public long getSunset() {
        return sunset;
    }
}
