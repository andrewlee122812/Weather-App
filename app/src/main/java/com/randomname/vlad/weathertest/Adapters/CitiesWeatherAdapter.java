package com.randomname.vlad.weathertest.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomname.vlad.weathertest.Model.BaseResponse;
import com.randomname.vlad.weathertest.Model.Weather;
import com.randomname.vlad.weathertest.Model.Wind;
import com.randomname.vlad.weathertest.R;
import com.randomname.vlad.weathertest.Util.Misc;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CitiesWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BaseResponse> baseResponseList;
    private List<BaseResponse> visibleObjects;
    private View.OnClickListener onClickListener;
    private String queryString = "";

    public CitiesWeatherAdapter(Context context, List<BaseResponse> baseResponseList) {
        this.mContext = context;
        this.baseResponseList = baseResponseList;

        flushFilter();
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        onClickListener = onItemClickListener;
    }

    public void flushFilter(){
        visibleObjects = new ArrayList<>();

        visibleObjects.addAll(baseResponseList);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {
        visibleObjects = new ArrayList<>();
        queryText = queryText.toLowerCase();
        queryString = queryText;

        for (BaseResponse item: baseResponseList) {
            if ( item.getName().toLowerCase().contains(queryText) ||
                    item.getDisplayName().toLowerCase().contains(queryText)) {
                visibleObjects.add(item);
            }
        }

        notifyDataSetChanged();
    }

    public BaseResponse getVisibleObject(int position) {
        return visibleObjects.get(position);
    }

    public void notifyDataChanged() {
        if (queryString.isEmpty()) {
            flushFilter();
        } else {
            setFilter(queryString);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_list_item, null);

        if (onClickListener != null) {
            view.setOnClickListener(onClickListener);
        }

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseResponse baseResponse = visibleObjects.get(position);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        int unitType = Integer.parseInt(sharedPref.getString("pressure_units", "1"));
        String unitString = mContext.getResources().getStringArray(R.array.pressure_settings_entries)[unitType - 1];

        String name = baseResponse.getDisplayName();
        String sunRiseString = DateFormat.format("kk:mm", new Date(baseResponse.getSys().getSunrise() * 1000)).toString();
        String sunSetString = DateFormat.format("kk:mm", new Date(baseResponse.getSys().getSunset() * 1000)).toString();
        List<Weather> weatherList = baseResponse.getWeather();
        String description = "";
        int iconResId = 0;
        String temperature = Math.round(baseResponse.getMain().getTemp()) + " \u2103";
        String pressureString = "";
        String humidity = baseResponse.getMain().getHumidity() + " %";
        String windString = "";
        Wind wind = baseResponse.getWind();

        if (weatherList.size() > 0) {
            Weather weather = weatherList.get(0);

            description = weather.getDescription();

            if (!weather.getIcon().isEmpty()) {
                iconResId = Misc.getImageResource(weather.getIcon(), mContext);
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
                windString += String.format("%.1f", d) + " " + mContext.getString(R.string.wind_units);
            } else {
                windString += Math.round(d) + " " + mContext.getString(R.string.wind_units);
            }
        }

        pressureString += " " + unitString;

        CustomViewHolder customViewHolder = (CustomViewHolder) holder;
        customViewHolder.cityName.setText(name);
        customViewHolder.descriptionTextView.setText(description);
        customViewHolder.temperatureTextView.setText(temperature);
        customViewHolder.pressureTextView.setText(pressureString);
        customViewHolder.humidityTextView.setText(humidity);
        customViewHolder.windTextView.setText(windString);
        customViewHolder.sunRiseTextView.setText(sunRiseString);
        customViewHolder.sunSetTextView.setText(sunSetString);

        if (iconResId > 0) {
            Picasso.with(mContext).load(iconResId).into(customViewHolder.weatherIcon);
        } else {
            Picasso.with(mContext).load(R.drawable.unknown).into(customViewHolder.weatherIcon);
        }
    }

    @Override
    public int getItemCount() {
        return (null != visibleObjects ? visibleObjects.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView cityName, descriptionTextView, temperatureTextView, pressureTextView, humidityTextView;
        protected TextView windTextView, sunRiseTextView, sunSetTextView;
        protected ImageView weatherIcon;

        public CustomViewHolder(View view) {
            super(view);
            cityName = (TextView) view.findViewById(R.id.city_name_text_view);
            descriptionTextView = (TextView) view.findViewById(R.id.description_text_view);
            temperatureTextView = (TextView) view.findViewById(R.id.temperature_text_view);
            pressureTextView = (TextView) view.findViewById(R.id.pressure_text_view);
            humidityTextView = (TextView) view.findViewById(R.id.humidity_text_view);
            windTextView = (TextView) view.findViewById(R.id.wind_text_view);
            sunRiseTextView = (TextView) view.findViewById(R.id.sunrise_text_view);
            sunSetTextView = (TextView) view.findViewById(R.id.sunset_text_view);
            weatherIcon = (ImageView) view.findViewById(R.id.weather_icon_image_view);
        }
    }
}
