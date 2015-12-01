package com.randomname.vlad.weathertest.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.randomname.vlad.weathertest.API.RestClient;
import com.randomname.vlad.weathertest.Activities.DetailActivity;
import com.randomname.vlad.weathertest.Adapters.CitiesWeatherAdapter;
import com.randomname.vlad.weathertest.Model.BaseResponse;
import com.randomname.vlad.weathertest.Model.City;
import com.randomname.vlad.weathertest.Model.GroupWeatherResponse;
import com.randomname.vlad.weathertest.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    private CitiesWeatherAdapter adapter;
    private ArrayList<BaseResponse> baseResponseArrayList;

    public CitiesWeatherListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseResponseArrayList = new ArrayList<>();
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
                BaseResponse baseResponse = baseResponseArrayList.get(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.BASE_RESPONSE_EXTRA, baseResponse.getId());
                startActivity(intent);
            }
        });

        citiesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        citiesRecyclerView.setAdapter(adapter);

        getCitiesFromRealm();
        return view;
    }

    private void getCitiesFromRealm() {
        Realm realm = Realm.getInstance(getActivity());
        RealmQuery<City> query = realm.where(City.class);
        RealmResults<City> result = query.findAll();
        final HashMap<Long, String> nameHashMap = new HashMap<>();

        String cities = "";

        for (City city : result) {
            cities += city.getId() + ",";
            nameHashMap.put(city.getId(), city.getDisplayName());
        }

        RestClient.getInstance(getActivity()).getGroupWeather(cities, new Callback<GroupWeatherResponse>() {
            @Override
            public void success(GroupWeatherResponse groupWeatherResponse, Response response) {
                Realm realm = Realm.getInstance(getActivity());

                for (BaseResponse baseResponse : groupWeatherResponse.getList()) {
                    baseResponse.setDisplayName(nameHashMap.get(baseResponse.getId()));
                }

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(groupWeatherResponse.getList());
                realm.commitTransaction();

                baseResponseArrayList.addAll(groupWeatherResponse.getList());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.toString());
            }
        });
    }
}
