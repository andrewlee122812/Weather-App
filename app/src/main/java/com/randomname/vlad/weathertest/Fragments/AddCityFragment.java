package com.randomname.vlad.weathertest.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.randomname.vlad.weathertest.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddCityFragment extends Fragment{

    private final static String TAG = "add city fragment";

    @Bind(R.id.city_name_edit_text)
    AutoCompleteTextView cityNameEditText;

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    public AddCityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_city_fragment, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);

        cityNameEditText.setAdapter(adapter);

        return view;
    }
}
