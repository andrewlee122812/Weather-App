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
import com.randomname.vlad.weathertest.Activities.DetailActivity;
import com.randomname.vlad.weathertest.MainActivity;
import com.randomname.vlad.weathertest.Model.BaseResponse;
import com.randomname.vlad.weathertest.Model.Forecast;
import com.randomname.vlad.weathertest.Model.ForecastListItem;
import com.randomname.vlad.weathertest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
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

    private void updateHeaderUI(BaseResponse baseResponse) {
        cityTextView.setText(baseResponse.getDisplayName());
    }

    private void getWeatherInfo() {
        Realm realm = Realm.getInstance(getActivity());
        RealmQuery<BaseResponse> query = realm.where(BaseResponse.class);

        query.equalTo("id", getActivity().getIntent().getLongExtra(DetailActivity.BASE_RESPONSE_EXTRA, 0));

        RealmResults<BaseResponse> baseResponses = query.findAll();
        if (baseResponses.size() > 0) {
            updateHeaderUI(baseResponses.get(0));
        }
    }
}
