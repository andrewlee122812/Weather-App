package com.randomname.vlad.weathertest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.randomname.vlad.weathertest.Model.BaseResponse;
import com.randomname.vlad.weathertest.Model.City;
import com.randomname.vlad.weathertest.Model.ForecastListItem;
import com.randomname.vlad.weathertest.Model.Weather;
import com.randomname.vlad.weathertest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
        HeaderViewHolder customViewHolder = (HeaderViewHolder) holder;

        if (baseResponseHeaderInfo != null) {
            customViewHolder.cityNameTextView.setText(baseResponseHeaderInfo.getDisplayName());
        }
    }

    private void onBindMainViewHolder(RecyclerView.ViewHolder holder, int position) {
        ForecastViewHolder viewHolder = (ForecastViewHolder) holder;
        ForecastListItem item = forecastListItems.get(position);

        viewHolder.temperatureTextView.setText(item.getTemp().getMax() + "");

    }

    @Override
    public int getItemCount() {
        return (null != forecastListItems ? forecastListItems.size() + sizeDiff : 0);
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        protected TextView temperatureTextView;

        public ForecastViewHolder(View view) {
            super(view);
            temperatureTextView = (TextView) view.findViewById(R.id.temperature_text_view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        protected TextView cityNameTextView;

        public HeaderViewHolder(View view) {
            super(view);
            cityNameTextView = (TextView) view.findViewById(R.id.city_name_text_view);
        }
    }
}
