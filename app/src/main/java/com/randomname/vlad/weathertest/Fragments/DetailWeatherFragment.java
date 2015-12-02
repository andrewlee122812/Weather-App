package com.randomname.vlad.weathertest.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.randomname.vlad.weathertest.API.RestClient;
import com.randomname.vlad.weathertest.Activities.DetailActivity;
import com.randomname.vlad.weathertest.Adapters.DetailWeatherAdapter;
import com.randomname.vlad.weathertest.MainActivity;
import com.randomname.vlad.weathertest.Model.BaseResponse;
import com.randomname.vlad.weathertest.Model.Forecast;
import com.randomname.vlad.weathertest.Model.ForecastListItem;
import com.randomname.vlad.weathertest.R;
import com.randomname.vlad.weathertest.Views.SpaceItemDecorator;

import java.util.ArrayList;
import java.util.List;

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

    private long cityId;
    private Realm realm;

    @Bind(R.id.detail_recycler_view)
    RecyclerView recyclerView;

    DetailWeatherAdapter adapter;
    ArrayList<ForecastListItem> forecastListItems;

    public DetailWeatherFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forecastListItems = new ArrayList<>();
        cityId = getActivity().getIntent().getLongExtra(DetailActivity.BASE_RESPONSE_EXTRA, -1);
        realm = Realm.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_weather_fragment, container, false);
        ButterKnife.bind(this, view);

        adapter = new DetailWeatherAdapter(getActivity(), forecastListItems, new BaseResponse());
        adapter.setHasStableIds(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecorator(4));

        getWeatherInfo();

        return view;
    }

    private void updateHeaderUI(BaseResponse baseResponse) {
        adapter.setHeaderInfo(baseResponse);
        adapter.notifyItemChanged(0);
    }

    private void updateForecastUI( List<ForecastListItem> list) {
        forecastListItems.clear();
        forecastListItems.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void getWeatherInfo() {
        RealmQuery<BaseResponse> query = realm.where(BaseResponse.class);

        query.equalTo("id", cityId);

        RealmResults<BaseResponse> baseResponses = query.findAll();
        if (baseResponses.size() > 0) {
            updateHeaderUI(baseResponses.get(0));
            getActivity().setTitle(baseResponses.get(0).getDisplayName());
        }

        getWeatherInfoFromRealm();
        getWeatherInfoViaNetwork();
    }

    private void getWeatherInfoFromRealm() {
        RealmQuery<ForecastListItem> query = realm.where(ForecastListItem.class);
        query.equalTo("cityId", cityId);
        RealmResults<ForecastListItem> result = query.findAll();

        updateForecastUI(result);
    }

    private void getWeatherInfoViaNetwork() {
        RestClient.getInstance(getActivity()).getForecast(cityId, 16, new Callback<Forecast>() {
            @Override
            public void success(Forecast forecast, Response response) {
                List<ForecastListItem> list = forecast.getList();
                ForecastListItem item = null;

                for (int i = 0; i < list.size(); i++) {
                    item = list.get(i);
                    item.setItemId(cityId + "_" + i);
                    item.setCityId(cityId);
                }

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(list);
                realm.commitTransaction();

                updateForecastUI(list);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.toString());
            }
        });
    }


}
