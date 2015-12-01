package com.randomname.vlad.weathertest.API;

import com.randomname.vlad.weathertest.Model.BaseResponse;
import com.randomname.vlad.weathertest.Model.Forecast;
import com.randomname.vlad.weathertest.Model.GroupWeatherResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeatherAPI {

    final String CURRENT_WEATHER = "/weather?&lang=ru&units=metric";
    final String GROUP_WEATHER = "/group?&lang=ru&units=metric";
    final String FORECAST = "/forecast/daily?&lang=ru&units=metric";

    @Headers("Content-Type: application/json")
    @GET(CURRENT_WEATHER)
    void getCurrentWeather(
            @Query("q") String city,
            @Query("appId") String appId,
            Callback<BaseResponse> callback
    );

    @Headers("Content-Type: application/json")
    @GET(GROUP_WEATHER)
    void getGroupWeather(
            @Query("id") String citiesGroup,
            @Query("appId") String appId,
            Callback<GroupWeatherResponse> callback
    );

    @Headers("Content-Type: application/json")
    @GET(FORECAST)
    void getForecast(
            @Query("q") String city,
            @Query("appId") String appId,
            @Query("cnt") int dayCount,
            Callback<Forecast> callback
    );
}
