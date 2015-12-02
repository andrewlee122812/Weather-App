package com.randomname.vlad.weathertest.Adapters;

import android.content.Context;
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
import com.randomname.vlad.weathertest.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class DetailWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_VIEW_HEADER = Integer.MAX_VALUE;
    private int sizeDiff = 0;
    private Context mContext;
    private List<ForecastListItem> forecastListItems;
    private BaseResponse baseResponseHeaderInfo = null;

    public DetailWeatherAdapter(Context context, List<ForecastListItem> forecastListItems) {
        this.mContext = context;
        this.forecastListItems = forecastListItems;
    }

    public DetailWeatherAdapter(Context context, List<ForecastListItem> forecastListItems, BaseResponse baseResponse) {
        this.mContext = context;
        this.forecastListItems = forecastListItems;
        this.baseResponseHeaderInfo = baseResponse;
        sizeDiff = 1;
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
        String dateString = DateFormat.format("dd MMMM kk:mm", new Date(baseResponseHeaderInfo.getDt() * 1000)).toString();
        List<Weather> weatherList = baseResponseHeaderInfo.getWeather();
        String description = "";
        String iconURL = "";
        String temperature = Math.round(baseResponseHeaderInfo.getMain().getTemp()) + " \u2103";

        if (weatherList.size() > 0) {
            Weather weather = weatherList.get(0);

            description = weather.getDescription();

            if (!weather.getIcon().isEmpty()) {
                iconURL = "http://openweathermap.org/img/w/" + weather.getIcon() + ".png";
            }
        }

        customViewHolder.cityNameTextView.setText(name);
        customViewHolder.dateTextView.setText(dateString);
        customViewHolder.descriptionTextView.setText(description);
        customViewHolder.temperatureTextView.setText(temperature);

        if (!iconURL.isEmpty()) {
            Picasso.with(mContext).load(iconURL).into(customViewHolder.weatherIconImageView);
        } else {
            customViewHolder.weatherIconImageView.setImageResource(android.R.color.transparent);
        }
    }

    private void onBindMainViewHolder(RecyclerView.ViewHolder holder, int position) {
        ForecastViewHolder viewHolder = (ForecastViewHolder) holder;
        ForecastListItem item = forecastListItems.get(position);

        String dateString = DateFormat.format("dd MMMM", new Date(item.getDt() * 1000)).toString();
        String description = "";
        String iconURL = "";
        String temperature = Math.round((item.getTemp().getMax() + item.getTemp().getMin()) / 2) + " \u2103";

        if (item.getWeather().size() > 0) {
            Weather weather = item.getWeather().get(0);

            description = weather.getDescription();

            if (!weather.getIcon().isEmpty()) {
                iconURL = "http://openweathermap.org/img/w/" + weather.getIcon() + ".png";
            }
        }

        viewHolder.dateTextView.setText(dateString);
        viewHolder.morningTempTextView.setText(Math.round(item.getTemp().getMorn()) + " \u2103");
        viewHolder.dayTempTextView.setText(Math.round(item.getTemp().getDay()) + " \u2103");
        viewHolder.eveningTempTextView.setText(Math.round(item.getTemp().getEve()) + " \u2103");
        viewHolder.nightTempTextView.setText(Math.round(item.getTemp().getNight()) + " \u2103");
        viewHolder.descriptionTextView.setText(description);
        viewHolder.temperatureTextView.setText(temperature);

        if (!iconURL.isEmpty()) {
            Picasso.with(mContext).load(iconURL).into(viewHolder.weatherIconImageView);
        } else {
            viewHolder.weatherIconImageView.setImageResource(android.R.color.transparent);
        }
    }

    @Override
    public int getItemCount() {
        return (null != forecastListItems ? forecastListItems.size() + sizeDiff : 0);
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        protected TextView dateTextView, eveningTempTextView, morningTempTextView, dayTempTextView, nightTempTextView;
        protected TextView descriptionTextView, temperatureTextView;
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
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        protected TextView cityNameTextView, dateTextView, descriptionTextView, temperatureTextView;
        protected ImageView weatherIconImageView;

        public HeaderViewHolder(View view) {
            super(view);
            cityNameTextView = (TextView) view.findViewById(R.id.city_name_text_view);
            dateTextView = (TextView) view.findViewById(R.id.date_text_view);
            descriptionTextView = (TextView) view.findViewById(R.id.description_text_view);
            temperatureTextView = (TextView) view.findViewById(R.id.temperature_text_view);
            weatherIconImageView = (ImageView) view.findViewById(R.id.weather_icon_image_view);
        }
    }
}
