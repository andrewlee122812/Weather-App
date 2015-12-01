package com.randomname.vlad.weathertest.API;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.randomname.vlad.weathertest.Model.BaseResponse;
import com.randomname.vlad.weathertest.Model.Forecast;
import com.randomname.vlad.weathertest.Model.GroupWeatherResponse;
import com.randomname.vlad.weathertest.R;

import javax.security.auth.callback.Callback;

import io.realm.RealmObject;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;

public class RestClient {

    private static final String TAG = "RestClient";
    private static final String BASE_WEATHER_URL = "http://api.openweathermap.org/data/2.5";

    private static RestClient mInstance;
    private static WeatherAPI mApi;
    private static Context mContext;

    public static RestClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RestClient();
        }
        mContext = context;
        return mInstance;
    }

    private RestClient() {

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                }).create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_WEATHER_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog(TAG))
                .build();
        mApi = restAdapter.create(WeatherAPI.class);
    }

    public void getCurrentWeather(String city, retrofit.Callback<BaseResponse> callback) {
        mApi.getCurrentWeather(city, mContext.getString(R.string.weather_api_key), callback);
    }

    public void getForecast(long cityId, int dayCount, retrofit.Callback<Forecast> callback) {
        mApi.getForecast(cityId, mContext.getString(R.string.weather_api_key), dayCount, callback);
    }

    public void getGroupWeather(String citiesGroup, retrofit.Callback<GroupWeatherResponse> callback) {
        mApi.getGroupWeather(citiesGroup, mContext.getString(R.string.weather_api_key), callback);
    }
}
