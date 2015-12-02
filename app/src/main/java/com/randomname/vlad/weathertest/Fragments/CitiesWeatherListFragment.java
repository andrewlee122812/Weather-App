package com.randomname.vlad.weathertest.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.randomname.vlad.weathertest.API.RestClient;
import com.randomname.vlad.weathertest.Activities.AddCityActivity;
import com.randomname.vlad.weathertest.Activities.DetailActivity;
import com.randomname.vlad.weathertest.Adapters.CitiesWeatherAdapter;
import com.randomname.vlad.weathertest.Model.BaseResponse;
import com.randomname.vlad.weathertest.Model.City;
import com.randomname.vlad.weathertest.Model.GroupWeatherResponse;
import com.randomname.vlad.weathertest.R;
import com.randomname.vlad.weathertest.Views.SpaceItemDecorator;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CitiesWeatherListFragment extends Fragment{
    private static final String TAG = "cities weather list";

    @Bind(R.id.cities_fragment_recycler_view)
    RecyclerView citiesRecyclerView;

    private String cities = ""; // here we have our cities id's
    private HashMap<Long, String> nameHashMap; // hash map with city id | city name. Bad, but don't know how to do it better

    private CitiesWeatherAdapter adapter;
    private ArrayList<BaseResponse> baseResponseArrayList;

    private Realm realm;

    private SharedPreferences.OnSharedPreferenceChangeListener mSharedPrefsChangeListener;

    public CitiesWeatherListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseResponseArrayList = new ArrayList<>();
        realm = Realm.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cities_list_fragment, container, false);
        ButterKnife.bind(this, view);

        adapter = new CitiesWeatherAdapter(getActivity(), baseResponseArrayList);
        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = citiesRecyclerView.getChildAdapterPosition(v);
                BaseResponse baseResponse = adapter.getVisibleObject(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.BASE_RESPONSE_EXTRA, baseResponse.getId());
                startActivity(intent);
            }
        });

        citiesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        citiesRecyclerView.setAdapter(adapter);
        citiesRecyclerView.addItemDecoration(new SpaceItemDecorator(4));

        mSharedPrefsChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                adapter.notifyDataChanged();
            }
        };

        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(mSharedPrefsChangeListener);

        getCitiesFromRealm();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void setSearchQuery(String string) {
        adapter.setFilter(string);
    }

    public void flushFilter() {
        adapter.flushFilter();
    }

    private void getCitiesFromRealm() {
        cities = "";

        RealmQuery<City> query = realm.where(City.class);
        RealmResults<City> result = query.findAll();
        nameHashMap = new HashMap<>();

        for (City city : result) {
            cities += city.getId() + ",";
            nameHashMap.put(city.getId(), city.getDisplayName());
        }

        loadWeatherFromRealm();
        loadWeatherViaNetwork();
    }

    private void loadWeatherFromRealm() {
        RealmQuery<BaseResponse> query = realm.where(BaseResponse.class);
        RealmResults<BaseResponse> result = query.findAll();

        baseResponseArrayList.clear();
        baseResponseArrayList.addAll(result);
        adapter.notifyDataChanged();
    }

    private void loadWeatherViaNetwork() {
        RestClient.getInstance(getActivity()).getGroupWeather(cities, new Callback<GroupWeatherResponse>() {
            @Override
            public void success(GroupWeatherResponse groupWeatherResponse, Response response) {

                for (BaseResponse baseResponse : groupWeatherResponse.getList()) {
                    baseResponse.setDisplayName(nameHashMap.get(baseResponse.getId()));
                }

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(groupWeatherResponse.getList());
                realm.commitTransaction();

                baseResponseArrayList.clear();
                baseResponseArrayList.addAll(groupWeatherResponse.getList());
                adapter.notifyDataChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.toString());
            }
        });
    }
}
