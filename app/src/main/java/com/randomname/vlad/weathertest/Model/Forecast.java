package com.randomname.vlad.weathertest.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Forecast extends RealmObject{
    public Forecast() {
    }

    private RealmList<ForecastListItem> list;

    public void setList(RealmList<ForecastListItem> list) {
        this.list = list;
    }

    public RealmList<ForecastListItem> getList() {
        return list;
    }
}
