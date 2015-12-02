package com.randomname.vlad.weathertest.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BaseResponse extends RealmObject {

    public BaseResponse() {
    }

    private RealmList<Weather> weather;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private long dt;
    private Sys sys;
    @PrimaryKey
    private long id;
    private String name = "";
    private String displayName = "";
    private String cod = "200";

    public void setWeather(RealmList<Weather> weathers) {
        this.weather = weathers;
    }

    public RealmList<Weather> getWeather() {
        return weather;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Main getMain() {
        return main;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Wind getWind() {
        return wind;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getDt() {
        return dt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Sys getSys() {
        return sys;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }
}
