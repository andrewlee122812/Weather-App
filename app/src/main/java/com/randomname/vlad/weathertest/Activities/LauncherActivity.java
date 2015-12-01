package com.randomname.vlad.weathertest.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.randomname.vlad.weathertest.MainActivity;
import com.randomname.vlad.weathertest.Model.City;
import com.randomname.vlad.weathertest.R;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        initRealm();
    }

    private void initRealm() {
        Realm realm = Realm.getInstance(this);
        RealmQuery<City> query = realm.where(City.class);
        City city;

        RealmResults<City> result = query.findAll();

        if (result.size() == 0) {
            realm.beginTransaction();
            city = realm.createObject(City.class);
            city.setDisplayName("Москва");
            city.setId(524901);
            city.setName("Moscow");

            city = realm.createObject(City.class);
            city.setDisplayName("Санкт-Петербург");
            city.setId(519690);
            city.setName("Novaya Gollandiya");
            realm.commitTransaction();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
