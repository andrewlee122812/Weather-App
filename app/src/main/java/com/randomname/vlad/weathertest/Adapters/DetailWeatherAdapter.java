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
import com.randomname.vlad.weathertest.Model.ForecastListItem;
import com.randomname.vlad.weathertest.Model.Weather;
import com.randomname.vlad.weathertest.Model.Wind;
import com.randomname.vlad.weathertest.R;
import com.randomname.vlad.weathertest.Util.Misc;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class DetailWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_VIEW_HEADER = Integer.MAX_VALUE;
    private int sizeDiff = 0;
    private Context mContext;
    private List<ForecastListItem> forecastListItems;
    private BaseResponse baseResponseHeaderInfo = null;
    private int unitType;
    private String unitString;


    public DetailWeatherAdapter(Context context, List<ForecastListItem> forecastListItems) {
        this.mContext = context;
        this.forecastListItems = forecastListItems;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        unitType = Integer.parseInt(sharedPref.getString("pressure_units", "1"));
        unitString = mContext.getResources().getStringArray(R.array.pressure_settings_entries)[unitType - 1];
    }

    public DetailWeatherAdapter(Context context, List<ForecastListItem> forecastListItems, BaseResponse baseResponse) {
        this.mContext = context;
        this.forecastListItems = forecastListItems;
        this.baseResponseHeaderInfo = baseResponse;
        sizeDiff = 1;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        unitType = Integer.parseInt(sharedPref.getString("pressure_units", "1"));
        unitString = mContext.getResources().getStringArray(R.array.pressure_settings_entries)[unitType - 1];
    }


    public void setHeaderInfo(BaseResponse baseResponse) {
        baseResponseHeaderInfo = baseResponse;
        sizeDiff = 1;
    }

    @Override
    public long getItemId(int position) {
        if (hasStableIds()) {
            return 10 + position;
        } else {
            return super.getItemId(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && sizeDiff > 0) {
            return TYPE_VIEW_HEADER;
        }
        return position - sizeDiff;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_VIEW_HEADER && sizeDiff > 0) {
            return onCreateHeaderViewHolder(viewGroup);
        } else {
            return onCreateMainViewHolder(viewGroup, i);
        }
    }

    private RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header_forecast_item, null);

        HeaderViewHolder viewHolder = new HeaderViewHolder(view);
        return viewHolder;
    }

    private RecyclerView.ViewHolder onCreateMainViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forecast_list_item, null);

        ForecastViewHolder viewHolder = new ForecastViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_VIEW_HEADER) {
            onBindHeaderViewHolder(holder);
        } else {
            onBindMainViewHolder(holder, position - sizeDiff);
        }
    }

    private void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        if (baseResponseHeaderInfo == null) {
            return;
        }

        HeaderViewHolder customViewHolder = (HeaderViewHolder) holder;

        String name = baseResponseHeaderInfo.getDisplayName();
        String sunRiseString = DateFormat.format("kk:mm", new Date(baseResponseHeaderInfo.getSys().getSunrise() * 1000)).toString();
        String sunSetString = DateFormat.format("kk:mm", new Date(baseResponseHeaderInfo.getSys().getSunset() * 1000)).toString();
        List<Weather> weatherList = baseResponseHeaderInfo.getWeather();
        String description = "";
        int iconResId = 0;
        String temperature = Math.round(baseResponseHeaderInfo.getMain().getTemp()) + " \u2103";
        String pressureString = "";
        String humidity = baseResponseHeaderInfo.getMain().getHumidity() + " %";
        String windString = "";
        Wind wind = baseResponseHeaderInfo.getWind();

        if (weatherList.size() > 0) {
            Weather weather = weatherList.get(0);

            description = weather.getDescription();

            if (!weather.getIcon().isEmpty()) {
                iconResId = Misc.getImageResource(weather.getIcon(), mContext);
            }
        }

        switch (unitType) {
            case 1:
                pressureString += Math.round(baseResponseHeaderInfo.getMain().getPressure());
                break;
            case 2:
                pressureString += Math.round(baseResponseHeaderInfo.getMain().getPressure() * 0.750062);
                break;
            default:
                pressureString += Math.round(baseResponseHeaderInfo.getMain().getPressure());
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

    private void onBindMainViewHolder(RecyclerView.ViewHolder holder, int position) {
        ForecastViewHolder viewHolder = (ForecastViewHolder) holder;
        ForecastListItem item = forecastListItems.get(position);

        String dateString = DateFormat.format("dd MMMM", new Date(item.getDt() * 1000)).toString();
        String description = "";
        int iconResId = 0;
        String temperature = Math.round((item.getTemp().getMax() + item.getTemp().getMin()) / 2) + " \u2103";
        String pressureString = "";
        String humidity = item.getHumidity() + " %";
        String windString = "";

        if (item.getWeather().size() > 0) {
            Weather weather = item.getWeather().get(0);

            description = weather.getDescription();

            if (!weather.getIcon().isEmpty()) {
                iconResId = Misc.getImageResource(weather.getIcon(), mContext);
            }
        }

        switch (unitType) {
            case 1:
                pressureString += Math.round(item.getPressure());
                break;
            case 2:
                pressureString += Math.round(item.getPressure() * 0.750062);
                break;
            default:
                pressureString += Math.round(item.getPressure());
        }

        double d = item.getSpeed();
        if ((d - (int)d)!= 0) {
            windString += String.format("%.1f", d) + " " + mContext.getString(R.string.wind_units);
        } else {
            windString += Math.round(d) + " " + mContext.getString(R.string.wind_units);
        }

        pressureString += " " + unitString;

        viewHolder.dateTextView.setText(dateString);
        viewHolder.morningTempTextView.setText(Math.round(item.getTemp().getMorn()) + " \u2103");
        viewHolder.dayTempTextView.setText(Math.round(item.getTemp().getDay()) + " \u2103");
        viewHolder.eveningTempTextView.setText(Math.round(item.getTemp().getEve()) + " \u2103");
        viewHolder.nightTempTextView.setText(Math.round(item.getTemp().getNight()) + " \u2103");
        viewHolder.descriptionTextView.setText(description);
        viewHolder.temperatureTextView.setText(temperature);
        viewHolder.pressureTextView.setText(pressureString);
        viewHolder.humidityTextView.setText(humidity);
        viewHolder.windTextView.setText(windString);

        if (iconResId > 0) {
            Picasso.with(mContext).load(iconResId).into(viewHolder.weatherIconImageView);
        } else {
            Picasso.with(mContext).load(R.drawable.unknown).into(viewHolder.weatherIconImageView);
        }
    }

    @Override
    public int getItemCount() {
        return (null != forecastListItems ? forecastListItems.size() + sizeDiff : 0);
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        protected TextView dateTextView, eveningTempTextView, morningTempTextView, dayTempTextView, nightTempTextView;
        protected TextView descriptionTextView, temperatureTextView, pressureTextView, humidityTextView, windTextView;
        protected ImageView weatherIconImageView;

        public ForecastViewHolder(View view) {
            super(view);
            dateTextView = (TextView) view.findViewById(R.id.date_text_view);
            eveningTempTextView = (TextView) view.findViewById(R.id.evening_temp_text_view);
            morningTempTextView = (TextView) view.findViewById(R.id.morning_temp_text_view);
            dayTempTextView = (TextView) view.findViewById(R.id.day_temp_text_view);
            nightTempTextView = (TextView) view.findViewById(R.id.night_temp_text_view);
            descriptionTextView = (TextView) view.findViewById(R.id.description_text_view);
            temperatureTextView = (TextView) view.findViewById(R.id.temperature_text_view);
            weatherIconImageView = (ImageView) view.findViewById(R.id.weather_icon_image_view);
            pressureTextView = (TextView) view.findViewById(R.id.pressure_text_view);
            humidityTextView = (TextView) view.findViewById(R.id.humidity_text_view);
            windTextView = (TextView) view.findViewById(R.id.wind_text_view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        protected TextView cityName, descriptionTextView, temperatureTextView, pressureTextView, humidityTextView;
        protected TextView windTextView, sunRiseTextView, sunSetTextView;
        protected ImageView weatherIcon;

        public HeaderViewHolder(View view) {
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
