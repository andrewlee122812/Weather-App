package com.randomname.vlad.weathertest.Model;

import io.realm.RealmObject;

public class Temp extends RealmObject{
    public Temp() {
    }

    private float day = 0f;
    private float min = 0f;
    private float max = 0f;
    private float night = 0f;
    private float eve = 0f;
    private float morn = 0f;

    public void setDay(float day) {
        this.day = day;
    }

    public float getDay() {
        return day;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMin() {
        return min;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMax() {
        return max;
    }

    public void setNight(float night) {
        this.night = night;
    }

    public float getNight() {
        return night;
    }

    public void setEve(float eve) {
        this.eve = eve;
    }

    public float getEve() {
        return eve;
    }

    public void setMorn(float morn) {
        this.morn = morn;
    }

    public float getMorn() {
        return morn;
    }
}
