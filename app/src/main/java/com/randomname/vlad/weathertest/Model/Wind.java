package com.randomname.vlad.weathertest.Model;

import io.realm.RealmObject;

public class Wind extends RealmObject{
    public Wind() {
    }

    private float speed = 0f;
    private float deg = 0f;

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setDeg(float deg) {
        this.deg = deg;
    }

    public float getDeg() {
        return deg;
    }
}
