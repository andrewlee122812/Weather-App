package com.randomname.vlad.weathertest.Model;

import io.realm.RealmObject;

public class Main extends RealmObject{

    public Main() {
    }

    private float temp = 0f;
    private float pressure = 0f;
    private int humidity = 0;
    private float temp_min = 0f;
    private float temp_max = 0f;

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getTemp() {
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

    public void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    public float getTemp_max() {
        return temp_max;
    }

}
