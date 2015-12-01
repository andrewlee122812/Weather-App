package com.randomname.vlad.weathertest.Model;

import io.realm.RealmObject;

public class Clouds extends RealmObject{
    public Clouds() {
    }

    private int all = 0;

    public void setAll(int all) {
        this.all = all;
    }

    public int getAll() {
        return all;
    }
}
