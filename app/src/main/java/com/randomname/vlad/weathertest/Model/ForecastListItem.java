package com.randomname.vlad.weathertest.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ForecastListItem extends RealmObject{
    public ForecastListItem() {
    }

    private long dt = 0;
    private Temp temp;
    private float pressure = 0f;
    private int humidity = 0;
    private RealmList<Weather> weather;
    private float speed = 0f;
    private float deg = 0f;
    private int clouds = 0;

    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getDt() {
        return dt;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getPressure() {
        return pressure;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setWeather(RealmList<Weather> weather) {
        this.weather = weather;
    }

    public RealmList<Weather> getWeather() {
        return weather;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDeg() {
        return deg;
    }

    public void setDeg(float deg) {
        this.deg = deg;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getClouds() {
        return clouds;
    }
}
