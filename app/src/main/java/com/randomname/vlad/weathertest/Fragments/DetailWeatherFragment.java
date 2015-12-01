package com.randomname.vlad.weathertest.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randomname.vlad.weathertest.API.RestClient;
import com.randomname.vlad.weathertest.MainActivity;
import com.randomname.vlad.weathertest.Model.Forecast;
import com.randomname.vlad.weathertest.Model.ForecastListItem;
import com.randomname.vlad.weathertest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailWeatherFragment extends Fragment{
    private final static String TAG = "detail weather tag";
    private String currentCity = "Moscow";

    @Bind(R.id.city_name)
    TextView cityTextView;

    public DetailWeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_weather_fragment, container, false);
        ButterKnife.bind(this, view);

        getWeatherInfo();

        return view;
    }

    private void updateUI(Forecast forecast) {
        cityTextView.setText(currentCity);

        RealmList<ForecastListItem> forecastListItems = forecast.getList();

        for (ForecastListItem item : forecastListItems) {

        }
    }

    private void getWeatherInfo() {
        RestClient.getInstance(getActivity()).getForecast(currentCity, 16, new Callback<Forecast>() {
            @Override
            public void success(Forecast forecast, Response response) {
                updateUI(forecast);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.toString());
            }
        });
    }
}
