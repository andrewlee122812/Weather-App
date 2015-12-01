package com.randomname.vlad.weathertest.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.randomname.vlad.weathertest.Model.BaseResponse;
import com.randomname.vlad.weathertest.Model.City;
import com.randomname.vlad.weathertest.R;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DetailActivity extends AppCompatActivity {

    public final static String BASE_RESPONSE_EXTRA = "city id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

    }
}
