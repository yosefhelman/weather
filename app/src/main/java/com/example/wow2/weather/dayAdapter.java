package com.example.wow2.weather;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class dayAdapter extends ArrayAdapter<SQLForecast>
{
     dayAdapter(Context context, SQLForecast []forecasts)
    {
        super(context,R.layout.list_item,forecasts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("tag","getView run");
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View customView=inflater.inflate(R.layout.list_item,parent,false);

        TextView maxTemp=(TextView)customView.findViewById(R.id.max_temp);
        TextView minTemp=(TextView)customView.findViewById(R.id.min_temp);
        TextView type=(TextView)customView.findViewById(R.id.weather_type);

        SQLForecast forecast=getItem(position);



        maxTemp.setText(""+forecast.get_max_temp());
        minTemp.setText(""+forecast.get_min_temp());
        type.setText(forecast.get_weatherType());
        return customView;


    }
}
