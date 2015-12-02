package com.randomname.vlad.weathertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.randomname.vlad.weathertest.Activities.SettingsActivity;
import com.randomname.vlad.weathertest.Fragments.CitiesWeatherListFragment;
import com.randomname.vlad.weathertest.Views.MaterialSearch.MaterialSearchView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "Main Activity Tag";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;

    private CitiesWeatherListFragment weatherListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        weatherListFragment = (CitiesWeatherListFragment) getSupportFragmentManager().findFragmentByTag("cities_recycler_fragment");

        if (weatherListFragment != null) {
            weatherListFragment.setSearchQuery(searchView.getQuery());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                if (weatherListFragment != null) {
                    weatherListFragment.flushFilter();
                }
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (weatherListFragment != null) {
                    weatherListFragment.setSearchQuery(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (weatherListFragment != null) {
                    weatherListFragment.setSearchQuery(newText);
                }
                return false;
            }
        });
    }
}
