package com.randomname.vlad.weathertest.Model;

import io.realm.RealmObject;

public class Weather extends RealmObject{

    public Weather() {
    }

    private String main = "";
    private String description = "";
    private String icon = "";


    public void setMain(String main) {
        this.main = main;
    }

    public String getMain() {
        return main;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
