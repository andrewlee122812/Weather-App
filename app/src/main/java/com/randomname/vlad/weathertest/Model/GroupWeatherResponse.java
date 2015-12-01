package com.randomname.vlad.weathertest.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class GroupWeatherResponse extends RealmObject{
    public GroupWeatherResponse() {
    }

    private RealmList<BaseResponse> list;

    public void setList(RealmList<BaseResponse> list) {
        this.list = list;
    }

    public RealmList<BaseResponse> getList() {
        return list;
    }
}
