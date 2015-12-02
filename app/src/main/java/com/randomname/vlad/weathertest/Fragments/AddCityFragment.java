package com.randomname.vlad.weathertest.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.randomname.vlad.weathertest.API.RestClient;
import com.randomname.vlad.weathertest.MainActivity;
import com.randomname.vlad.weathertest.Model.BaseResponse;
import com.randomname.vlad.weathertest.Model.City;
import com.randomname.vlad.weathertest.Model.Weather;
import com.randomname.vlad.weathertest.Model.Wind;
import com.randomname.vlad.weathertest.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import carbon.widget.Button;
import io.realm.Realm;
import io.realm.RealmQuery;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddCityFragment extends Fragment{

    private final static String TAG = "add city fragment";
    private final static String DO_REQUEST_STATE = "do_request_state";

    @Bind(R.id.city_name_edit_text)
    EditText cityNameEditText;
    @Bind(R.id.city_info_wrapper)
    RelativeLayout cityInfoWrapper;
    @Bind(R.id.city_name_text_view)
    TextView cityNameTextView;
    @Bind(R.id.country_text_view)
    TextView countryTextView;
    @Bind(R.id.description_text_view)
    TextView descriptionTextView;
    @Bind(R.id.weather_icon_image_view)
    ImageView weatherIconImageView;
    @Bind(R.id.temperature_text_view)
    TextView temperatureTextView;
    @Bind(R.id.pressure_text_view)
    TextView pressureTextView;
    @Bind(R.id.humidity_text_view)
    TextView humidityTextView;
    @Bind(R.id.wind_text_view)
    TextView windTextView;
    @Bind(R.id.sunrise_text_view)
    TextView sunriseTextView;
    @Bind(R.id.sunset_text_view)
    TextView sunsetTextView;
    @Bind(R.id.save_city_button)
    Button saveCityButton;

    private  SharedPreferences sharedPref;
    private boolean doRequest = false;
    private boolean firstChange = true;
    private BaseResponse currentBaseResponse;

    public AddCityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (savedInstanceState != null) {
            doRequest = savedInstanceState.getBoolean(DO_REQUEST_STATE, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_city_fragment, container, false);
        ButterKnife.bind(this, view);

        cityNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    searchCity();
                    return true;
                } else {
                    return false;
                }
            }
        });

        cityNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (firstChange) {
                    firstChange = false;
                } else {
                    hideCityInfo();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!doRequest) {
            ViewHelper.setAlpha(cityInfoWrapper, 0);
            ViewHelper.setAlpha(saveCityButton, 0);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(DO_REQUEST_STATE, doRequest);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (doRequest) {
            searchCity();
        }
    }

    private void searchCity() {
        RestClient.getInstance(getActivity()).getCurrentWeather(cityNameEditText.getText().toString(), new Callback<BaseResponse>() {
            @Override
            public void success(BaseResponse baseResponse, Response response) {
                if (baseResponse.getCod().equals("200")) {
                    showCityInfo();
                    updateCityInfo(baseResponse);
                    currentBaseResponse = baseResponse;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    private void hideCityInfo() {
        AnimatorSet set = new AnimatorSet();
        Animator animator = ObjectAnimator.ofFloat(cityInfoWrapper, "alpha", 0);
        Animator animator2 = ObjectAnimator.ofFloat(saveCityButton, "alpha", 0);
        set.playTogether(animator, animator2);
        set.setDuration(200);
        set.start();
        doRequest = false;
        saveCityButton.setClickable(false);
    }

    private void showCityInfo() {
        AnimatorSet set = new AnimatorSet();
        Animator animator = ObjectAnimator.ofFloat(cityInfoWrapper, "alpha", 1);
        Animator animator2 = ObjectAnimator.ofFloat(saveCityButton, "alpha", 1);
        set.playTogether(animator, animator2);
        set.setDuration(200);
        set.start();
        doRequest = true;
        saveCityButton.setClickable(true);
    }

    private void updateCityInfo(BaseResponse baseResponse) {
        List<Weather> weatherList = baseResponse.getWeather();
        Wind wind = baseResponse.getWind();

        int unitType = Integer.parseInt(sharedPref.getString("pressure_units", "1"));
        String unitString = getActivity().getResources().getStringArray(R.array.pressure_settings_entries)[unitType - 1];

        String cityNameString = baseResponse.getName();
        String countryString = baseResponse.getSys().getCountry();
        String descriptionString = "";
        String iconURL = "";
        String temperatureString = Math.round(baseResponse.getMain().getTemp()) + " \u2103";
        String pressureString = "";
        String humidityString = baseResponse.getMain().getHumidity() + " %";
        String windString = "";
        String sunriseString = DateFormat.format("kk:mm", new Date(baseResponse.getSys().getSunrise() * 1000)).toString();
        String sunsetString = DateFormat.format("kk:mm", new Date(baseResponse.getSys().getSunset() * 1000)).toString();

        if (weatherList.size() > 0) {
            Weather weather = weatherList.get(0);

            descriptionString = weather.getDescription();

            if (!weather.getIcon().isEmpty()) {
                iconURL = "http://openweathermap.org/img/w/" + weather.getIcon() + ".png";
            }
        }

        switch (unitType) {
            case 1:
                pressureString += Math.round(baseResponse.getMain().getPressure());
                break;
            case 2:
                pressureString += Math.round(baseResponse.getMain().getPressure() * 0.750062);
                break;
            default:
                pressureString += Math.round(baseResponse.getMain().getPressure());
        }

        if (wind != null) {
            double d = wind.getSpeed();
            if ((d - (int)d)!= 0) {
                windString += String.format("%.1f", d) + " " + getActivity().getString(R.string.wind_units);
            } else {
                windString += Math.round(d) + " " + getActivity().getString(R.string.wind_units);
            }
        }

        pressureString += " " + unitString;

        cityNameTextView.setText(cityNameString);
        countryTextView.setText(countryString);
        descriptionTextView.setText(descriptionString);
        temperatureTextView.setText(temperatureString);
        pressureTextView.setText(pressureString);
        humidityTextView.setText(humidityString);
        windTextView.setText(windString);
        sunriseTextView.setText(sunriseString);
        sunsetTextView.setText(sunsetString);

        if (!iconURL.isEmpty()) {
            Picasso.with(getActivity()).load(iconURL).into(weatherIconImageView);
        } else {
            weatherIconImageView.setImageResource(android.R.color.transparent);
        }
    }

    @OnClick(R.id.save_city_button)
    public void onSaveCityClick() {
        if (currentBaseResponse == null) {
            return;
        }

        Realm realm = Realm.getInstance(getActivity());

        realm.beginTransaction();

        City city = realm.createObject(City.class);
        city.setDisplayName(cityNameEditText.getText().toString());
        city.setId(currentBaseResponse.getId());
        city.setName(currentBaseResponse.getName());

        realm.commitTransaction();
        realm.close();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
