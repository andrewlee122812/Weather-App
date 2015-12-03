package com.randomname.vlad.weathertest.Activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.randomname.vlad.weathertest.R;
import com.randomname.vlad.weathertest.Views.CachingUrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends DrawerBaseActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private String OWM_TILE_URL =  "http://tile.openweathermap.org/map/%s/%d/%d/%d.png";
    private String[] tileOverlayValues;
    private TileOverlay tileOverlay;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tile_type_spinner)
    Spinner tileTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        tileOverlayValues = getResources().getStringArray(R.array.map_tile_values);
        initToolbar();
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void initToolbar() {
        super.initDrawer(toolbar, getClass().getSimpleName());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ArrayAdapter mAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.map_tile_names, android.R.layout.simple_spinner_dropdown_item);
        mAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        tileTypeSpinner.setAdapter(mAdapter);
        tileTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tileOverlay.remove();
                changeMapOverlay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        changeMapOverlay();
    }

    private void changeMapOverlay() {
        tileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(createTileProvider()));
    }

    private TileProvider createTileProvider() {
        TileProvider tileProvider = new CachingUrlTileProvider(this, 256, 256) {
            @Override
            public String getTileUrl(int x, int y, int z) {
                return String.format(OWM_TILE_URL, tileOverlayValues[tileTypeSpinner.getSelectedItemPosition()], z, x, y);
            }
        };

        return tileProvider;
    }
}
