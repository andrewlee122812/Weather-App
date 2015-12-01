package com.randomname.vlad.weathertest.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject{
    public City() {
    }

    @PrimaryKey
    private long id = 0;
    private String name = "";
    private String displayName = "";

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
}
