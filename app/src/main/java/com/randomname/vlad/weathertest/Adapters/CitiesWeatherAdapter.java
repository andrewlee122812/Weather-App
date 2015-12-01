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
import com.randomname.vlad.weathertest.Model.City;
import com.randomname.vlad.weathertest.Model.Weather;
import com.randomname.vlad.weathertest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CitiesWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BaseResponse> baseResponseList;
    private View.OnClickListener onClickListener;

    public CitiesWeatherAdapter(Context context, List<BaseResponse> baseResponseList) {
        this.mContext = context;
        this.baseResponseList = baseResponseList;
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        onClickListener = onItemClickListener;
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
        BaseResponse baseResponse = baseResponseList.get(position);

        String name = baseResponse.getDisplayName();
        String dateString = DateFormat.format("dd MMMM kk:mm", new Date(baseResponse.getDt() * 1000)).toString();
        List<Weather> weatherList = baseResponse.getWeather();
        String description = "";
        String iconURL = "";
        String temperature = Math.round(baseResponse.getMain().getTemp()) + " \u2103";

        if (weatherList.size() > 0) {
            Weather weather = weatherList.get(0);

            description = weather.getDescription();

            if (!weather.getIcon().isEmpty()) {
                iconURL = "http://openweathermap.org/img/w/" + weather.getIcon() + ".png";
            }
        }

        CustomViewHolder customViewHolder = (CustomViewHolder) holder;
        customViewHolder.cityName.setText(name);
        customViewHolder.dateTextView.setText(dateString);
        customViewHolder.descriptionTextView.setText(description);
        customViewHolder.temperatureTextView.setText(temperature);

        if (!iconURL.isEmpty()) {
            Picasso.with(mContext).load(iconURL).into(customViewHolder.weatherIcon);
        } else {
            customViewHolder.weatherIcon.setImageResource(android.R.color.transparent);
        }
    }

    @Override
    public int getItemCount() {
        return (null != baseResponseList ? baseResponseList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView cityName, dateTextView, descriptionTextView, temperatureTextView;
        protected ImageView weatherIcon;

        public CustomViewHolder(View view) {
            super(view);
            cityName = (TextView) view.findViewById(R.id.city_name_text_view);
            dateTextView = (TextView) view.findViewById(R.id.date_text_view);
            descriptionTextView = (TextView) view.findViewById(R.id.description_text_view);
            temperatureTextView = (TextView) view.findViewById(R.id.temperature_text_view);
            weatherIcon = (ImageView) view.findViewById(R.id.weather_icon_image_view);
        }
    }
}
